import Components.components.*;

import java.io.*;

public class Chip8 {
    Display display;
    Memory memory;
    Stack stack;


    Register[] registers;

    int pc = 0x200;
    int I = 0x000;

    int rate = 3;

    public Chip8(String filePath)
    {
        display = new Display();
        memory = new Memory();
        stack = new Stack();
        registers = new Register[16];
        for (int i = 0; i < registers.length; i++)
        {
            registers[i] = new Register();
        }

        try{
            this.loadRom(filePath);
        }
        catch (IOException e)
        {
            System.out.println("File " + filePath + " failed to read. Exiting...");
            System.exit(400);
        }

    }

    public void run()
    {
        Display.createDisplay(display);

        long delay = 1000 / rate;

        while (true)
        {
            long start = System.currentTimeMillis();

            // execute next instruction here //
            byte first = memory.read(pc);
            byte second = memory.read(pc + 1);

            byte op = (byte) ((first >> 4) & 0x0F); // first nibble
            byte x = (byte) (first & 0x0F);  // second nibble
            byte y = (byte) ((second >> 4) & 0x0F); // third nibble
            byte n = (byte) (second & 0x0F); // fourth nibble
            byte nn = second; // third and fourth nibbles
            int nnn = (x << 8) | (y << 4) | n; // second, third and fourth nibbles

            pc += 2;
            //System.out.println(pc);

            switch (op)
            {
                // clear screen
                case 0x0:
                    System.out.println("clearing display");
                    display.clear();
                    break;
                // jump
                case 0x1:
                    System.out.print("jumping to " + nnn + "\n");
                    pc = nnn;
                    break;
                // set register Vx
                case 0x6:
                    System.out.print("writing to register" + x + "\n");
                    registers[x].write(nn);
                    break;
                // add to register Vx
                case 0x7:
                    registers[x].add(nn);
                    break;
                // set index register
                case 0xA:
                    I = nnn;
                    break;
                // display/draw
                case 0xD:
                    registers[0xF].write((byte) 0);  // Reset the collision flag


                    for (int row = 0; row < n; row++) {
                        int spriteByte = memory.read(I + row);  // Get sprite row from memory
                        for (int col = 0; col < 8; col++) {
                            int spritePixel = (spriteByte >> (7 - col)) & 1;  // Get each bit (pixel) of the sprite
                            int displayX = (x + col) % 64;  // Wrap around horizontally
                            int displayY = (y + row) % 32;  // Wrap around vertically

                            // Check if we are XORing with an existing pixel
                            if (spritePixel == 1) {
                                if (display.pixels[displayY][displayX] == 1) {
                                    registers[0xF].write((byte) 0x1);  // Set collision flag if a pixel is turned off
                                }
                                display.pixels[displayY][displayX] ^= 1;  // XOR the pixel
                            }
                        }
                    }
                    break;
            }


            // delay to keep fixed instruction rate
            long elapsedTime = System.currentTimeMillis() - start;
            long sleepTime = delay - elapsedTime;

            if (sleepTime > 0)
            {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void loadRom(String filePath) throws IOException
    {
        FileInputStream inputStream;

        File romFile = new File(filePath);
        try {
            inputStream = new FileInputStream(romFile);
        }
        catch (FileNotFoundException e)
        {
            throw new IOException("File not Found: " + filePath);
        }

        // Read the ROM file byte by byte
        byte[] romData = new byte[(int) romFile.length()];
        int bytesRead = inputStream.read(romData);

        if (bytesRead != romData.length) {
            throw new IOException("ROM file too large.");
        }

        // writes ROM data into memory
        for (int i = 0; i < romData.length; i++) {
            memory.write(0x200 + i, romData[i]);
        }

        inputStream.close();
    }

}
