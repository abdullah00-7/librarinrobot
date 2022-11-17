package sa.edu.sa.librarian.robot.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import lejos.nxt.Battery;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import sa.edu.sa.librarian.robot.Command;
import sa.edu.sa.librarian.robot.Navigator;
import sa.edu.sa.librarian.robot.Pilot;
import sa.edu.sa.librarian.robot.Packet;
import sa.edu.sa.librarian.robot.RobotLCD;


public class NXTRunTime
{
    // the connection variables

    private static BTConnection btc = null;
    private static BufferedInputStream bis = null;
    private static BufferedOutputStream bos = null;
    // the state machines of functionalities
    private static Packet pkt_Init = null;
    private static BTSenderReceiver btSenderReceiver = null;
    private static Motor swiperMotor = Motor.B;
    private static LightSensor lightSensor = new LightSensor(SensorPort.S2);
    // the motor and the sensor and other component
    private static Pilot pilot;
    private static Navigator navigator;
    // the core controller
    private static Controller_Core coreController = null;
    private static Controller_Movement movementController = null;
    private static Controller_Sweeper sweeper = null;
    private static Controller_Sensor sensor = null;
    private static StatusListener statusListener = null;

    /**
     *  Initialize the robot
     * @param btc  the blue tooth connection
     */
    public static void createApplicationRuntime(BTConnection btc)
    {
        // the blue tooth and create the input and output
        NXTRunTime.btc = btc;
        NXTRunTime.bis = new BufferedInputStream(btc.openInputStream(), 254);
        NXTRunTime.bos = new BufferedOutputStream(btc.openOutputStream(), 254);
        // listening to the init packet
        while (true)
        {
            NXTRunTime.pkt_Init = NXTRunTime.receivePacket();
            // exam the packet
            if (NXTRunTime.pkt_Init != null)
            {
                if (!NXTRunTime.pkt_Init.isDiscard)
                {
                    RobotLCD.clearLCD();
                    // the the reply command
                    if (NXTRunTime.pkt_Init.getCommand() == Command.INIT)
                    {
                        break;
                    }
                }
            }
        }

        RobotLCD.clearLCD();
        RobotLCD.DrawStringInLCD("Init:", 1, 1);

        ///////////////////////
        // set the default parameters to the hardware
        ///////////////////////
        pilot = new Pilot(5.6f, 5.6f, 9.5f, Motor.A, Motor.C, false);
        navigator = new Navigator(pilot);

        navigator.setPosition(0, 0, 0);
        navigator.setMoveSpeed(5);
        navigator.setTurnSpeed(40);

        // the swiper's power
        swiperMotor.setPower(5);
        swiperMotor.setSpeed(100);

        // the BT Sender and receiver
        btSenderReceiver = new BTSenderReceiver(5);
        btSenderReceiver.init();
        btSenderReceiver.startTimer();

        // the movement 
        movementController = new Controller_Movement(5);
        movementController.init();
        movementController.startTimer();

        // the core Controller
        coreController = new Controller_Core();
        //coreController.init();
        //coreController.startTimer();

        //swiper.startSwiper();
        // start the sensot
        sensor = new Controller_Sensor(5);
        sensor.init();
        sensor.setLightChkValue(1);
        sensor.startTimer();


        // the swiper of the sensor
        sweeper = new Controller_Sweeper(5);
        sweeper.init();
        sweeper.startTimer();

        // init the status listener
        statusListener = new StatusListener(1000);
        statusListener.init();
        statusListener.startTimer();
        statusListener.startStatusListener();

        // play the sound while robot is OK
        RobotLCD.AlertInitialized();

        // after all the controller stated ,send back the Robot_init
        Packet pkt_InitAck = new Packet(pkt_Init.getSequenceNumber() + 1, Command.ROBOT_INIT, NXTRunTime.getRobotStatus());
        NXTRunTime.btSenderReceiver.sendNewPacket(pkt_InitAck);
        // tun of the yellow
        RobotLCD.lightS1(false);
        // turn on the light 4 green
        RobotLCD.lightS4(true);
    }

    /**
     * return the btConnection
     * 
     * @return BTConnection
     */
    public static BTConnection getBTConnection()
    {
        return NXTRunTime.btc;
    }

