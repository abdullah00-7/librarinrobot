package sa.edu.sa.librarian.robot.controller;

import sa.edu.sa.librarian.robot.Command;
import sa.edu.sa.librarian.robot.Packet;

public class StatusListener extends AbstractStateMachine
{

    private int state_current = -100;
    private final int state_OFF = -100;
    private final int state_ON = 10;

    /**
     * the default constructor
     * @param timeInterval
     */
    public StatusListener(int timeInterval)
    {
        super(timeInterval);
    }

    /**
     *  the event in every timeout
     */
    @Override
    public void timedOut()
    {

        if (this.state_current == this.state_ON)
        {
            this.CheckingRobotStatus();
        }

        if (this.state_current == this.state_OFF)
        {
            // do nothing here
        }
    }

    @Override
    public void nextState(int newState)
    {
        this.state_current = newState;
    }

    @Override
    public void init()
    {
        this.nextState(this.state_OFF);
    }

    /**
     * reset th the duration, should call stop timer first
     * @param period
     */
    public void resetStatusListenerDuration(int period)
    {
        this.resetTimerInterval(period);
    }

    /**
     * start the status listener
     */
    public void startStatusListener()
    {
        this.nextState(this.state_ON);
    }

    /**
     * sop the status listener
     */
    public void stoptStatusListener()
    {
        this.nextState(this.state_OFF);
    }

    /**
     *  checking and display the status of the Robots
     */
    private void CheckingRobotStatus()
    {
        String[] status = NXTRunTime.getRobotStatus();
        /*
         * send the packet to the nxt with the fixed Sequence number -100
         */
        Packet p = new Packet(-100, Command.ROBOT_STATUS, status);
        NXTRunTime.getBTSenderReceiver().sendNewPacket(p);
    }
}
