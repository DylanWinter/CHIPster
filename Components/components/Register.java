package Components.components;

public class Register {
    byte data;

    public Register()
    {
        data = 0x0;
    }

    public byte read()
    {
        return (byte) (data & 0xFF);
    }

    public void write(byte newData)
    {
        data = (byte) (newData & 0xFF);
    }

    public void add(byte valueToAdd)
    {
        data += valueToAdd;
    }

}
