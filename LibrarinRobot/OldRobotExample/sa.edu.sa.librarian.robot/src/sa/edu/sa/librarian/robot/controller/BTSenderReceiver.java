package sa.edu.sa.librarian.robot.controller;

import sa.edu.sa.librarian.robot.Packet;

public class BTSenderReceiver extends AbstractStateMachine
{

    private int state_stop = -1;
    private int state_start = 1;
    private int currentState = 0;
    // for sending packet
    private boolean isPacketToSend;
    private Packet pkt_Send;
    private Packet pkt_rcv;

    /**
     * the default constructor
     * 
     * @param timeInterval
     */
    public BTSenderReceiver(int timeInterval)
    {
        super(timeInterval);
    }

    /**
     *  the event in every time out
     */
    @Override
    public void timedOut()
    {

        if (currentState == state_start)
        {
            // if there is packet to send
            if (this.isPacketToSend)
            {
                NXTRunTime.sendPacket(this.pkt_Send);
                this.isPacketToSend = false;
            }
            // receive the packet
            receivePacket();
        }

        // in stop status, do nothing
        if (currentState == state_stop)
        {
        	return;
        }
    }

    @Override
    public void nextState(int newState)
    {
        this.currentState = newState;
    }

    ////////////////////////////////////
    // the event
    ////////////////////////////////////
    /**
     * Initialize the sender and receiver, let the state machine start
     */
    @Override
    public void init()
    {
        this.isPacketToSend = false;
        this.pkt_Send = null;
        this.pkt_rcv = null;
        nextState(state_start);
    }

    /**
     * set the packet send, send packet in the next time out
     *
     * @param pkt_send
     */
    public void sendNewPacket(Packet pkt_send)
    {
        // put packet in the buffer
        this.pkt_Send = pkt_send;
        this.isPacketToSend = true;
        this.nextState(this.state_start);
    }

    /**
     * check and receive the Packet put them in to core controller
     */
    private void receivePacket()
    {
        int bisLen = NXTRunTime.BTAvailable();
        // read the packet from the blue tooth
        if (bisLen > 0)
        {
            this.pkt_rcv = NXTRunTime.receivePacket();
            if (!pkt_rcv.isDiscard)
            { // set the New Packet to the Controller
                NXTRunTime.getCoreController().setNewPacket(this.pkt_rcv);
            }
        }
    }
}
