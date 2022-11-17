package sa.edu.sa.librarian.robot;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;

public class RobotLCD
{

    /**
     *  Clear the Screen
     */
    public static void clearLCD()
    {
        LCD.clear();
        LCD.clearDisplay();
    }

    /**
     * Draw String on the LCD
     * 
     * @param content
     *            the content to display on the Screen
     * @param line
     *            line (0-6)
     */
    public static void DrawStringInLCD(String content, int line)
    {
        LCD.drawString(content, 0, line);
        LCD.refresh();
    }

    /**
     * Draw String on LCD with a duration d,block the thread
     * 
     * @param content the content on the Screen
     * @param line the line of the screen (0-6)
     * @param duration
     *            how many second for the blocking time out 
     */
    public static void DrawStringInLCD(String content, int line, int duration)
    {
        LCD.drawString(content, 0, line);
        LCD.refresh();
        try
        {
            Thread.sleep(1000 * duration);
        }
        catch (InterruptedException e)
        {
        }
    }

    /**
     * Playing the Sound when robot start
     */
    public static void AlertAppStart()
    {
        Sound.beepSequenceUp();
        Sound.beepSequenceUp();
        lightS1(true);
    }

    /**
     * Playing the Sound when robot losing connection
     */
    public static void AlertLostingConntion()
    {
        Sound.beepSequence();
        Sound.beepSequence();
        Sound.beepSequence();
        Sound.beepSequence();
        Sound.beepSequence();
        lightS1(true);
        lightS3(true);
        lightS4(true);
    }

    /**
     * play the Sound while BT Connection built
     */
    public static void AlertConnected()
    {
        Sound.beepSequenceUp();
        Sound.beepSequence();
        Sound.beepSequenceUp();
        lightS1(false);
    }

    /**
     * alter when object Found
     */
    public static void AlertFoundObject()
    {
        Sound.twoBeeps();
        lightS1(true);
    }

    /**
     * play sound when received initialize packet
     */
    public static void AlertInitialized()
    {
        lightS1(true);
        Sound.twoBeeps();
        lightS4(true);
    }

    /**
     * play a sound when finish some action
     */
    public static void AlertAction()
    {
        lightS4(false);
        Sound.beep();
        lightS4(true);
    }

    /**
     * active and de-active the light on port 4
     * @param lightOn true for turn on , false for turn off the light on sensor 3 of NXT
     */
    public static void lightS4(boolean lightOn)
    {
        if (lightOn)
        {
            SensorPort.S4.activate();
        }
        else
        {
            SensorPort.S4.passivate();
        }
    }

    /**
     * active and de-active the light on port 3
     * @param lightOn true for turn on , false for turn off the light on sensor 3 of NXT
     */
    public static void lightS3(boolean lightOn)
    {
        if (lightOn)
        {
            SensorPort.S4.activate();
        }
        else
        {
            SensorPort.S4.passivate();
        }
    }

    /**
     *  active and de-active the light on port 1
     * @param lightOn true for turn on , false for turn off the light on sensor 3 of NXT
     */
    public static void lightS1(boolean lightOn)
    {
        if (lightOn)
        {
            SensorPort.S1.activate();
        }
        else
        {
            SensorPort.S1.passivate();
        }
    }
}
