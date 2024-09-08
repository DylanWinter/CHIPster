public class Main {

    static Chip8 emu;

    public static void main(String[] args)
    {
        String path = "Roms/IBMLogo.ch8";

        emu = new Chip8(path);
        emu.run();
    }
}
