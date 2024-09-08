import Components.components.*;

import java.io.*;

public class Chip8 {
    Display display;
    Memory memory;
    Stack stack;

    Register[] registers;

    int pc = 0x200;

    int rate = 100;

    public Chip8(String filePath)
    {
        display = new Display();
        memory = new Memory();
        stack = new Stack();
        registers = new Register[16];
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
        long delay = 1000 / rate;

        while (true)
        {
            long start = System.currentTimeMillis();

            // execute next instruction here //
            byte first = memory.read(pc);
            byte second = memory.read(pc + 1);

            byte nib1 = (byte) ((first >> 4) & 0x0F); // first nibble
            byte x = (byte) (first & 0x0F);  // second nibble
            byte y = (byte) ((second >> 4) & 0x0F); // third nibble
            byte n = (byte) (second & 0x0F); // fourth nibble
            int nnn = (x << 8) | (y << 4) | n; // second, third and fourth nibbles

            

            pc += 2;




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
