import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a single confetti particle used for visual effects in the game.
 * Confetti particles have properties such as position, speed, and color,
 * and they can move and draw themselves on the screen.
 */
public class Confetti {

    private int x, y;           /** Position of the confetti */
    private int speedX, speedY; /** Speed in the X and Y directions */
    private Color color;        /** Color of the confetti */

    /**
     * Constructs a confetti particle with the specified position and color.
     * The speed is randomized to create varied movement patterns.
     *
     * @param x     The initial x-coordinate of the confetti.
     * @param y     The initial y-coordinate of the confetti.
     * @param color The color of the confetti.
     */
    public Confetti(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        // Randomize the speeds to make the confetti move in different directions
        this.speedX = (int) (Math.random() * 6) - 3;  // Random between -3 and 3
        this.speedY = (int) (Math.random() * 6) - 3;  // Random between -3 and 3
    }

    /**
     * Updates the position of the confetti to simulate falling motion.
     * The speed in the Y direction increases over time to simulate gravity.
     */
    public void fall() {
        x += speedX; // Move horizontally
        y += speedY; // Move vertically

        // Apply gravity effect (fall faster over time)
        speedY += 1; // Increase downward speed to simulate gravity
    }

    /**
     * Draws the confetti on the screen using the specified graphics context.
     *
     * @param g2 The Graphics2D object used to draw the confetti.
     */
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillOval(x, y, 10, 10);  // Draw a small circle for each confetti piece
    }
}
