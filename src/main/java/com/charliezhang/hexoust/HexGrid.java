package com.charliezhang.hexoust;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


/**
     * Constructs a Point with the given x and y coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
class Point {
    public Point(double x, double y) {
        /**
     * The x/y-coordinate of the point.
     */
        this.x = x;
        this.y = y;
    }
    public final double x;
    public final double y;

    /**
     * Compares this Point to another object for equality. Two Points are equal if their
     * x and y coordinates are identical.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // same reference
        if (obj == null || getClass() != obj.getClass()) return false; // not same type
        Point point = (Point) obj;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    /**
     * Computes the hash code for the Point based on its x and y coordinates.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

/**
 * Represents a hexagonal grid cell in cube coordinates.
 */
/**
     * Constructs a HexCube with the specified coordinates.
     *
     * @param q the q-coordinate
     * @param r the r-coordinate
     * @param s the s-coordinate
     * @throws IllegalArgumentException if q + r + s != 0
     */
class HexCube {
    public HexCube(int q, int r, int s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (q + r + s != 0)
            throw new IllegalArgumentException("q + r + s must be 0");
    }
    public final int q;
    public final int r;
    public final int s;

    /**
     * Adds the coordinates of another HexCube to this one.
     *
     * @param b the HexCube to add
     * @return a new HexCube resulting from the addition
     */
    public HexCube add(HexCube b) {
        return new HexCube(q + b.q, r + b.r, s + b.s);
    }

    /**
     * Subtracts the coordinates of another HexCube from this one.
     *
     * @param b the HexCube to subtract
     * @return a new HexCube resulting from the subtraction
     */
    public HexCube subtract(HexCube b) {
        return new HexCube(q - b.q, r - b.r, s - b.s);
    }

    static public ArrayList<HexCube> directions = new ArrayList<HexCube>() {{
        add(new HexCube(1, 0, -1));
        add(new HexCube(1, -1, 0));
        add(new HexCube(0, -1, 1));
        add(new HexCube(-1, 0, 1));
        add(new HexCube(-1, 1, 0));
        add(new HexCube(0, 1, -1));
    }};

    static public HexCube direction(int direction) {
        return HexCube.directions.get(direction);
    }

    /**
     * Returns the neighbor HexCube in the specified direction.
     *
     * @param direction the direction (0 to 5)
     * @return the neighbor HexCube
     */
    public HexCube neighbor(int direction) {
        return add(HexCube.direction(direction));
    }

    /**
     * Calculates the length of the HexCube from the origin (0, 0, 0).
     *
     * @return the length
     */
    public int length() {
        return (int)((Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2);
    }

    /**
     * Calculates the distance to another HexCube.
     *
     * @param b the other HexCube
     * @return the distance
     */
    public int distance(HexCube b) {
        return subtract(b).length();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HexCube hexCube = (HexCube) o;
        return q == hexCube.q && r == hexCube.r && s == hexCube.s;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(q, r, s);
    }

}

/**
 * Represents a fractional hexagonal grid cell in cube coordinates.
 */
class FractionalHexCube {
    public FractionalHexCube(double q, double r, double s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (Math.round(q + r + s) != 0)
            throw new IllegalArgumentException("q + r + s must be 0");
    }
    public final double q;
    public final double r;
    public final double s;

    /**
     * Rounds the fractional hex cube coordinates to the nearest integer HexCube.
     *
     * @return the rounded HexCube
     */
    public HexCube hexRound() {
        int qi = (int)(Math.round(q));
        int ri = (int)(Math.round(r));
        int si = (int)(Math.round(s));
        double q_diff = Math.abs(qi - q);
        double r_diff = Math.abs(ri - r);
        double s_diff = Math.abs(si - s);
        if (q_diff > r_diff && q_diff > s_diff) {
            qi = -ri - si;
        } else if (r_diff > s_diff) {
            ri = -qi - si;
        } else {
            si = -qi - ri;
        }
        return new HexCube(qi, ri, si);
    }
}

class Orientation {
    public Orientation(double f0, double f1, double f2, double f3,
                       double b0, double b1, double b2, double b3,
                       double start_angle) {
        this.f0 = f0;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.b0 = b0;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
        this.start_angle = start_angle;
    }
    public final double f0;
    public final double f1;
    public final double f2;
    public final double f3;
    public final double b0;
    public final double b1;
    public final double b2;
    public final double b3;
    public final double start_angle;
}

class Layout {
    public Layout(Orientation orientation, Point size, Point origin) {
        this.orientation = orientation;
        this.size = size;
        this.origin = origin;
    }
    public final Orientation orientation;
    public final Point size;
    public final Point origin;

