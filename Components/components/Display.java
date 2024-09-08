package Components.components;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class Display extends JPanel {
    public static Color on = Color.WHITE;
    public static Color off = Color.BLACK;

    private final int pixelSize = 10;
    public int[][] pixels = new int[32][64];

    public Display()
    {
        for (int row = 0; row < pixels.length; row++) {
            Arrays.fill(pixels[row], 0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set background color
        this.setBackground(off);

        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[row].length; col++) {
                // Set color for each pixel
                if (pixels[row][col] == 1) {
                    g.setColor(on);
                }
                else {
                    g.setColor(off);
                }

                // Draw filled rectangle (the pixel)
                g.fillRect(col * pixelSize, row * pixelSize, pixelSize, pixelSize);
            }
        }

        // Set grid color
        g.setColor(on);
    }

    public void clear()
    {
        for (int row = 0; row < pixels.length; row++) {
            Arrays.fill(pixels[row], 0);
        }

        this.repaint();
    }

    public static void createDisplay(Display d) {
        JFrame frame = new JFrame("CHIPster");
        frame.add(d);
        frame.setSize(640, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}