    /**
     *
     * @return the BufferedInputStream
     */
    public static BufferedInputStream getBufferedInputStream()
    {
        return NXTRunTime.bis;
    }

    /**
     * return the BufferedOutputStream
     * 
     * @return BufferedOutputStream
     */
    public static BufferedOutputStream getBufferedOutputStream()
    {
        return NXTRunTime.bos;
    }

    /**
     * return the init packet with the init parameters
     * 
     * @return init Packet
     */
    public static Packet getInitPacket()
    {
        return pkt_Init;
    }

    /**
     * return the motor for the sweeper
     * 
     * @return  the motor of the sweeper
     */
    public static Motor getSweeperMotor()
    {
        return swiperMotor;
    }

    /**
     * return the light sensor
     * 
     * @return the light sensor object
     */
    public static LightSensor getLightSensor()
    {
        return NXTRunTime.lightSensor;
    }

    /**
     *  get sensor controller
     * @return the light sensor controller
     */
    public static Controller_Sensor getSensor()
    {
        return sensor;
    }

    /**
     * return BTReceiver
     * 
     * @return BTReceiver
     */
    public static BTSenderReceiver getBTSenderReceiver()
    {
        return NXTRunTime.btSenderReceiver;
    }

    /**
     * get the Navigator
     * 
     * @return navigator
     */
    public static Navigator getNavigator()
    {
        return NXTRunTime.navigator;
    }

    /**
     *   find the movement Controller
     * @return the instance of the movement Controller
     */
    public static Controller_Movement getMovementController()
    {
        return NXTRunTime.movementController;
    }

    /**
     *   find the core Controller
     * @return the instance of the core Controller
     */
    public static Controller_Core getCoreController()
    {
        return NXTRunTime.coreController;
    }

    /**
     *  return sweeper controller
     * @return the instance of the sweeper controller
     */
    public static Controller_Sweeper getSweeper()
    {
        return NXTRunTime.sweeper;
    }

    /**
     * return the status listener
     * @return the status listener controller
     */
    public static StatusListener getStatusListener()
    {
        return statusListener;
    }

    /**
     * return the techoMeter
     * 
     * @return the techoCount
     */
    public static String[] getTechoMeter()
    {
        String[] techoMeter = new String[2];
        techoMeter[0] = String.valueOf(Motor.A.getTachoCount());
        techoMeter[1] = String.valueOf(Motor.C.getTachoCount());
        return techoMeter;
    }

    /**
     * resetTechoMeter
     */
    public static void resetTechoMeter()
    {
        Motor.A.resetTachoCount();
        Motor.C.resetTachoCount();
    }

    // //////////////////////////////////////////////////////////////
    // Sending packet and receive packet from the channel, called by the BT
    // /////////////////////////////////////////////////////////////////
    /**
     * send a packet via blue tooth
     * 
     * @param pkt_toSend
     */
    public static void sendPacket(Packet pkt_toSend)
    {
        if (pkt_toSend.isDiscard)
        {
            return;
        }
        // get the byte and then send via blue tooth
        byte[] data = pkt_toSend.getDataFrameInByte();
        try
        {
            NXTRunTime.bos.write(data, 0, data.length);
            NXTRunTime.bos.flush();
        }
        catch (Exception ex)
        {
            chkSignal();
        }
    }

    /**
     * receive Packet
     * 
     * @return the packet , build he packet by using the the data in chanel
     */
    public static Packet receivePacket()
    {
        int bisLen = -1;
        Packet rcvPkt = null;
        try
        {
            bisLen = NXTRunTime.bis.available();
            byte[] command = new byte[bisLen];
            NXTRunTime.bis.read(command, 0, bisLen);
            rcvPkt = new Packet(command);
        }
        catch (Exception e)
        {
            return null;
        }
        return rcvPkt;
    }

    /**
     * return the Available byte in the channel
     * 
     * @return the number of the bytes in the connection
     */
    public static int BTAvailable()
    {
        int bisLen;
        try
        {
            bisLen = NXTRunTime.bis.available();
        }
        catch (IOException e)
        {
            bisLen = 0;
            chkSignal();
        }
        return bisLen;
    }