    static public Orientation flat = new Orientation(3.0 / 2.0, 0.0, Math.sqrt(3.0) / 2.0, Math.sqrt(3.0),
            2.0 / 3.0, 0.0, -1.0 / 3.0, Math.sqrt(3.0) / 3.0, 0.0);

    /**
     * Converts a HexCube to a pixel Point.
     *
     * @param h the HexCube to convert
     * @return the corresponding Point in pixel coordinates
     */
    public Point hexToPixel(HexCube h) {
        Orientation M = orientation;
        double x = (M.f0 * h.q + M.f1 * h.r) * size.x;
        double y = (M.f2 * h.q + M.f3 * h.r) * size.y;
        return new Point(x + origin.x, y + origin.y);
    }


    /**
     * Converts a pixel Point to a FractionalHexCube.
     *
     * @param p the Point in pixel coordinates
     * @return the corresponding FractionalHexCube
     */
    public FractionalHexCube pixelToHex(Point p) {
        Orientation M = orientation;
        Point pt = new Point((p.x - origin.x) / size.x, (p.y - origin.y) / size.y);
        double q = M.b0 * pt.x + M.b1 * pt.y;
        double r = M.b2 * pt.x + M.b3 * pt.y;
        return new FractionalHexCube(q, r, -q - r);
    }

    /**
     * Calculates the offset for a hex corner.
     *
     * @param corner the corner index (0 to 5)
     * @return the offset Point
     */
    public Point hexCornerOffset(int corner) {
        Orientation M = orientation;
        double angle = 2.0 * Math.PI * (M.start_angle - corner) / 6.0;
        return new Point(size.x * Math.cos(angle), size.y * Math.sin(angle));
    }

    /**
     * Calculates the polygon corners for a HexCube.
     *
     * @param h the HexCube
     * @return an ArrayList of Points representing the polygon corners
     */
    public ArrayList<Point> polygonCorners(HexCube h) {
        ArrayList<Point> corners = new ArrayList<>();
        Point center = hexToPixel(h);
        for (int i = 0; i < 6; i++) {
            Point offset = hexCornerOffset(i);
            corners.add(new Point(center.x + offset.x, center.y + offset.y));
        }
        return corners;
    }
}

/**
 * Represents a hexagonal grid-based game. The grid supports placing stones, 
 * calculating neighbors, and determining game states such as winning conditions.
 * Includes graphical rendering and user interaction features.
 * <p>
 * The game alternates turns between two players (Red and Blue) and tracks game 
 * states, including capturing mechanics and win conditions. The class also 
 * manages rendering of the game board and user interactions via mouse clicks.
 */

public class HexGrid extends JPanel {

     /** Indicates whether the game is over. */
    private boolean gameOver = false;

    /** The grid of hexagons representing the game board. */
    final ArrayList<ArrayList<Point>> grid;

    /** List of points occupied by red stones. */
    final ArrayList<Point> redStones = new ArrayList<>();

    /** List of points occupied by blue stones. */
    public final ArrayList<Point> blueStones = new ArrayList<>();
    
    /** Tracks whether it is Red's turn to play. */
    public boolean isRedTurn = true;

    /** The background image displayed in the game. */
    private BufferedImage backgroundImage;

    /** The texture image applied to hexagons. */
    private BufferedImage Texture;

    /** The image used for the cloud-shaped reset button. */
    private BufferedImage cloudButtonImage;

    /** Bounds of the cloud-shaped reset button. */
    private Rectangle cloudButtonBounds;

    /** Tracks the current number of turns played. */
    int currentTurns = 0;

    /** List of confetti objects for the win animation. */
    public final ArrayList<Confetti> confettiList = new ArrayList<>();

    /** Flag indicating whether to show confetti animation. */
    public boolean showConfetti = false;

    /** Timer to control the animation of confetti. */
    private Timer confettiTimer;



    /**
     * Constructs a HexGrid object with the given hexagonal grid layout.
     *
     * @param hexagons The grid layout containing hexagonal tiles.
     */
    public HexGrid(ArrayList<ArrayList<Point>> hexagons) {
        this.grid = hexagons;
        try {
            backgroundImage = ImageIO.read(new File("sky.jpg")); // adds the image in the background (of a sky)
            Texture = ImageIO.read(new File("Texture.jpg")); // adds the texture
            cloudButtonImage = ImageIO.read(new File("cloud.png"));
            cloudButtonBounds = new Rectangle(700, 300, 200, 200); // x, y, width, height

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add mouse click listener for interactions
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (cloudButtonBounds.contains(e.getPoint())) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset the game?", "Reset Game", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        redStones.clear();
                        blueStones.clear();
                        isRedTurn = true;
                        gameOver = false;
                        currentTurns = 0;
                        repaint();
                    }
                    return;
                }

