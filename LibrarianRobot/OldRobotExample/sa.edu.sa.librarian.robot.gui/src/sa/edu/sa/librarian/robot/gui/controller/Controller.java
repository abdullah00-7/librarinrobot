package sa.edu.sa.librarian.robot.gui.controller;

import java.util.Timer;
import java.util.TimerTask;

abstract class Controller extends TimerTask
{
    protected Timer timer;
    protected long period;

    public Controller(long period)
    {
        this.timer = new Timer();
        this.period = period;
    }

    /**
     * return the timeout interval of this controller
     * @return the time out period
     */
    public long getPeriod()
    {
        return this.period;
    }

    /**
     * start the timer at the fix rate
     */
    public void startTimer()
    {
        this.timer.scheduleAtFixedRate(this, 0, this.period);
    }

    /**
     * start Timer at the fix rate after the delay
     */
    public void startTimer(long delay)
    {
        this.timer.scheduleAtFixedRate(this, delay, this.period);
    }

    public void stopTimer()
    {
        this.timer.cancel();
    }

    /**
     * Initialize the class
     */
    public abstract void init();
}
