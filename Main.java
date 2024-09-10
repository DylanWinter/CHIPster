public class Main {

    static Chip8 emu;

    public static void main(String[] args)
    {
        String path = "Roms/Airplane.ch8";
        int rate = 300;

        emu = new Chip8(path, rate);
        emu.run();
    }
}