    /**
     * checking the signal, if lost connection , then exit
     * if there are no signal , the robot will going back a certain distance and then stop the application
     */
    public static void chkSignal()
    {
        int sig = NXTRunTime.btc.getSignalStrength();
        // when losing connection
        if (sig < 0)
        {
            NXTRunTime.navigator.travel(-5);
            RobotLCD.DrawStringInLCD("losing conntion", 1);
            RobotLCD.AlertLostingConntion();
            lejos.nxt.NXT.exit(-1);
        }
    }

    /**
     *  return the String arrays for sending status packet to the host 
     * @return String[] of all the robot information
     */
    public static String[] getRobotStatus()
    {
        String[] status = new String[12];
        // the robot status
        status[0] = String.valueOf(NXTRunTime.navigator.getX2());
        status[1] = String.valueOf(NXTRunTime.navigator.getY2());
        status[2] = String.valueOf(NXTRunTime.navigator.getHeading2());
        status[3] = String.valueOf(NXTRunTime.pilot.getMoveSpeed());
        status[4] = String.valueOf(NXTRunTime.pilot.getTurnSpeed());
        status[5] = String.valueOf(NXTRunTime.swiperMotor.getSpeed());
        status[6] = String.valueOf(NXTRunTime.sensor.getLightValue());
        status[7] = String.valueOf(NXTRunTime.btc.getSignalStrength());
        status[8] = String.valueOf(Battery.getVoltageMilliVolt());
        status[9] = String.valueOf(Sound.getVolume());
        status[10] = String.valueOf(NXTRunTime.statusListener.getTimeIntrval());
        //status[11] = String.valueOf(NXTRunTime.swiperMotor.getTachoCount());
        return status;
    }

    /**
     *  return the String arrays for sending status packet to the host when robot stop
     * @return String[] of all the robot information
     */
    public static String[] getRobotStatusWhenStop()
    {
        String[] status = new String[12];
        // the robot status
        status[0] = String.valueOf(NXTRunTime.navigator.getX2());
        status[1] = String.valueOf(NXTRunTime.navigator.getY2());
        status[2] = String.valueOf(NXTRunTime.navigator.getHeading2());
        status[3] = String.valueOf(NXTRunTime.pilot.getMoveSpeed());
        status[4] = String.valueOf(NXTRunTime.pilot.getTurnSpeed());
        status[5] = String.valueOf(NXTRunTime.swiperMotor.getSpeed());
        status[6] = String.valueOf(NXTRunTime.sensor.getLightValue());
        status[7] = String.valueOf(NXTRunTime.btc.getSignalStrength());
        status[8] = String.valueOf(Battery.getVoltageMilliVolt());
        status[9] = String.valueOf(Sound.getVolume());
        status[10] = String.valueOf(NXTRunTime.statusListener.getTimeIntrval());
        status[11] = String.valueOf(NXTRunTime.swiperMotor.getTachoCount());
        return status;
    }

    /**
     *  use the packet information to reset robot
     * @param pkt
     */
    public static void resetRobot(Packet pkt)
    {
        String[] para = pkt.getParameter();
        float x = Float.parseFloat(para[0]);
        float y = Float.parseFloat(para[1]);
        float heading = Float.parseFloat(para[2]);
        int moveSpeed = Integer.parseInt(para[3]);
        int turnSpeed = Integer.parseInt(para[4]);

        // for the sensor and swiper
        int swiperSpeed = Integer.parseInt(para[5]);
        int chkLightValue = Integer.parseInt(para[6]);
        // the status listener's value;
        int statusChkDuration = Integer.parseInt(para[7]);

        // set the position of the robot
        NXTRunTime.navigator.setPosition(x, y, heading);
        NXTRunTime.navigator.setMoveSpeed(moveSpeed);
        NXTRunTime.navigator.setTurnSpeed(turnSpeed);

        // set the swiper speed
        NXTRunTime.swiperMotor.setSpeed(swiperSpeed);
        NXTRunTime.getSensor().setLightChkValue(chkLightValue);

        // reset the timer of the chk duration can not be smaller than 200
        if (statusChkDuration < 200)
        {
            statusChkDuration = 200;
        }
        // reset the timer
        NXTRunTime.statusListener.resetTimerInterval(statusChkDuration);
    }
}
