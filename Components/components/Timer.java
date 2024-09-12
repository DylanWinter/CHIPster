package Components.components;

public class Timer {
    int timer;

    public Timer()
    {
        timer = 0;
    }

    public void setTimer(int time)
    {
        this.timer = time;
    }

    public int getTimer()
    {
        return timer;
    }

    public void decrement()
    {
        if (timer > 0)
        {
            this.timer -= 1;
        }
    }
}
