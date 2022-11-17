package sa.edu.sa.librarian.robot;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import sa.edu.sa.librarian.robot.controller.NXTRunTime;

public class RobotMain
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        RobotLCD.AlertAppStart();
        // print the text and waiting sound
        RobotLCD.DrawStringInLCD("LibrarianRobot", 0);
        RobotLCD.DrawStringInLCD("Waiting connection", 1);
        RobotLCD.lightS1(true);
        
        // Create the BlueTooth Connection
        BTConnection btc = null;
        // waiting for the connection
        while (true)
        {
            btc = Bluetooth.waitForConnection();
            if (btc != null)
            {
                Sound.beep();
                RobotLCD.DrawStringInLCD(".", 1, 1);
                break;
            }
        }
        // alter the BT is connected
        RobotLCD.AlertConnected();

        // waiting for the unit packet
        RobotLCD.clearLCD();
        RobotLCD.DrawStringInLCD("Connected Sucessfully", 0);
        RobotLCD.DrawStringInLCD("Initializing robot", 1);

        // the run time is created here
        NXTRunTime.createApplicationRuntime(btc);

        Button.ESCAPE.waitForPressAndRelease();

    }
}