                Point click = new Point(e.getX(), e.getY());
                if (cloudButtonBounds.contains(e.getPoint())) {
                    resetGame();
                    return; // skip further click handling
                }

                if(!gameOver) {
                    // Loop over hexagons in the grid
                    for (ArrayList<Point> hexagon : grid) {
                        Point center = getHexCenter(hexagon);
                        double distance = Math.sqrt(Math.pow(center.x - click.x, 2) + Math.pow(center.y - click.y, 2));

                        // Check if click is within the hexagon's center radius (20 units)
                        if (distance <= 20) {
                            // Check if the cell is already occupied
                            if (redStones.contains(center) || blueStones.contains(center)) {
                                JOptionPane.showMessageDialog(null, "Cell Placement Invalid", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                            }

                            ArrayList<Point> neighbors = getNeighborCenters(center);


                            boolean touchesSame = false;
                            for (Point nb : neighbors) {
                                if (isRedTurn && redStones.contains(nb)) {
                                    touchesSame = true;
                                    break;
                                } else if (!isRedTurn && blueStones.contains(nb)) {
                                    touchesSame = true;
                                    break;
                                }
                            }

                            // If touching a same-color stone, build the connected chain and check for an opponent neighbor.
                            if (touchesSame) {
                                ArrayList<Point> chain = new ArrayList<>();
                                ArrayList<Point> line = new ArrayList<>();
                                chain.add(center);
                                line.add(center);

                                while (!line.isEmpty()) {
                                    Point current = line.remove(0);
                                    for (Point nb : getNeighborCenters(current)) {
                                        if (isRedTurn && redStones.contains(nb) && !chain.contains(nb)) {
                                            chain.add(nb);
                                            line.add(nb);
                                        } else if (!isRedTurn && blueStones.contains(nb) && !chain.contains(nb)) {
                                            chain.add(nb);
                                            line.add(nb);
                                        }
                                    }
                                }

                                boolean chainTouchesOpponent = false;
                                for (Point stone : chain) {
                                    for (Point nb : getNeighborCenters(stone)) {
                                        if (isRedTurn && blueStones.contains(nb)) {
                                            chainTouchesOpponent = true;
                                            break;
                                        } else if (!isRedTurn && redStones.contains(nb)) {
                                            chainTouchesOpponent = true;
                                            break;
                                        }
                                    }
                                    if (chainTouchesOpponent) break;
                                }

                                // Check the chained stones / if touching enemies
                                if (!chainTouchesOpponent) {
                                    JOptionPane.showMessageDialog(null, "Invalid move", "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                            }

                            if (isRedTurn) {
                                redStones.add(center);
                                currentTurns++;
                            } else {
                                blueStones.add(center);
                                currentTurns++;
                            }

                            // Check for any captured pieces and to remove them
                            boolean captured = checkAndChangeColour(center, isRedTurn);
                            repaint();

                            if (currentTurns > 2) {
                                if (blueStones.isEmpty()) {
                                    gameOver = true;
                                    startConfettiAnimation();
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "<html><div style='text-align: center;'><span style='font-size:20pt; color:red;'>Red Player Wins!!!</span></div></html>",
                                            "Game Over",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                    return;
                                } else if (redStones.isEmpty()) {
                                    gameOver = true;
                                    startConfettiAnimation();
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "<html><div style='text-align: center;'><span style='font-size:20pt; color:blue;'>Blue Player Wins!!!</span></div></html>",
                                            "Game Over",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                    return;
                                }
                            }


                            if (!captured) {
                                isRedTurn = !isRedTurn;
                            }

                            repaint();
                            break;  // Stop processing further hexagons for this click.
                        }
                    }
                }
            }
        });
    }

    /**
     * Starts the confetti animation when the game is won.
     * Confetti falls for 5 seconds and then stops.
     */
    public void startConfettiAnimation() {
        showConfetti = true;
        confettiList.clear();

        // Create a bunch of random confetti
        for (int i = 0; i < 500; i++) {
            int x = (int) (Math.random() * getWidth());
            int y = (int) (Math.random() * 100); // Start above the screen
            Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE};
            Color color = colors[(int) (Math.random() * colors.length)];
            confettiList.add(new Confetti(x, y, color));
        }

        // Animation timer
        confettiTimer = new Timer(30, e -> {
            for (Confetti c : confettiList) {
                c.fall();
            }
            repaint();
        });

        confettiTimer.start();

