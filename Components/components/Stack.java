package Components.components;
import java.util.ArrayList;
public class Stack {
    ArrayList<Integer> data;

    public Stack()
    {
        data = new ArrayList<Integer>();
    }

    public void push(Integer value)
    {
        data.add(value);
    }

    public Integer pop()
    {
        if (data.isEmpty())
        {
            return null;
        }
        return data.remove(data.size() - 1);
    }

}
