package sa.edu.sa.librarian.robot.gui.driver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.controller.RobotController;
import sa.edu.sa.librarian.robot.gui.model.*;
import lejos.pc.comm.NXTCommFactory;
import java.util.Arrays;

/**
 * The NxtDriver including connection and sending/receiving data
 *
 */
public class NxtDriver extends TimerTask {
	// the controller of robot

	private RobotController robotController = null;
	// the nxt connection
	private NXTConnectionTabelModel connTableModel = null;
	private NXTConnector conn = null;
	private NXTInfo[] nxts = null;
	// whether the connection is estabilished
	private boolean isRobotConnected = false;
	private boolean isDataOutputInputInitialized = false;
	// the data input and output
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	// the Packet send and rcv
	private Packet pkt_send = null;
	private Packet pkt_rcv = null;
	// the timer and the tick
	private Timer timer = null;
	private int timeout = 10; // for each 10 ms run onece

	/**
	 * use the timer period the initialize the NXT Driver
	 * 
	 * @param timeout
	 */
	public NxtDriver(int timeout) {
		this.timeout = timeout;
		this.timer = new Timer();
	}

	/**
	 * INIT this NxtDriver
	 */
	public void init() {
		// invoke the robot controller
		this.robotController = CoreController.getRobotController();
		// create the connector
		this.search();
		timer.scheduleAtFixedRate(this, 0, timeout);
	}

	/**
	 * in every time out , if there are new packet to sent , just sent , If there
	 * are no new packets to sent,that means the channel is free, so now we can
	 * receive the data
	 */
	@Override
	public void run() {
		// listen to the blue tooth
		if (this.isRobotConnected && this.isDataOutputInputInitialized) {
			this.pkt_rcv = this.receivePacket();

			if (this.pkt_rcv != null && !pkt_rcv.isDiscard) {
				// validated packed received here
				LogDriver.addLog_Robot("NXT Driver Receiving:" + this.pkt_rcv.getDataFrameInString());
				// event to the robot controller
				this.robotController.newRcvPacket(this.pkt_rcv);
			}
		}

	}

	/**
	 * test the connection
	 *
	 * @return true : robot connected, flase :
	 */
	public boolean isRobotConnected() {
		return this.isRobotConnected;
	}

	/**
	 * search the available NXT brick
	 */
	public void search() {
		disConnect();
		// create the connector
		this.conn = new NXTConnector();
		// start the search
		this.nxts = conn.search(null, null, NXTCommFactory.ALL_PROTOCOLS);
		LogDriver.addLog_Robot("Start New Searching, found NXTs: " + this.nxts.length);
		// put information into table
		this.connTableModel = new NXTConnectionTabelModel(this.nxts);
	}

	/**
	 * Connected to the robot
	 * 
	 * @param index selected table model
	 * @return true for robot blue tooth connected
	 */
	public boolean connectRobot(int index) {
		if (this.nxts.length < 1) {
			return false;
		}
		if (index < 0) {
			return false;
		}

		LogDriver.addLog_Robot("NXT Driver :starting connect to " + this.nxts[index].name);
		// connect to the robots
		this.isRobotConnected = this.conn.connectTo(this.nxts[index], NXTComm.LCP);
		LogDriver.addLog_Robot("NXT Driver: Strart connect to robot --> :" + this.isRobotConnected);
		// if connected then start input and out put
		if (this.isRobotConnected) {
			dos = this.conn.getDataOut();
			dis = this.conn.getDataIn();
			this.isDataOutputInputInitialized = true;
			LogDriver.addLog_Robot("NXT Driver: Data input and output is initialized!");
		}

		return this.isRobotConnected;
	}

	/**
	 * DisConnect the robot, try to reset all the parameters
	 */
	public void disConnect() {
		try {
			this.isRobotConnected = false;
			this.isDataOutputInputInitialized = false;
			this.conn.close();
			this.conn = null;
			this.dis.close();
			this.dis = null;
			this.dos.close();
			this.dos = null;
			this.pkt_send = null;
			this.pkt_rcv = null;
		} catch (Exception e) {
			this.isRobotConnected = false;
			this.isDataOutputInputInitialized = false;
			this.conn = null;
			this.dis = null;
			this.dos = null;
			this.pkt_send = null;
			this.pkt_rcv = null;
		}
	}

	/**
	 * return the connection Table Model for showing in the Connection Table
	 *
	 * @return NXTConnectionTabelModel
	 */
	public NXTConnectionTabelModel getConnTableModel() {
		return this.connTableModel;
	}

	/**
	 * give a new packet to the NXT Driver and the NXT Driver will send the Packet
	 * in the next Time out
	 * 
	 * @param pkt_send
	 */
	public void setNewPacketToSend(Packet pkt_send) {
		LogDriver.addLog_Robot("NXT Driver : sending" + pkt_send.getDataFrameInString());
//        this.isNewPacketToSend = true;
//        this.pkt_send = pkt_send;
		this.sendPacket(pkt_send);
		// this.isSending = false;

	}

	/**
	 * Send a packet to the NXT
	 *
	 * @param packet
	 * @throws IOException
	 */
	private void sendPacket(Packet pkt_send) {
		// if that is not a currect packet,return
		if (pkt_send.isDiscard) {
			LogDriver.addLog_Robot_Error("NXT Sending Packet " + " Pkt error");
			return;
		}
		// get the bytes for sending
		byte[] dataToSend = pkt_send.getDataFrameInByte();
		try {
			this.dos.write(dataToSend);
			this.dos.flush();
			String msg = String.format("NXT Send Packet<%s>\n<%s> Completed", pkt_send.getDataFrameInString(),
					Arrays.toString(dataToSend));
			LogDriver.addLog_Robot(msg);
		} catch (IOException ex) {
			LogDriver.addLog_Robot_Error(" connection fail, can not send data to the robot");
			// call disconnect
			this.disConnect();
			return;
		}
	}

	/**
	 * get the receive from the blue tooth channel
	 *
	 * @return
	 * @throws IOException
	 */
	private Packet receivePacket() {
		// if not connected, then return null
		if (!this.isRobotConnected) {
			return null;
		}

		int i = 0;
		byte[] rcv = new byte[254];// the buffer

		// try to read 254 bytes from the input String
		try {
			for (i = 0; i < rcv.length; i++) {
				rcv[i] = this.dis.readByte();
				// break when end with ">0"
				if ((int) rcv[i] == 0 && i > 1) {
					if ((int) rcv[i - 1] == 62) {
						break;
					}
				}
			}
		} catch (Exception e) {
			LogDriver.addLog_Robot_Error(" connection fail, can not send data to the robot");
			// call disconnect
			this.disConnect();
			return null;
		}
		// create the receive packet
		int len = i + 1;
		byte[] pkt_byte = new byte[len];
		System.arraycopy(rcv, 0, pkt_byte, 0, pkt_byte.length);
		Packet pkt_rcv = new Packet(pkt_byte);
		// a log msg
		String msg = String.format("receive Packet<%s>\n", pkt_rcv.getDataFrameInString());
		LogDriver.addLog_Robot(msg);
		// return the pkt_rcv
		return pkt_rcv;
	}
}
