package sa.edu.sa.librarian.robot.gui.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

import sa.edu.sa.librarian.robot.gui.Utility;
import sa.edu.sa.librarian.robot.gui.driver.LogDriver;
import sa.edu.sa.librarian.robot.gui.driver.NxtDriver;
import sa.edu.sa.librarian.robot.gui.model.Command;
import sa.edu.sa.librarian.robot.gui.model.Packet;
import sa.edu.sa.librarian.robot.gui.model.RobotElement;
import sa.edu.sa.librarian.robot.gui.view.ConnectionWindow;
import sa.edu.sa.librarian.robot.gui.view.RobotControlFrame;

public class RobotController extends Controller {

	private int currentState = -100;
	private int State_OFF = 100; // the robot controller is disabled.
	private int State_ON = 101;
	private int State_ON_BT_CONNECTED = 102; // the blue tooth is connected
	private int State_ON_ROBOT_CONNECTED = 103;// the robot is connected
	private int State_ON_ROBOT_AUTO_RUN = 104;// the robot is connected
	private int State_ON_ROBOT_AUTO_RUNING = 105;// the robot is connected
	// the actions
	private String actionName = null; // the action number
	private String[] actionParameter = null;
	// for the timer
	private int tick = -1;
	// the related view
	private ConnectionWindow connWindow = null;
	private RobotControlFrame ctrWindow = null;
	// Drivers and connection
	private NxtDriver nxtDriver = null;
	private Packet pkc_send = null;
	private Packet pkt_Status = null;
	private RobotElement robotElement;
	// for auto run
	private ArrayList<Point> robotLocations = null;
	private Point nextPoint = null;
	private boolean isAutoRuning = false;

	/*
	 * reset the time out for the waiting start
	 */
	private void resetTimeOut() {
		this.tick = 30000 / (int) this.getPeriod();
	}

	/**
	 * the constructor
	 * 
	 * @param period
	 */
	public RobotController(long period) {
		super(period);
	}

	@Override
	public void init() {
		this.connWindow = CoreController.getRobotConnWin();
		this.ctrWindow = CoreController.getRobotCtrWin();
		this.nxtDriver = CoreController.getNxtDriver();

		// create an instance of robot to update its status
		this.robotElement = new RobotElement();
		this.robotLocations = new ArrayList<Point>();
		this.robotLocations.clear();
		this.startTimer();
	}

	@Override
	public void run() {
		if (this.currentState == this.State_OFF) {
			return;
		}
		///////////////////////////////////////////////////////////////////////
		/// when state is ON , the only thing can do is start the communication
		///////////////////////////////////////////////////////////////////////
		if (this.currentState == this.State_ON) {
			if (this.actionName.equals("StartBlueToothConnection")) {
				// start conn
				boolean isBTConnected = this.nxtDriver.connectRobot(Integer.parseInt(this.actionParameter[0]));
				this.resetTimeOut();
				if (isBTConnected) {
					LogDriver.addLog_Robot("The bluetooth is connected, stating to init the robot");
					// identify the blue tooth is connected
					this.nextState(this.State_ON_BT_CONNECTED);
				} else {
					LogDriver.addLog_Robot_Error("Cannot connect to the robot.");
					this.disConnect();
					CoreController.getRobotConnWin().setVisible(false);
					this.nextState(this.State_OFF);
					return;
				}
			}
		}

		//////////////////////////////////////////////////////////
		// when BT is Connected
		//////////////////////////////////////////////////////////
		if (this.currentState == this.State_ON_BT_CONNECTED) {
			this.tick--;
			CoreController.getRobotConnWin().setTitle("tick: " + this.tick);
			if (this.tick % 100 == 0) {
				// send the init packet
				int seq = Utility.getRandom(0, 1000);
				int cmd = Command.INIT;
				this.pkc_send = new Packet(seq, cmd, null);
				this.nxtDriver.setNewPacketToSend(this.pkc_send);
			}

			if (this.tick == 0) {
				this.disConnect();
				CoreController.getRobotConnWin().setVisible(false);
				this.nextState(this.State_OFF);
				LogDriver.addLog_Robot_Error("Robot can not be init in 15s");
			}
		}

		////////////////////////////////////////////////////////////////
		/// the status is auto running, the timer will start the event
		////////////////////////////////////////////////////////////////
		if (this.currentState == this.State_ON_ROBOT_AUTO_RUN) {
			// send the first destination packet to the robot and then wait
			if (this.robotLocations.size() > 0) {
				// get the top one
				this.nextPoint = this.robotLocations.get(0);

				this.actionParameter = new String[2];
				this.actionParameter[0] = String.valueOf(this.nextPoint.x / 10);
				this.actionParameter[1] = String.valueOf(this.nextPoint.y / 10);

				this.sendCmdtoRobot(Command.GOTO, actionParameter);
				LogDriver.addLog_System("send go to  " + Arrays.toString(this.actionParameter));

				this.nextState(this.State_ON_ROBOT_AUTO_RUNING);

				LogDriver.addLog_System("change status");
			}
			// if no points
			else {
				this.nextState(this.State_ON_ROBOT_CONNECTED);
			}

		}
	}

