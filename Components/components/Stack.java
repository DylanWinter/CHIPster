package Components.components;
import java.util.ArrayList;
public class Stack {
    ArrayList<Byte> data;

    public Stack()
    {
        data = new ArrayList<Byte>();
    }

    public void push(Byte value)
    {
        data.add(value);
    }

    public Byte pop()
    {
        if (data.isEmpty())
        {
            return null;
        }
        return data.remove(data.size() - 1);
    }

}
