import Components.components.*;

import java.io.*;

public class Chip8 {
    Display display;
    Memory memory;
    Stack stack;


    Register[] registers;

    int pc = 0x200;
    int I = 0x000;

    int rate;

    public Chip8(String filePath, int rate)
    {
        this.rate = rate;

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

    public void run() {
        Display.createDisplay(display);

        long delay = 1000 / rate;

        while (true)
        {
            long start = System.currentTimeMillis();

            // execute next instruction here //
            int first = (memory.read(pc) & 0xFF);
            int second =  (memory.read(pc + 1) & 0xFF);

            int opcode = ((first & 0xFF) << 8) | (second & 0xFF);
            byte op = (byte) ((opcode >> 12) & 0x0F);  // First nibble
            byte x = (byte) ((opcode >> 8) & 0x0F);    // Second nibble
            byte y = (byte) ((opcode >> 4) & 0x0F);    // Third nibble
            byte n = (byte) (opcode & 0x0F);           // Fourth nibble
            byte nn = (byte) (opcode & 0xFF);          // Last 8 bits
            int nnn = opcode & 0x0FFF;                 // Last 12 bits

            pc += 2;

            switch (op)
            {
                // 00e0 clear screen
                case 0x0:
                    System.out.println("Clearing display.");
                    display.clear();
                    break;
                // 1nnn jump
                case 0x1:
                    pc = nnn;
                    break;
                // 3xnn skip conditionally
                case 0x3:
                    if (registers[x].read() == nn)
                    {
                        pc += 2;
                    }
                    break;
                // 4xnn skip conditionally
                case 0x4:
                {
                    if (registers[x].read() != nn)
                    {
                        pc += 2;
                    }
                    break;
                }
                // 5xy0 skip conditionally
                case 0x5:
                    if (registers[x].read() == registers[y].read())
                    {
                        pc += 2;
                    }
                    break;
                // 6xnn set register Vx
                case 0x6:
                    registers[x].write(nn);
                    break;
                // 7xnn add to register Vx
                case 0x7:
                    registers[x].add(nn);
                    break;
                // 9xy0 skip conditionally
                case 0x9:
                    if (registers[x].read() != registers[y].read())
                    {
                        pc += 2;
                    }
                    break;
                // Annn set index register
                case 0xA:
                    I = nnn;
                    break;
                // Dxyn display/draw
                case 0xD:
                    registers[0xF].write((byte) 0);  // Reset the collision flag

                    int xPos = registers[x].read() % 64;
                    int yPos = registers[y].read() % 32;

                    for (int spriteRow=0; spriteRow<n; ++spriteRow)
                    {
                        var spriteRowData = memory.read(I + spriteRow);
                        for(int bit = 0; bit < 8; bit++)
                        {
                            if(xPos + bit < 64 && yPos + spriteRow < 32)
                            {
                                int spritePixel = (spriteRowData >> (7-bit)) & 1;
                                int screenPixel = display.pixels[yPos + spriteRow][xPos + bit];
                                if (spritePixel == 1 & screenPixel == 1)
                                {
                                    registers[0xF].write((byte) 1);
                                }

                                display.pixels[yPos + spriteRow][xPos + bit] ^= spritePixel;
                            }
                            else {
                                System.out.println("Out of bounds draw attempt");
                            }
                        }
                    }
                    display.repaint();
                    break;

                // 8 depends on the value of the last nibble
                case 0x8:
                    int xval;
                    int yval;
                    int sum;
                    int difference;
                    switch (n)
                    {
                        // 8xy0 set
                        case 0x0:
                            registers[x].write(registers[y].read());
                            break;
                        // 8xy1 binary OR
                        case 0x1:
                            registers[x].write( (byte) (registers[x].read() | registers[y].read()) );
                            break;
                        // 8xy2 binary AND
                        case 0x2:
                            registers[x].write( (byte) (registers[x].read() & registers[y].read()) );
                            break;
                        // 8xy3 binary XOR
                        case 0x3:
                            registers[x].write( (byte) (registers[x].read() ^ registers[y].read()) );
                            break;
                        // 8xy4 add
                        case 0x4:
                            xval = registers[x].read();
                            if (xval < 0) {
                                xval = Math.abs(xval) + 128;
                            }

                            yval = registers[y].read();
                            if (yval < 0) {
                                yval = Math.abs(yval) + 128;
                            }

                            sum = xval + yval;
                            // set overflow flag
                            if (sum > 255)
                            {
                                registers[0xF].write((byte) 1);
                            }

                            registers[x].write( (byte) (sum % 255));

                            break;
                        // 8xy5 sub
                        case 0x5:
                            xval = registers[x].read();
                            if (xval < 0) {
                                xval = Math.abs(xval) + 128;
                            }

                            yval = registers[y].read();
                            if (yval < 0) {
                                yval = Math.abs(yval) + 128;
                            }

                            difference = xval - yval;
                            // set overflow flag
                            if (difference < 0) {
                                difference = Math.abs(difference) + 128;
                                registers[0xF].write((byte) (0));
                            }
                            else {
                                registers[0xF].write((byte) (1));
                            }

                            {
                                registers[0xF].write((byte) 1);
                            }

                            registers[x].write( (byte) (difference % 255));

                            break;
                        // 8xy7 sub
                        case 0x7:
                            xval = registers[x].read();
                            if (xval < 0) {
                                xval = Math.abs(xval) + 128;
                            }

                            yval = registers[y].read();
                            if (yval < 0) {
                                yval = Math.abs(yval) + 128;
                            }

                            difference = yval - xval;
                            // set overflow flag
                            if (difference < 0) {
                                difference = Math.abs(difference) + 128;
                                registers[0xF].write((byte) (0));
                            }
                            else {
                                registers[0xF].write((byte) (1));
                            }

                        {
                            registers[0xF].write((byte) 1);
                        }

                        registers[x].write( (byte) (difference % 255));

                        break;
                    }
                    break;

                default:
                    System.out.printf("Error with opcode: %d %d %d %d%n", op, x, y, n);
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
