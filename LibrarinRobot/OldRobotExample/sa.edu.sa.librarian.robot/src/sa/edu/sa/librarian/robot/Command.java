package sa.edu.sa.librarian.robot;


public class Command
{

    ///////////////////////////////////////
    // From 100-199  all are used reset the value of the robot
    //////////////////////////////////////
    /**
     * ask the robot to disconnect
     */
    public static final int DISCONNECT = -100;
    /**
     *   give parameters to reset the robots variables when robot init
     */
    public static final int INIT = -200;
    /**
     * Set the position of the robot
     */
    public static final int SET_MOVE_POSITION = 101;
    /**
     *  set the move speed of the Robot
     */
    public static final int SET_MOVE_MOVESPEED = 102;
    /**
     * set the turn speed of the Robot
     */
    public static final int SET_MOVE_TURNSPEED = 103;
    
    //////////////////////////////////////////////////
    /// control the sensor
    /////////////////////////////////////////////////
    public static final int SET_SENSOR_ON = 110;
    public static final int SET_SENSOR_OFF = 111;
    public static final int SET_SWIPER_ON = 112;
    public static final int SET_SWIPER_OFF = 113;
    public static final int SET_SENSOR_SWIPER_SPEED = 114;
    public static final int SET_SENSOR_DETECT_LIGHT_VALUE = 115;
    
    ////////////////////////////////////////////////////////////////
    ////// variables to handle the status check and duration (period) time
    //////////////////////////////////////////////////////////////
    public static final int SET_CHECK_STATUS_ON = 301;
    public static final int SET_CHECK_STATUS_OFF = 302;
    public static final int SET_CHECK_STATUS_DURATION = 303;
    
    ///////////////////////////////
    // the co nform information from the host to robot
    /////////////////////////////////
    /*
     * confirm that is a mine
     */
    public static final int CONFIRM_YES = 120;
    /**
     * confirm that is not a mine
     */
    public static final int CONFIRM_NO = 121;
    /////////////////////////////////////////////////////////////////////////
    // the command [ 200,255] are  belongs to the robot movement control
    //////////////////////////////////////////////////////////////////////////
    /**
     * ask robot to go forward until stop command
     */
    public static final int FWD = 201;
    /**
     * ask robot to go backward
     */
    public static final int BWD = 202;
    /**
     * ask robot to turn right
     */
    public static final int RTN = 203;
    /**
     * ask robot to turn left
     */
    public static final int LTN = 204;
    /**
     * ask robot to go to a certain distance
     */
    public static final int TRAVAL = 205;
    /**
     * ask the robot to go to the certain x,y and heading
     */
    public static final int GOTO = 206;
    /**
     * ask the robot to reset a pose
     */
    public static final int POSE = 207;
    /**
     * ask the robot to stop
     */
    public static final int STOP = 254;
    public static final int EMERGENCY_STOP = 255;
    
    ///////////////////////////////////////////////////////////
    // This part defines the command from robot to host
    // the nxt to host command is bigger than 800
    /////////////////////////////////////////////////////////
    /**
     * the Found contains 3 parameters x,y, heading
     */
    public static final int ROBOT_FOUND = 800;
    /**
     * the Location contains 3 parameters x,y, heading
     */
    public static final int ROBOT_LOCATION = 801;
    /**
     * the ROBOT_STATUS contains 2 parameters
     * they are , signal strength, the battery level
     */
    public static final int ROBOT_STATUS = 802;
    /**
     * the reply from the Robot to show the robot is fully initialized
     */
    public static final int ROBOT_INIT = 899;
    /**
     *  Robot Stop by GoTo.
     */
    public static final int ROBOT_STOP = 900;
}