        // Stop after 5 seconds
        new Timer(5000, e -> {
            showConfetti = false;
            if (confettiTimer != null) confettiTimer.stop();
            repaint();
        }).start();
    }
    
    /**
     * Resets the game by clearing all stones, resetting the turn, and repainting the board.
     */
    public void resetGame() {
        redStones.clear();
        blueStones.clear();
        isRedTurn = true;
        currentTurns = 0;
        gameOver = false;
        repaint();
    }

    /**
     * Checks and changes the color of stones based on capturing rules.
     *
     * @param center      The center point of the recently placed stone.
     * @param isRedTurn   Whether the current turn belongs to the red player.
     * @return True if any opponent stones were captured, otherwise false.
     */
    public boolean checkAndChangeColour(Point center, boolean isRedTurn) {
        ArrayList<Point> chain = new ArrayList<>();
        ArrayList<Point> line = new ArrayList<>();

        chain.add(center);
        line.add(center);

        while (!line.isEmpty()) {
            Point current = line.remove(0);

            for (Point nb : getNeighborCenters(current)) {
                if (isRedTurn && redStones.contains(nb) && !chain.contains(nb)) {
                    chain.add(nb);
                    line.add(nb);

                } else if (!isRedTurn && blueStones.contains(nb) && !chain.contains(nb)) {
                    chain.add(nb);
                    line.add(nb);
                }
            }
        }

        // made it so it can only capture if the chain is greater than 2 stones
        if (chain.size() < 2) {
            return false;
        }

        ArrayList<Point> removeStones = new ArrayList<>();
        for (Point stone : chain) {

            for (Point nb : getNeighborCenters(stone)) {
                if (isRedTurn && blueStones.contains(nb) && !removeStones.contains(nb)) {
                    removeStones.add(nb);
                } else if (!isRedTurn && redStones.contains(nb) && !removeStones.contains(nb)) {
                    removeStones.add(nb);
                }

            }
        }

        for (Point p : removeStones) {
            if (isRedTurn) {
                blueStones.remove(p);
            } else {
                redStones.remove(p);
            }
        }
        repaint();

        // Return true if any opponent pieces were captured.
        return !removeStones.isEmpty();
    }

    /**
     * Computes the center of a hexagon by averaging its vertices.
     *
     * @param hexagon The list of points representing the vertices of the hexagon.
     * @return The center point of the hexagon.
     */
    Point getHexCenter(ArrayList<Point> hexagon) {
        double sumX = 0;
        double sumY = 0;
        for (Point p : hexagon) {
            sumX += p.x;
            sumY += p.y;
        }
        return new Point(sumX / hexagon.size(), sumY / hexagon.size());
    }

