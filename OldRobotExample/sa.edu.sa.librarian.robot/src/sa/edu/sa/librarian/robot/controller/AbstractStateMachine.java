package sa.edu.sa.librarian.robot.controller;

import lejos.util.Timer;

/*
 * Description : this abstract class will provide timer for the state machine running
 * The controller which need the timer to provide the event should extend this class
 *
 */

abstract class AbstractStateMachine implements lejos.util.TimerListener
{

    /**
     * the function name for overload to invoke other controller
     */
    private Timer timer;

    /**
     * the constructor to start the timer
     * 
     * @param timeInterval
     *            Time Interval
     */
    public AbstractStateMachine(int timeInterval)
    {
        timer = new Timer(timeInterval, this);
    }

    /**
     * start the timer
     */
    public void startTimer()
    {
        timer.start();
    }

    /**
     * stop the timer
     */
    public void stopTimer()
    {
        timer.stop();
    }

    /**
     * reset the time Interval, stop the timer before reseting the timer interval ,
     * call stop Timer function
     * @param timeIterval the timeout period
     */
    public void resetTimerInterval(int timeIterval)
    {
    	stopTimer();
        timer.setDelay(timeIterval);
    }

    /**
     * return the timer interval
     * @return the time out interval
     */
    public int getTimeIntrval()
    {
        return timer.getDelay();
    }

    /**
     * to initialize the state Machine instance
     */
    abstract void init();

    /**
     * force the state go to next State;
     * 
     * @param newState
     */
    abstract void nextState(int newState);
}
