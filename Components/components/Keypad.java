package Components.components;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keypad extends JPanel implements KeyListener {
    private boolean[] keys;

    public Keypad() {
        keys = new boolean[16];
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required to implement KeyListener
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int key = getKeyFromKeyCode(keyCode);

        if (key != -1) {
            setKey(key, true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int key = getKeyFromKeyCode(keyCode);

        if (key != -1) {
            setKey(key, false);
        }
    }

    public boolean isAnyKeyPressed() {
        for (boolean keyState : keys) {
            if (keyState) {
                return true;
            }
        }
        return false;
    }

    private int getKeyFromKeyCode(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_1: return 0x1;
            case KeyEvent.VK_2: return 0x2;
            case KeyEvent.VK_3: return 0x3;
            case KeyEvent.VK_4: return 0xC;
            case KeyEvent.VK_Q: return 0x4;
            case KeyEvent.VK_W: return 0x5;
            case KeyEvent.VK_E: return 0x6;
            case KeyEvent.VK_R: return 0xD;
            case KeyEvent.VK_A: return 0x7;
            case KeyEvent.VK_S: return 0x8;
            case KeyEvent.VK_D: return 0x9;
            case KeyEvent.VK_F: return 0xE;
            case KeyEvent.VK_Z: return 0xA;
            case KeyEvent.VK_X: return 0x0;
            case KeyEvent.VK_C: return 0xB;
            case KeyEvent.VK_V: return 0xF;
            default: return -1;
        }
    }

    // Set the state of a specific key
    public void setKey(int key, boolean pressed) {
        if (key >= 0 && key < 16) {
            keys[key] = pressed;
        } else {
            throw new IllegalArgumentException("Key value must be between 0 and 15");
        }
    }

    // Check if a specific key is pressed
    public boolean isKeyPressed(int key) {
        if (key >= 0 && key < 16) {
            return keys[key];
        } else {
            throw new IllegalArgumentException("Key value must be between 0 and 15");
        }
    }

    // Clear all keys
    public void clearKeys() {
        java.util.Arrays.fill(keys, false);
    }
}
