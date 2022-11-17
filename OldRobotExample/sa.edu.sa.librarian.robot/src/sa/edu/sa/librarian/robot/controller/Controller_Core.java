package sa.edu.sa.librarian.robot.controller;

import sa.edu.sa.librarian.robot.Command;
import sa.edu.sa.librarian.robot.Packet;
import sa.edu.sa.librarian.robot.RobotLCD;

public class Controller_Core
{

    private Packet pkt_rcv = null;
    private boolean isWaitingFoundSomThing = false;

    ////////////////////////////////////////////////////////////////////////
    // The events
    ////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param pkt_rcv  the received packet
     */
    public void setNewPacket(Packet pkt_rcv)
    {
        this.pkt_rcv = pkt_rcv;
        ////////////////////////////////////////////
        //  detecting the mine
        ////////////////////////////////////////////
        // when found sth here  the new packet here should be only the confirm packet
        if (this.isWaitingFoundSomThing)
        {
            int cmd = this.pkt_rcv.getCommand();
            String[] para = this.pkt_rcv.getParameter();
            // when confirm that is a mine
            if (cmd == Command.CONFIRM_YES)
            {
                // confirm yes
                // turn on the red
                RobotLCD.lightS3(false);
                RobotLCD.lightS1(true);

                float mine_x = Float.parseFloat(para[0]);
                float mine_y = Float.parseFloat(para[1]);

                float leave_x = Float.parseFloat(para[2]);
                float leave_y = Float.parseFloat(para[3]);
                // go to th mine point
                NXTRunTime.getNavigator().goTo(mine_x, mine_y);
                // wait 2 second
                RobotLCD.DrawStringInLCD("On the mine", 3, 2);

                NXTRunTime.getNavigator().goTo(leave_x, leave_y);

                this.isWaitingFoundSomThing = false;
                NXTRunTime.getSensor().startLightSensor();
                NXTRunTime.getSweeper().startSwiper();

                RobotLCD.AlertAction();
                this.sendResponse();
                RobotLCD.lightS3(false);
                RobotLCD.lightS1(false);
                RobotLCD.lightS4(true);

            } // when that is the mine
            else if (cmd == Command.CONFIRM_NO)
            {
                //leave the object
                NXTRunTime.getNavigator().travel(5);
                this.isWaitingFoundSomThing = false;
                NXTRunTime.getSensor().startLightSensor();
                NXTRunTime.getSweeper().startSwiper();
                RobotLCD.AlertAction();
                this.sendResponse();
                RobotLCD.lightS3(false);
                RobotLCD.lightS1(false);
                RobotLCD.lightS4(true);
            } // the packet is not conform packet that just ignore
            else
            {
                this.pkt_rcv = null;
                return;
            }
        } //not detecting the mine, sound me move control or others
        else
        {
            this.handlePacket();
        }
    }

    /**
     * Input Function, notify the robot controller  when found  some thing
     */
    public void FoundObject()
    {
        this.isWaitingFoundSomThing = true;
        Packet pkt_foundSth = new Packet(-8, Command.ROBOT_FOUND, NXTRunTime.getRobotStatusWhenStop());
        NXTRunTime.getBTSenderReceiver().sendNewPacket(pkt_foundSth);
        // turn on the red
        RobotLCD.lightS4(false);
        RobotLCD.lightS3(true);
    }

    /////////////////////////////////////////////////////
    // The function of the core controller state machine
    ////////////////////////////////////////////////////
    /**
     * handle the packet 
     */
    private void handlePacket()
    {
        int cmd = this.pkt_rcv.getCommand();
        String[] para = this.pkt_rcv.getParameter();
        //////////////////////////
        /// set Robot configure
        //////////////////////////
        if (cmd == Command.SET_MOVE_POSITION)
        {
            float x = Float.parseFloat(para[0]);
            float y = Float.parseFloat(para[1]);
            float heading = Float.parseFloat(para[2]);
            NXTRunTime.getNavigator().setPosition(x, y, heading);
            this.sendResponse();

            return;
        }

        if (cmd == Command.SET_MOVE_MOVESPEED)
        {
            float speed = Float.parseFloat(para[0]);
            NXTRunTime.getNavigator().setMoveSpeed(speed);
            this.sendResponse();
            return;
        }

        if (cmd == Command.SET_MOVE_TURNSPEED)
        {
            float speed = Float.parseFloat(para[0]);
            NXTRunTime.getNavigator().setTurnSpeed(speed);
            this.sendResponse();
            return;
        }

        ////////////////////////////////
        // The sensor and the swiper
        /////////////////////////////////
        if (cmd == Command.SET_SWIPER_OFF)
        {
            NXTRunTime.getSweeper().stopSwiper();
            this.sendResponse();
            RobotLCD.AlertAction();
            return;
        }

        if (cmd == Command.SET_SWIPER_ON)
        {
            NXTRunTime.getSweeper().startSwiper();
            this.sendResponse();
            return;
        }

        if (cmd == Command.SET_SENSOR_SWIPER_SPEED)
        {
            int speed = Integer.parseInt(para[0]);
            NXTRunTime.getSweeperMotor().setSpeed(speed);
            this.sendResponse();
            return;
        }


        ///////////////////////////
        // for the lines
        ////////////////
        if (cmd == Command.SET_SENSOR_ON)
        {
            NXTRunTime.getSensor().startLightSensor();
            this.sendResponse();
            return;
        }

        if (cmd == Command.SET_SENSOR_OFF)
        {
            NXTRunTime.getSensor().stopLightSensor();
            this.sendResponse();
            return;
        }

        if (cmd == Command.SET_SENSOR_DETECT_LIGHT_VALUE)
        {
            int lightValue = Integer.parseInt(para[0]);
            NXTRunTime.getSensor().setLightChkValue(lightValue);
            this.sendResponse();
            return;
        }

        ////////////////////////////
        // the status listener
        ////////////////////////////
        if (cmd == Command.SET_CHECK_STATUS_ON)
        {
            NXTRunTime.getStatusListener().startStatusListener();
            this.sendResponse();
            return;
        }

        if (cmd == Command.SET_CHECK_STATUS_OFF)
        {
            NXTRunTime.getStatusListener().stoptStatusListener();
            this.sendResponse();
            return;
        }

        // the check 
        if (cmd == Command.SET_CHECK_STATUS_DURATION)
        {
            int period = Integer.valueOf(para[0]);
            NXTRunTime.getStatusListener().stopTimer();
            NXTRunTime.getStatusListener().resetTimerInterval(period);
            NXTRunTime.getStatusListener().startTimer();
            this.sendResponse();
            return;
        }


        if (cmd == Command.DISCONNECT)
        {
            System.exit(0);
            return;
        }
        /////////////////////////////////////////////////////////////////////////////
        // CMD From [200-255] are the movement controller, swift the packet t
        // control the movement of the robot
        /////////////////////////////////////////////////////////////////////////////
        if (this.pkt_rcv.getCommand() >= 200 && this.pkt_rcv.getCommand() <= 255)
        {
            NXTRunTime.getMovementController().setCommand(this.pkt_rcv);
            this.sendResponse();
            return;
        }

    }

    /**
     *  send the response packet to the Host
     */
    private void sendResponse()
    {
        RobotLCD.AlertAction();
        Packet pkt_cmdAck = new Packet(pkt_rcv.getSequenceNumber() + 1, Command.ROBOT_STATUS, NXTRunTime.getRobotStatus());
        NXTRunTime.getBTSenderReceiver().sendNewPacket(pkt_cmdAck);
    }
}
