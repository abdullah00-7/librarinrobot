package sa.edu.sa.librarian.robot.controller;

import sa.edu.sa.librarian.robot.Command;
import sa.edu.sa.librarian.robot.Navigator;
import sa.edu.sa.librarian.robot.Packet;
import sa.edu.sa.librarian.robot.RobotLCD;

public class Controller_Movement extends AbstractStateMachine
{

    private Packet pkt_mov = null; // the command packet
    private Navigator navigator = null; // navigator
    // state machine use
    private int state_current = -255;
    private final int state_OFF = 0;
    private final int state_ON = 1;
    // the stopType
    // type of the robot stop// type =1, found sth stop type =2 the command call if stop, other wise ,the stop type should be zero
    int stopType = -9999;

    /**
     * Constructor
     * 
     * @param timeInterval
     */
    public Controller_Movement(int timeInterval)
    {
        super(timeInterval);
    }

    /**
     *  the time out event
     */
    @Override
    public void timedOut()
    {

        // do nothing when it is off
        if (this.state_current == this.state_OFF)
        {
            return;
        }

        // check the packet content and implement the action
        if (this.state_current == this.state_ON)
        {
            // get the command and the paramters
            String[] p = this.pkt_mov.getParameter();
            int cmd = this.pkt_mov.getCommand();

            switch (cmd)
            {
                default:
                {
                    this.nextState(state_OFF);
                    break;
                }

                case Command.STOP:
                {
                    this.emergencyStop(-7777);
                    return;
                }

                case Command.BWD:
                {
                    this.navigator.backward();
                    this.nextState(state_OFF);
                    break;
                }

                case Command.FWD:
                {
                    this.navigator.forward();
                    this.nextState(state_OFF);
                    break;
                }

                case Command.TRAVAL:
                {
                    this.navigator.travel(Integer.valueOf(p[0]));
                    this.nextState(state_OFF);
                    break;
                }

                case Command.LTN:
                {
                    this.navigator.rotate(-90f);
                    this.nextState(state_OFF);
                    break;
                }

                case Command.RTN:
                {
                    this.navigator.rotate(90f);
                    this.nextState(state_OFF);
                    break;
                }

                case Command.GOTO:
                {
                    RobotLCD.AlertConnected();
                    float x = Float.parseFloat(p[0]);
                    float y = Float.parseFloat(p[1]);
                    RobotLCD.DrawStringInLCD("x:" + x + "y" + y, 2);
                    this.navigator.goTo(x, y);
                    RobotLCD.DrawStringInLCD("Stype: " + this.stopType, 3);
                    // if the robot move there are stop it self , not calls by the command or found object
                    Packet pkt_stop = new Packet(this.stopType, Command.ROBOT_STOP, NXTRunTime.getRobotStatus());
                    NXTRunTime.sendPacket(pkt_stop);
                    this.nextState(state_OFF);
                    break;
                }
            }
        }
    }

    @Override
    public void init()
    {
        navigator = NXTRunTime.getNavigator();
        nextState(this.state_OFF);
    }

    @Override
    void nextState(int newState)
    {
        this.state_current = newState;
    }

    /////////////////////////////////////////////////////
    // The input event is here, when packer come, then change the state,
    // The time out will start the next event
    //////////////////////////////////////////////////////
    /**
     * the event called by the core controller , the input is the packet with movement control information
     * @param pkt_mov the movement control packet
     */
    public void setCommand(Packet pkt_mov)
    {
        // put the default to -9999 means ro bot go to the destianation and stop
        this.stopType = -9999;
        this.pkt_mov = pkt_mov;

        if (pkt_mov.getCommand() == Command.EMERGENCY_STOP)
        {
            this.emergencyStop(-7777);
            this.nextState(state_OFF);
            return;
        }

        this.nextState(state_ON);
    }

    /**
     *  ask to emergency stop
     * @param stopType
     */
    public void emergencyStop(int stopType)
    {
        this.nextState(state_OFF);
        this.stopType = stopType;
        this.navigator.stop();

    }
}
