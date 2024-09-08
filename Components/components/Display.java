package Components.components;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Display extends JPanel {

    private final int pixelSize = 10;
    private Color[][] pixelColours = new Color[32][64];

    public Display()
    {
        for (int row = 0; row < pixelColours.length; row++) {
            Arrays.fill(pixelColours[row], Color.WHITE);
        }

        pixelColours[5][7] = Color.BLACK;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set background color
        this.setBackground(Color.WHITE);

        for (int row = 0; row < pixelColours.length; row++) {
            for (int col = 0; col < pixelColours[row].length; col++) {
                // Set color for each pixel
                g.setColor(pixelColours[row][col]);
                // Draw filled rectangle (the pixel)
                g.fillRect(col * pixelSize, row * pixelSize, pixelSize, pixelSize);
            }
        }

        // Set grid color
        g.setColor(Color.BLACK);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("64x32 Pixel Grid");
        Display grid = new Display();
        frame.add(grid);
        frame.setSize(640, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}