	/**
	 * change the state Machine to another state
	 * 
	 * @param newState
	 */
	private void nextState(int newState) {
		this.currentState = newState;
	}

	/**
	 * connect to the Robot
	 * 
	 * @param index index from table
	 */
	public void connectToNXT(int index) {
		// give the action name and the parameter, the time tick event will start to
		// connection to the robot
		this.actionName = "StartBlueToothConnection";
		this.actionParameter = new String[1];
		this.actionParameter[0] = String.valueOf(index);
		this.resetTimeOut();
		this.nextState(this.State_ON);
	}

	/**
	 * DisConnect the robot
	 */
	public void disConnect() {
		this.nxtDriver.disConnect();
		this.actionName = null;
		this.actionParameter = null;
		this.ctrWindow.switchLayerOut(1);
		this.nextState(this.State_OFF);
	}

	/**
	 * start auto run of robot in the map
	 */
	public void autoRun() {
		// ask whether to start the auto run
		int value = JOptionPane.showConfirmDialog(CoreController.getMainWindow(), "Start AutoRun?", "AUTO RUN",
				JOptionPane.YES_NO_OPTION);
		if (value == JOptionPane.YES_OPTION) {
			if (this.robotLocations.size() > 1) {
				int confirmValue = JOptionPane.showConfirmDialog(CoreController.getMainWindow(),
						"Continute the last Auto Run?", "AUTO RUN", JOptionPane.YES_NO_OPTION);
				if (confirmValue == JOptionPane.YES_OPTION) {
					this.nextState(this.State_ON_ROBOT_AUTO_RUN);
					return;
				}
				// start the new turn
				else {
				}
			}
		} else {
			return;
		}

		String[] param = this.pkt_Status.getParameter();

		int x = (int) (Float.parseFloat(param[0]) * 10);
		int y = (int) (Float.parseFloat(param[1]) * 10);
		int heading = (int) Float.parseFloat(param[2]);
		LogDriver.addLog_System_Error(
				String.format("x:%d\ty:%d\theadin:%d\n Starting calculating the robot path", x, y, heading));

		this.sendCmdtoRobot(Command.EMERGENCY_STOP, null);
		this.robotLocations = CoreController.getRobotPathPoint(x, y, heading);
		LogDriver.addLog_System_Error(String.format("length of points(%s)", this.robotLocations.size()));
//        this.robotLocations.add(new Point(0, 0));
//        this.robotLocations.add(new Point(500, 0));
//        this.robotLocations.add(new Point(500, 500));
//        this.robotLocations.add(new Point(0, 500));
//        this.robotLocations.add(new Point(0, 0));
		this.nextState(this.State_ON_ROBOT_AUTO_RUN);
	}

	/**
	 * send command to the robot
	 * 
	 * @param cmd
	 * @param parameter
	 */
	public void sendCmdtoRobot(int cmd, String[] parameter) { // create the new packet and send
		this.pkc_send = new Packet(this.pkc_send.getSequenceNumber() + 1, cmd, parameter);
		LogDriver.addLog_Robot("send cmd to robot" + this.pkc_send.getDataFrameInString());
		this.nxtDriver.setNewPacketToSend(pkc_send);
	}

	/**
	 * the in put event , new packet comes
	 * 
	 * @param pkt_rcv the receive packet from blue tooth
	 */
	public void newRcvPacket(Packet pkt_rcv) {
		// this.pkt_rcv = pkt_rcv;
		LogDriver.addLog_Robot("Rcvpkt: " + pkt_rcv.getDataFrameInString());
		///////////////////////////////////////////////////////////////////
		// when the state is BT connected , the only packet is Robot INIT
		//////////////////////////////////////////////////////////////////////////
		if (this.currentState == this.State_ON_BT_CONNECTED) {
			// if the Robot is init
			if (pkt_rcv.getCommand() == Command.ROBOT_INIT) {
				// close the conn window
				this.connWindow.setVisible(false);
				this.ctrWindow.switchLayerOut(2);
				LogDriver.addLog_Robot("Get INIT packet, Robot is connected");
				this.nextState(this.State_ON_ROBOT_CONNECTED);
			}
			// if no the init packet
			else {
				return;
			}
		}

		////////////////////////////////////////////////////
		// when robot is connected handle the packet
		////////////////////////////////////////////////////
		if (this.currentState == this.State_ON_ROBOT_CONNECTED) {
			// update the status
			if (pkt_rcv.getCommand() == Command.ROBOT_STATUS) {
				updateRobotStatus(pkt_rcv);
			}

			// when robot detecting object on manul control
			if (pkt_rcv.getCommand() == Command.ROBOT_FOUND) {
				this.isAutoRuning = false;
				this.handleFoundObject(pkt_rcv);
			}
		}

		////////////////////////////////////////////////////////////////
		// The robot is auto running, the same like in the
		///////////////////////////////////////////////////////////////
		if (this.currentState == this.State_ON_ROBOT_AUTO_RUNING) {
			// update the status
			if (pkt_rcv.getCommand() == Command.ROBOT_STATUS) {
				updateRobotStatus(pkt_rcv);
				LogDriver.addLog_System("do update packet");
			}

			// when robot detecting object
			if (pkt_rcv.getCommand() == Command.ROBOT_FOUND) {
				this.isAutoRuning = true;
				this.handleFoundObject(pkt_rcv);
			}

			// the robot send stop to the host
			if (pkt_rcv.getCommand() == Command.ROBOT_STOP) {
				LogDriver.addLog_System("do stop packet");
				LogDriver.addLog_System("STOP SEQ" + pkt_rcv.getSequenceNumber());

				// the robot has been go to the destination and stop
				if (pkt_rcv.getSequenceNumber() == -9999) {
					LogDriver.addLog_System("The robot go to the point and stop");

					if (this.robotLocations.size() > 0) {
						this.robotLocations.remove(0);
						LogDriver.addLog_System("remove the point point num: " + this.robotLocations.size());

						this.nextState(this.State_ON_ROBOT_AUTO_RUN);
						return;
					} else {
						LogDriver.addLog_System("AUTO mode finish");
						this.nextState(this.State_ON_ROBOT_CONNECTED);
					}
				}
			}
		}

	}

