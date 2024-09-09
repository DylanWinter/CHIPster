package Components.components;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class Display extends JPanel {
    public static Color on = Color.WHITE;
    public static Color off = Color.BLACK;
    public static Color gridColor = Color.GRAY;

    private static final int pixelSize = 18;

    private static final boolean drawGrid = false;
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
        this.setBackground(off);

        int gridWidth = pixels[0].length * pixelSize;
        int gridHeight = pixels.length * pixelSize;

        // Calculate the offset to center the grid
        int xOffset = (getWidth() - gridWidth) / 2;
        int yOffset = (getHeight() - gridHeight) / 2;

        // Draw the pixels
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[row].length; col++) {
                // Set color for each pixel
                if (pixels[row][col] == 1) {
                    g.setColor(on);
                } else {
                    g.setColor(off);
                }

                // Draw filled rectangle (the pixel), applying the offset
                g.fillRect(xOffset + col * pixelSize, yOffset + row * pixelSize, pixelSize, pixelSize);
            }
        }


        g.setColor(gridColor);
        if (drawGrid)
        {

            // Draw vertical grid lines with the offset
            for (int col = 0; col <= pixels[0].length; col++) {
                g.drawLine(xOffset + col * pixelSize, yOffset, xOffset + col * pixelSize, yOffset + gridHeight);

                // Draw horizontal grid lines with the offset
                for (int row = 0; row <= pixels.length; row++) {
                    g.drawLine(xOffset, yOffset + row * pixelSize, xOffset + gridWidth, yOffset + row * pixelSize);
                }
            }
        }
        else
        {
            g.drawRect(xOffset, yOffset, pixelSize * 64, pixelSize * 32);
        }


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
        frame.setSize(1280, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}