package sa.edu.sa.librarian.robot.gui;


public class Utility
{

    /**
     * return  a Random integer number
     * @param from
     * @param to
     * @return
     */
    public static int getRandom(int from, int to)
    {
        return from + (int) ((to - from) * Math.random());
    }

    /**
     *  let system wait for a duration
     * @param time
     */
    public static void wait(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException ex)
        {
        }
    }
}