    /**
     * Retrieves the neighbor centers of a given hexagon center.
     *
     * @param center The center of the hexagon whose neighbors are to be found.
     * @return A list of points representing the neighboring hexagons' centers.
     */
    private ArrayList<Point> getNeighborCenters(Point center) {
        ArrayList<Point> neighbors = new ArrayList<>();
        for (ArrayList<Point> hexagon : grid) {
            Point neighbor = getHexCenter(hexagon);
            double distance = Math.sqrt(Math.pow(center.x - neighbor.x, 2) + Math.pow(center.y - neighbor.y, 2));
            // Using 45 as the approximate center-to-center distance threshold
            if (distance > 0 && distance <= 45) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    /**
     * Renders the game board and its elements, including hexagons, stones, and UI elements.
     *
     * @param g The Graphics object used for rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the background image (sky)
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(Color.CYAN); // Fallback if image fails to load
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 70));
        g2.drawString("HexOust", 700, 100);

        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g2.drawString("Game Developers:", 700, 150);
        g2.drawString("Mahad Maqsood", 750, 175);
        g2.drawString("Hamza Yasin", 750, 200);
        g2.drawString("Charlie Zhang", 750, 225);


        // Draw hex grid with texture
        for (ArrayList<Point> hexagon : grid) {
            int[] xPoints = new int[hexagon.size()];
            int[] yPoints = new int[hexagon.size()];

            for (int i = 0; i < hexagon.size(); i++) {
                xPoints[i] = (int) Math.round(hexagon.get(i).x);
                yPoints[i] = (int) Math.round(hexagon.get(i).y);
            }
            Polygon hexPolygon = new Polygon(xPoints, yPoints, hexagon.size());
            // Fill hexagon with grass texture
            if (Texture != null) {

                Rectangle hexBounds = hexPolygon.getBounds();
                BufferedImage subImage = Texture.getSubimage(100, 100, Math.min(Texture.getWidth(), hexBounds.width), Math.min(Texture.getHeight(), hexBounds.height));
                TexturePaint grassPaint = new TexturePaint(subImage, hexBounds);
                g2.setPaint(grassPaint);
            } else {
                g2.setColor(new Color(34, 139, 34));
            }

            g2.fillPolygon(hexPolygon); // Fill hex with grass
            // Draw hexagon border
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.drawPolygon(xPoints, yPoints, hexagon.size());
        }
        Color customBlue = new Color(48, 30, 165);
        Color customRed = new Color(195, 0, 0);
        // Draw player stones (filling entire hexagon)
        for (Point p : redStones) {
            fillHexWithColor(g2, p, customRed);
        }
        for (Point p : blueStones) {
            fillHexWithColor(g2, p, customBlue);
        }

        // Draw turn indicator
        int circleX = 30;
        int circleY = getHeight() - 50;
        int circleDiameter = 30;

        g.setColor(isRedTurn ? Color.RED : Color.BLUE);
        g.fillOval(circleX, circleY, circleDiameter, circleDiameter);

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("To Make A Move", circleX + circleDiameter + 10, circleY + 23);

        if (cloudButtonImage != null) {
            g2.drawImage(cloudButtonImage, cloudButtonBounds.x, cloudButtonBounds.y,
                    cloudButtonBounds.width, cloudButtonBounds.height, this);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
            FontMetrics fm = g2.getFontMetrics();
            String text = "Reset";
            int textWidth = fm.stringWidth(text);
            int textX = cloudButtonBounds.x + (cloudButtonBounds.width - textWidth) / 2;
            int textY = cloudButtonBounds.y + (cloudButtonBounds.height + fm.getAscent()) / 2 - 5;
            g2.drawString(text, textX, textY);

            if(showConfetti)

            {
                for (Confetti c : confettiList) {
                    c.draw(g2);
                }
            }

        }
    }


    /**
     * Fills a hexagon with the specified color.
     *
     * @param g2     The Graphics2D object used for rendering.
     * @param center The center point of the hexagon to fill.
     * @param color  The color to fill the hexagon with.
     */
    // Helper function to fill a hex with a given color
    private void fillHexWithColor(Graphics2D g2, Point center, Color color) {
        for (ArrayList<Point> hexagon : grid) {
            Point hexCenter = getHexCenter(hexagon);
            if (hexCenter.equals(center)) {
                int[] xPoints = new int[hexagon.size()];
                int[] yPoints = new int[hexagon.size()];

                for (int i = 0; i < hexagon.size(); i++) {
                    xPoints[i] = (int) Math.round(hexagon.get(i).x);
                    yPoints[i] = (int) Math.round(hexagon.get(i).y);
                }

                GradientPaint gradient = new GradientPaint(
                        (float) xPoints[0], (float) yPoints[0], color.brighter(),
                        (float) xPoints[3], (float) yPoints[3], color.darker()
                );

                g2.setPaint(gradient);
                g2.fillPolygon(xPoints, yPoints, hexagon.size());

                g2.setColor(color.darker());
                g2.setStroke(new BasicStroke(2));
                g2.drawPolygon(xPoints, yPoints, hexagon.size());
                break;


            }
        }
    }

    /**
     * Main method to run the HexGrid application.
     *
     * @param args Command-line arguments specifying size and origin coordinates.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("java HexGrid <size> <originx> <originy>");
            System.exit(1);
        }

        double size = 0.0, originx = 0.0, originy = 0.0;
        try {
            size = Double.parseDouble(args[0]);
            originx = Double.parseDouble(args[1]);
            originy = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Problems parsing double arguments.");
            System.exit(1);
        }

        Layout flat = new Layout(Layout.flat,
                new Point(size, size),
                new Point(originx, originy));

        int baseN = 6;
        ArrayList<ArrayList<Point>> grid = new ArrayList<>();
        for (int q = -baseN; q <= baseN; q++) {
            for (int r = -baseN; r <= baseN; r++) {
                for (int s = -baseN; s <= baseN; s++) {
                    if ((q + r + s) == 0) {
                        HexCube h = new HexCube(q, r, s);
                        ArrayList<Point> corners = flat.polygonCorners(h);
                        grid.add(corners);
                    }
                }
            }
        }

        JFrame frame = new JFrame("HexGrid");
        HexGrid panel = new HexGrid(grid);
        frame.add(panel);
        frame.setSize(1100, 768);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Type quit to exit: ");
            String input = scanner.next().toLowerCase();
            if (input.equals("quit")) {
                System.out.println("Exiting Game...");
                System.out.println("GoodBye :)");
                frame.dispose();
                System.exit(0);
            }
        }
    }
}
