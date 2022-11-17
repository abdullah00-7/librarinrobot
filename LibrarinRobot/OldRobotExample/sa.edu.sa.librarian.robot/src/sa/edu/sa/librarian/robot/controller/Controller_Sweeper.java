package sa.edu.sa.librarian.robot.controller;

import lejos.nxt.Motor;
import sa.edu.sa.librarian.robot.RobotLCD;


public class Controller_Sweeper extends AbstractStateMachine
{
    // the light

    private int stopAngle = 0;
    // the swiper
    private Motor swipper;
    private boolean goingToLeft = true;
    private int standPoint = 0; // 0= center,-1 =left. 1= right
    // state
    private int currentState = -1;
    private int state_Stop = 255;
    private int state_Start = 100;

    /**
     * use check time interval to check the read value
     *
     * @param timeInterval
     */
    public Controller_Sweeper(int timeInterval)
    {
        super(timeInterval);
        this.swipper = NXTRunTime.getSweeperMotor();
    }

    @Override
    public void timedOut()
    {
        if (this.currentState == this.state_Stop)
        {
            return;
        }

        // start to swipe
        if (this.currentState == this.state_Start) // check the stop angel{
        {
            // if the current is on the left, the direct should going to right
            if (this.standPoint == -1)
            {
                this.goingToLeft = false; // should going to the right
                this.swipper.rotate(0 - this.stopAngle);// go to the center
                this.standPoint = 0;// expect it is center
                this.stopAngle = this.swipper.getTachoCount();
                //Utility.DrawStringInLCD("STA:" + this.stopAngle, 5);
                return;
            }
            // when in the center point
            if (this.standPoint == 0)
            {
                // if going to right
                if (!this.goingToLeft)
                {
                    this.swipper.rotate(-45 - this.stopAngle);
                    this.standPoint = 1;
                    this.stopAngle = this.swipper.getTachoCount();
                    //Utility.DrawStringInLCD("STA:" + this.stopAngle, 5);
                    return;
                }
                else
                {
                    this.swipper.rotate(45 - this.stopAngle);
                    this.standPoint = -1;
                    this.stopAngle = this.swipper.getTachoCount();
                    //Utility.DrawStringInLCD("STA:  " + this.stopAngle, 5);
                    return;
                }

            }
            // now the swipe is on the right
            if (this.standPoint == 1)
            {
                this.goingToLeft = true;
                this.swipper.rotate(0 - this.stopAngle);// go to the center
                this.standPoint = 0;// expect it is center
                this.stopAngle = this.swipper.getTachoCount();
                //Utility.DrawStringInLCD("STA:" + this.stopAngle, 5);
                return;
            }
        }

    }

    /**
     * start from center, the will first moving to the left 45
     */
    @Override
    public void init()
    {
        this.swipper = NXTRunTime.getSweeperMotor();

        this.swipper.resetTachoCount();
        this.swipper.setSpeed(100);
        this.swipper.setPower(10);

        this.stopAngle = 0;
        this.swipper.rotate(45);

        this.standPoint = -1;
        this.stopAngle = this.swipper.getTachoCount();
        this.goingToLeft = false;

        this.nextState(this.state_Stop);
    }

    @Override
    void nextState(int newState)
    {
        this.currentState = newState;
    }

    /**
     * return the stop angel
     *
     * @return the current stop angle
     */
    public int getStopAngel()
    {
        return this.stopAngle;
    }

    /**
     * start the moving
     */
    public void startSwiper()
    {
        this.nextState(this.state_Start);
    }

    /**
     * start the moving
     */
    public void stopSwiper()
    {
        this.swipper.stop();
        this.stopAngle = this.swipper.getTachoCount();
        this.nextState(this.state_Stop);
    }

    // immediate stop the swipe
    public void emergencyStop()
    {
        this.nextState(state_Stop);
        this.swipper.stop();
        this.stopAngle = this.swipper.getTachoCount();
        RobotLCD.DrawStringInLCD("Stop Angle: " + this.stopAngle, 1);

    }
}
