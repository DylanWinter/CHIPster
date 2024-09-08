package Components.components;

public class Register {
    byte data;

    public Register()
    {
        data = 0x0;
    }

    public byte read()
    {
        return data;
    }

    public void write(byte newData)
    {
        data = newData;
    }

    public void add(byte valueToAdd)
    {
        data += valueToAdd;
    }

}
