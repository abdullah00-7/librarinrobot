package sa.edu.sa.librarian.robot.gui.controller;

import java.awt.Point;
import java.util.ArrayList;

import sa.edu.sa.librarian.robot.gui.driver.GraphicDriver;
import sa.edu.sa.librarian.robot.gui.driver.LogDriver;
import sa.edu.sa.librarian.robot.gui.driver.NxtDriver;
import sa.edu.sa.librarian.robot.gui.driver.RobotPathDriver;
import sa.edu.sa.librarian.robot.gui.driver.XMLDriver;
import sa.edu.sa.librarian.robot.gui.model.RobotLocation;
import sa.edu.sa.librarian.robot.gui.view.*;

public class CoreController {

	// create controllers
	private static GuiController guiController = new GuiController(500);
	private static MapController mapController = new MapController(1000);
	private static RobotController robotController = new RobotController(10);

	// create display windows
	private static MapWindow mapWindow = new MapWindow();
	private static MainFrame mainFrame = new MainFrame();
	private static ConnectionWindow robotConnWin = new ConnectionWindow();
	private static RobotControlFrame robotCtrWin = new RobotControlFrame();
	private static MapEditorWindow mapEditorWin = new MapEditorWindow();
	private static LogWindow logWindow = new LogWindow();
	private static RobotConfigWindow robotSetting = new RobotConfigWindow(mainFrame, true);

	// Create drivers
	private static NxtDriver nxtDriver = new NxtDriver(10);
	private static XMLDriver xmlDriver = new XMLDriver();
	private static GraphicDriver graphicDriver = new GraphicDriver(mapWindow);

	/**
	 * Method to start the runtime application
	 */
	public static void createRunTime() {

		guiController.init();
		mapController.init();
		robotController.init();

		mapWindow.init();
		robotConnWin.init();
		robotCtrWin.init();
		mapEditorWin.init();
		mainFrame.init();
		LogDriver.addLog_System("Application-View is Initialized successfuly");

		nxtDriver.init();
		LogDriver.addLog_System("Application-Controller is Initialized successfuly");

	}

	/**
	 * return the Main Form Controller
	 * 
	 * @return
	 */
	public static GuiController getMainFormController() {
		return guiController;
	}

	/**
	 * return the view Main window
	 * 
	 * @return instance of main Window
	 */
	public static MainFrame getMainWindow() {
		return mainFrame;
	}

	/**
	 * get instance of map Controller
	 * 
	 * @return instance of map Controller
	 */
	public static MapController getMapController() {
		return mapController;
	}

	/**
	 * get instance of map Editor
	 * 
	 * @return instance of map editor
	 */
	public static MapEditorWindow getMapEditorWin() {
		return mapEditorWin;
	}

	/**
	 * instance of map display window
	 * 
	 * @return instance of map display window
	 */
	public static MapWindow getMapWindow() {
		return mapWindow;
	}

	/**
	 * instance of map log window
	 * 
	 * @return log window
	 */
	public static LogWindow getLogWindow() {
		return logWindow;
	}

	/**
	 * instance of NXTDriver
	 * 
	 * @return instance of NXTDriver
	 */
	public static NxtDriver getNxtDriver() {
		return nxtDriver;
	}

	/**
	 * Robot Connection Window instance
	 * 
	 * @return Robot Connection Window instance
	 */
	public static ConnectionWindow getRobotConnWin() {
		return robotConnWin;
	}

	/**
	 * Robot Controller instance
	 * 
	 * @return Robot Controller instance
	 */
	public static RobotController getRobotController() {
		return robotController;
	}

	/**
	 * Robot Controller Window instance
	 * 
	 * @return Robot Controller Window instance
	 */
	public static RobotControlFrame getRobotCtrWin() {
		return robotCtrWin;
	}

	/**
	 * XML Driver
	 * 
	 * @return XML Driver
	 */
	public static XMLDriver getXmlDriver() {
		return xmlDriver;
	}

	/**
	 * instance of Graphic Driver
	 * 
	 * @return
	 */
	public static GraphicDriver getGraphicDriver() {
		return graphicDriver;
	}

	/**
	 * instance of Robot configuration Driver
	 * 
	 * @return
	 */
	public static RobotConfigWindow getRobotEditSetting() {
		return robotSetting;
	}