	/**
	 * Update the Robot status
	 * 
	 * @param pkt
	 */
	private void updateRobotStatus(Packet pkt) {
		this.pkt_Status = pkt;
		// get parameters for recieving packet
		String[] para = pkt.getParameter();
		if (para == null) {
			return;
		}

		if (para.length == 0) {
			return;
		}

		// updating robot status based on given parameters
		int x = (int) (Float.parseFloat(para[0]) * 10);
		int y = (int) (Float.parseFloat(para[1]) * 10);
		int heading = (int) Float.parseFloat(para[2]);
		robotElement.setPoint(x, y);
		robotElement.setAttributeKey("");
		robotElement.setAttributeValue("");
		robotElement.setType("robot");
		robotElement.setDirection(String.valueOf(heading));
		CoreController.getMapController().setRobotElement(robotElement);
		CoreController.getRobotCtrWin().updateRobotInfo(para);
	}

	/**
	 * handle found some objects
	 * 
	 * @param pkt
	 */
	private void handleFoundObject(Packet pkt) {
		this.updateRobotStatus(pkt);
		// update the robot position
		// step one -- get the robot paraameter
		String[] para = pkt.getParameter();
		float robot_x = Float.parseFloat(para[0]);
		float robot_y = Float.parseFloat(para[1]);
		float robot_heading = Float.parseFloat(para[2]);
		float robot_stopAngle = Float.parseFloat(para[11]);

		LogDriver.addLog_System_Error("FOUND>>" + " Curr_x =" + robot_x + " Curr_y =" + robot_y + " Curr_headin"
				+ robot_heading + " St angle =" + robot_stopAngle);
		LogDriver.addLog_System_Error("FOUND>>" + pkt.getDataFrameInString());

		// step 2 ,calculate the mine point and the leave point
		String[] points = CoreController.findMinePointAndLeavePoint(robot_x, robot_y, robot_heading, robot_stopAngle);

		LogDriver.addLog_System_Error("Mine and the leave" + Arrays.toString(points));
		// the mine x and y
		float mine_x = Float.parseFloat(points[0]);
		float mine_y = Float.parseFloat(points[1]);

		// draw the temprory mine on screen and save its Uuid value
		String mineUuid = CoreController.getMapController().createElement("mine", "mine",
				String.valueOf(Utility.getRandom(500, 1000)), (int) mine_x * 10, (int) mine_y * 10);

		// pop up a comfirm
		int confirmValue = JOptionPane.showConfirmDialog(CoreController.getMainWindow(),
				"The robot find an Object, is that a mine?", "Object Found", JOptionPane.YES_NO_OPTION);

		// confirm is yes
		if (confirmValue == JOptionPane.YES_OPTION) {
			LogDriver.addLog_Map("detected mine");
			this.sendCmdtoRobot(Command.CONFIRM_YES, points);
			// Refresh the list in mapeditor
			CoreController.getMapEditorWin().fillList();
			if (this.isAutoRuning) {
				this.nextState(this.State_ON_ROBOT_AUTO_RUN);
			} else {
				this.nextState(this.State_ON_ROBOT_CONNECTED);
			}
		}
		// confirm is no
		else {
			CoreController.getMapController().deleteElement(mineUuid);
			String[] leavePoint = new String[2];
			leavePoint[0] = points[2];
			leavePoint[1] = points[3];
			this.sendCmdtoRobot(Command.CONFIRM_NO, leavePoint);

			if (this.isAutoRuning) {
				this.nextState(this.State_ON_ROBOT_AUTO_RUN);
			} else {
				this.nextState(this.State_ON_ROBOT_CONNECTED);
			}
		}
	}
}
