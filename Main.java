public class Main {

    static Chip8 emu;

    public static void main(String[] args)
    {
        String path = "Roms/Breakout.ch8";
        int rate = 100;

        emu = new Chip8(path, rate);
        emu.run();
    }
}