	/**
	 * Get the array list of the path for the robot
	 * 
	 * @param x       Initial x position of the robot
	 * @param y       Initial y position of the robot
	 * @param heading the heading of robot
	 * @return The array list of location of the path to the robot
	 */
	public static ArrayList<Point> getRobotPathPoint(int x, int y, int heading) {
		ArrayList<Point> robotPathPoint = new ArrayList<Point>();
		ArrayList<RobotLocation> robotPath = new ArrayList<RobotLocation>();
		Point p = new Point(x, y);

		int myHeading = 0;
		if (heading == 0) {
			myHeading = 90;
		}
		if (heading == -90) {
			myHeading = 0;
		}
		if (heading == 180) {
			myHeading = 270;
		}
		if (heading == 90) {
			myHeading = 180;
		}

		RobotPathDriver rp = new RobotPathDriver(p, myHeading);
		robotPath = rp.getRobotPath();
		for (int i = 0; i < robotPath.size(); i++) {
			// System.out.format("%d: (%d,%d) -> %d\n", i, robotPath.get(i).getPosition().x,
			// robotPath.get(i).getPosition().y, robotPath.get(i).getDirection());
			robotPathPoint.add(new Point(robotPath.get(i).getPosition().x, robotPath.get(i).getPosition().y));
		}
		// System.out.println("final num" + robotPathPoint.size());
		return robotPathPoint;
	}

	/**
	 * find the mine point and the robot leave point
	 * 
	 * @param x
	 * @param y
	 * @param heading
	 * @param stopAngle
	 * @return String[0]= mine.x string[1]= mine.y,String[2]=leave.x
	 *         string[3]=leave.y
	 */
	public static String[] findMinePointAndLeavePoint(double x, double y, double heading, double stopAngle) {
		String[] points = new String[4];

		double mine_x = 0;
		double mine_y = 0;
		double leavePoint_x = -1;
		double leavePoint_y = -1;

		double radius = 5.0; // radius of the mine;
		double swapperLength = 5.0 + radius; // length of the swapper in mm.

		// Assuming
		// heading 0 is this way --> ( robot facing right )
		// heading 90 is \/ ( robot facing down)
		// heading 180 is <-- ( robot facing left)
		// heading -90 is /\ ( robot facing up)

		double headerAngle = heading + stopAngle;

		// robot is in the first cordrant
		if (-90 <= headerAngle && headerAngle <= 0) {
			mine_x = x + swapperLength * Math.cos((headerAngle) * Math.PI / 180); // to convert angles into radian
			mine_y = y + swapperLength * Math.sin((headerAngle) * Math.PI / 180); // to convert angles into radian

			// leave points based on original heading
			leavePoint_x = x + 2 * swapperLength * Math.cos((heading) * Math.PI / 180);
			leavePoint_y = y + 2 * swapperLength * Math.sin((heading) * Math.PI / 180);

		}
		// robot in the 2nd cordrant
		if (0 < headerAngle && headerAngle <= 90) {
			mine_x = x + swapperLength * Math.sin((90 - headerAngle) * Math.PI / 180);
			mine_y = y + swapperLength * Math.cos((90 - headerAngle) * Math.PI / 180);

			// leave points

			leavePoint_x = x + 2 * swapperLength * Math.sin((90 - heading) * Math.PI / 180);
			leavePoint_y = y + 2 * swapperLength * Math.cos((90 - heading) * Math.PI / 180);

		}
		// robot in the 3rd cordrant
		if (90 < headerAngle && headerAngle <= 180) {
			mine_x = x + swapperLength * Math.cos((headerAngle) * Math.PI / 180);
			mine_y = y + swapperLength * Math.sin((headerAngle) * Math.PI / 180);

			// leave points
			leavePoint_x = x + 2 * swapperLength * Math.cos((heading) * Math.PI / 180);
			leavePoint_y = y + 2 * swapperLength * Math.sin((heading) * Math.PI / 180);

		}
		// robot in the 4th cordrant
		if (-180 < headerAngle && headerAngle < -90) {
			mine_x = x + swapperLength * Math.sin((180 - headerAngle) * Math.PI / 180);
			mine_y = y - swapperLength * Math.cos((180 - headerAngle) * Math.PI / 180);

			// leave points

			leavePoint_x = x + 2 * swapperLength * Math.sin((180 - heading) * Math.PI / 180);
			leavePoint_y = y - 2 * swapperLength * Math.cos((180 - heading) * Math.PI / 180);

		}

		// set the value and return
		points[0] = String.valueOf(mine_x);
		points[1] = String.valueOf(mine_y);
		points[2] = String.valueOf(leavePoint_x);
		points[3] = String.valueOf(leavePoint_y);
		return points;
	}
}
