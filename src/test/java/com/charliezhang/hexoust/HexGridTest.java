package com.charliezhang.hexoust;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HexGrid class. This class includes tests for various functionalities
 * such as grid initialization, game mechanics, stone placement, and win conditions.
 * <p>
 * Each test ensures that the behavior of the HexGrid class adheres to the expected outcomes.
 */
class HexGridTest {

    /**
     * Helper method to create a sample hexagonal grid with 3 rows and 3 columns.
     *
     * @return A 3x3 grid of points representing a hexagonal grid layout.
     */
    private ArrayList<ArrayList<Point>> createSampleHexGrid() {
        ArrayList<ArrayList<Point>> grid = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            ArrayList<Point> hexRow = new ArrayList<>();
            for (int col = 0; col < 3; col++) {
                hexRow.add(new Point(col * 100, row * 50));
            }
            grid.add(hexRow);
        }
        return grid;
    }

    private HexGrid hexGrid;

    /**
     * Sets up the HexGrid instance before each test case.
     */
    @BeforeEach
    void setUp() {
        hexGrid = new HexGrid(createSampleHexGrid());
    }

    /**
     * Verifies that the HexGrid is initialized correctly with the expected grid layout.
     */
    @Test
    public void testSetup() {

        assertNotNull(hexGrid.getClass(), "Grid should not be null.");
        assertNotNull(hexGrid, "HexGrid should be initialized");

        ArrayList<ArrayList<Point>> grid = hexGrid.grid;

        assertEquals(3, grid.size(), "Grid should have 3 rows");

        ArrayList<Point> firstHexRow = grid.get(0);
        assertEquals(3, firstHexRow.size(), "First row should have 3 columns");

        assertEquals(0.0, firstHexRow.get(0).x, 0.001, "The x-coordinate of the first hex should be 0.0");
        assertEquals(0.0, firstHexRow.get(0).y, 0.001, "The y-coordinate of the first hex should be 0.0");
    }

    /**
     * Tests the addition operation for HexCube objects.
     */
    @Test
    public void testAddition() {
        HexCube a = new HexCube(1, -2, 1);
        HexCube b = new HexCube(3, 1, -4);
        HexCube result = a.add(b);
        assertEquals(4, result.q);
        assertEquals(-1, result.r);
        assertEquals(-3, result.s);
    }

    /**
     * Tests the getHexCenter method to ensure it returns the correct center of a hexagon.
     */
    @Test
    public void testGetHexCenter() {
        ArrayList<Point> row = hexGrid.grid.get(1);
        Point expectedCenter = row.get(1);

        //Call method to get the actual center
        Point actualCenter = hexGrid.getHexCenter(row);

        //Check that expected and actual are equal
        assertEquals(expectedCenter, actualCenter, "getHexCenter should return the middle hex in the row");
    }

    /**
     * Tests the subtraction operation for HexCube objects.
     */
    @Test
    void testSubtraction() {
        HexCube a = new HexCube(2, -3, 1);
        HexCube b = new HexCube(1, -1, 0);
        HexCube result = a.subtract(b);

        assertEquals(1, result.q);
        assertEquals(-2, result.r);
        assertEquals(1, result.s);
    }

    /**
     * Tests the distance calculation between two HexCube objects.
     */
    @Test
    void testDistance() {
        HexCube a = new HexCube(0, 0, 0);
        HexCube b = new HexCube(2, -1, -1);

        assertEquals(2, a.distance(b));
    }
    
    /**
     * Tests the rounding of fractional hex coordinates to the nearest integer hex.
     */
    @Test
    void testHexRoundExactCenter() {
        FractionalHexCube fractionalHex = new FractionalHexCube(0.0, 0.0, 0.0);
        HexCube roundedHex = fractionalHex.hexRound();
        assertEquals(new HexCube(0, 0, 0), roundedHex, "Exact center rounding should return (0, 0, 0).");
    }

        /**
     * Tests the rounding of fractional hex coordinates near boundaries.
     */
    @Test
    void testHexRoundBoundaryCase() {
        FractionalHexCube fractionalHex = new FractionalHexCube(0.5, -1.5, 1.0);
        HexCube roundedHex = fractionalHex.hexRound();
        assertEquals(new HexCube(1, -2, 1), roundedHex, "Boundary case rounding should return (1, -2, 1).");
    }
    /**
     * Tests that neighbors of a HexCube are calculated correctly.
     */
    @Test
    public void testNeighbors() {
        HexCube origin = new HexCube(0, 0, 0);

        // Iterate through all 6 directions (neighbors)
        for (int i = 0; i < 6; i++) {
            HexCube neighbor = origin.neighbor(i);

            // Check that the distance from the origin to the neighbor is 1
            assertEquals(1, origin.distance(neighbor), "Distance should be 1");
        }
    }

    /**
     * Tests the placement of the first red stone on the grid and verifies its position.
     */
    @Test
    public void testFirstStonePlacement() {
        Point center = hexGrid.getHexCenter(hexGrid.grid.get(0));

        // Add the red stone to the grid
        hexGrid.redStones.add(center);

        // Assert that the red stone has been added
        assertEquals(1, hexGrid.redStones.size(), "There should be 1 red stone.");
        assertTrue(hexGrid.redStones.contains(center), "Red stone should be placed at the correct center.");
    }

    /**
     * Ensures no capture occurs when a blue stone is surrounded by only one red stone.
     */
    @Test
    public void testNoCaptureWithSingleRedStone() {
        Point center = hexGrid.grid.get(1).get(1);
        Point neighborLeft = hexGrid.grid.get(1).get(0);

        // One red stone only
        hexGrid.redStones.add(neighborLeft);
        hexGrid.blueStones.add(center);

        boolean captured = hexGrid.checkAndChangeColour(center, true);

        //Check false for captured
        assertFalse(captured, "Blue stone should not be captured with only one red neighbor");
        assertTrue(hexGrid.blueStones.contains(center), "Blue stone should remain");
    }

    /**
     * Tests that adding a red stone increases the count of red stones on the grid.
     */
    @Test
    public void testRedStonePlacementIncreasesCount() {
        int initialCount = hexGrid.redStones.size();
        Point target = hexGrid.grid.get(2).get(1);

        hexGrid.redStones.add(target);

        //Check red count has increased and position is correct
        assertEquals(initialCount + 1, hexGrid.redStones.size(), "Red stone count should increase by 1");
        assertTrue(hexGrid.redStones.contains(target), "Red stone should be added at specified location");
    }

    /**
     * Tests that adding a blue stone increases the count of blue stones on the grid.
     */
    @Test
    public void testBlueStonePlacementIncreasesCount() {
        int initialCount = hexGrid.blueStones.size();
        Point target = hexGrid.grid.get(2).get(1);

        hexGrid.blueStones.add(target);

        //Check blue count has increased and position is correct
        assertEquals(initialCount + 1, hexGrid.blueStones.size(), "Blue stone count should increase by 1");
        assertTrue(hexGrid.blueStones.contains(target), "Blue stone should be added at specified location");
    }

    /**
     * Tests that the resetGame method properly clears the board and resets the turn.
     */
    @Test
    public void testResetGame() {
        hexGrid.redStones.add(new Point(0, 0));
        hexGrid.blueStones.add(new Point(100, 100));
        hexGrid.currentTurns = 5;
        hexGrid.isRedTurn = false;

        hexGrid.resetGame();

        //Check that everything is reset
        assertTrue(hexGrid.redStones.isEmpty(), "Red stones should be cleared");
        assertTrue(hexGrid.blueStones.isEmpty(), "Blue stones should be cleared");
        assertEquals(0, hexGrid.currentTurns, "Turns should reset to 0");
        assertTrue(hexGrid.isRedTurn, "Red should go first after reset");
    }

    /**
     * Tests that the confetti animation starts and behaves as expected when triggered.
     */
    @Test
    public void testStartConfettiAnimation() {
        hexGrid.startConfettiAnimation();

        //Check that confetti is active
        assertTrue(hexGrid.showConfetti, "Confetti should be showing");
        assertFalse(hexGrid.confettiList.isEmpty(), "Confetti list should be populated");
    }
}
