package sa.edu.sa.librarian.robot.controller;


import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import sa.edu.sa.librarian.robot.RobotLCD;


public class Controller_Sensor extends AbstractStateMachine
{

    private LightSensor lightSensor = null;
    private int detecedtLightValue;
    private int currentLightValue;
    // state
    private int currentState = 0;
    private int state_start = -2;
    private int state_stop = 2;

    /**
     * use check time interval to check the read value
     * 
     * @param timeInterval
     */
    public Controller_Sensor(int timeInterval)
    {
        super(timeInterval);
    }

    @Override
    public void timedOut()
    {
        if (this.currentState == this.state_start)
        {
            chkLightReading();
        }

        if (this.currentState == this.state_stop)
        {
            return;
        }

    }

    @Override
    public void init()
    {
        this.currentLightValue = Integer.MAX_VALUE;
        this.detecedtLightValue = Integer.MIN_VALUE;
        this.lightSensor = NXTRunTime.getLightSensor();
        this.nextState(this.state_stop);
    }

    @Override
    void nextState(int newState)
    {
        this.currentState = newState;
    }

    //////////////////////////////////////////////
    // input and output  changing the status
    /////////////////////////////////////////////
    /**
     * start the light sensor
     */
    public void startLightSensor()
    {
        this.nextState(this.state_start);
    }

    /**
     * stop the light sensor
     */
    public void stopLightSensor()
    {
        this.nextState(this.state_stop);
    }

    /**
     *  reset Detected Light Value
     */
    public void setLightChkValue(int lightValue)
    {
        this.detecedtLightValue = lightValue;
    }

    /**
     * return the current light reading
     * @return  Integer type of the light value
     */
    public int getLightValue()
    {
        return this.currentLightValue;
    }
    ///////////////////////////////////////////////
    //  functions for check the mine
    //////////////////////////////////////////////

    private void chkLightReading()
    {
        // read the light value
        this.currentLightValue = this.lightSensor.readValue();
        LCD.clear();
        RobotLCD.DrawStringInLCD("C:" + this.currentLightValue + "D: " + this.detecedtLightValue, 0);

        if (this.currentLightValue < this.detecedtLightValue)
        {
            NXTRunTime.getSweeper().emergencyStop();
            NXTRunTime.getMovementController().emergencyStop(-8888);
            // notify the core Controller
            NXTRunTime.getCoreController().FoundObject();
            this.nextState(this.state_stop);
        }

    }
}
