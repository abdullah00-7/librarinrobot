package sa.edu.sa.librarian.robot.gui.driver;

import java.awt.Point;
import java.util.ArrayList;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.controller.MapController;
import sa.edu.sa.librarian.robot.gui.model.MapElement;
import sa.edu.sa.librarian.robot.gui.model.RobotLocation;

/**
 * This class calculates the Path to be followed by the robot, according the map
 * and its map elements.
 */
public class RobotPathDriver {

	private final int N = 0;
	private final int E = 90;
	private final int S = 180;
	private final int W = 270;
	private final int[] NESE = { N, E, S, E }; // Strategy N-E-S-E
	private final int[] NWSW = { N, W, S, W }; // Strategy N-W-S-W
	private final int[] ENWN = { E, N, W, N }; // Strategy E-N-W-N
	private final int[] ESWS = { E, S, W, S }; // Strategy E-S-W-S
	private final int[] SENE = { S, E, N, E }; // Strategy S-E-N-E
	private final int[] SWNW = { S, W, N, W }; // Strategy S-W-N-W
	private final int[] WNEN = { W, N, E, N }; // Strategy W-N-E-N
	private final int[] WSES = { W, S, E, S }; // Strategy W-S-E-S
	private final int[][] STRATEGIES = { NESE, NWSW, ENWN, ESWS, SENE, SWNW, WNEN, WSES };
	private final int DIST = 50; // Number of pixel to check in every new step
	private final int RWID = 120; // Width of the robot
	// Size of the A1 map
	private final int FINAL_W_A1 = 841;
	private final int FINAL_H_A1 = 594;
	private int[] robotStrategy = new int[4];
	private Point initialPosition = null;
	private int initialDirection;
	private ArrayList<RobotLocation> robotPath = new ArrayList<RobotLocation>();
	private ArrayList<MapElement> me = null;
	private ArrayList<MapElement> noGoZoneMapElements = new ArrayList<MapElement>();
	private GraphicDriver graphicDriver = null;
	private MapController mapController = null;

	/**
	 * Constructor of the class
	 */
	public RobotPathDriver(Point p, int dir) {
		this.initialPosition = p;
		this.initialDirection = dir;
		this.graphicDriver = CoreController.getGraphicDriver();
		this.mapController = CoreController.getMapController();
	}

	public RobotPathDriver(int x, int y, int dir) {
		this.initialPosition = new Point(x, y);
		this.initialDirection = dir;
		this.graphicDriver = CoreController.getGraphicDriver();
		this.mapController = CoreController.getMapController();
	}

	/**
	 * This method returns a calculated robot path on the map. If no step can be set
	 * on the path, returns an empty array.
	 * 
	 * @return The path for the robot on the map.
	 */
	public ArrayList<RobotLocation> getRobotPath() {
		// Call to calculate the path
		this.setRobotPath();

		// Return the path
		return this.robotPath;
	}

	/**
	 * This method set the path to be followed by the robot. First define the
	 * strategy according initial position and direction The follow the strategy to
	 * determine the possible next location of the robot. Every new location is
	 * store in the robotPath global array.
	 */
	public void setRobotPath() {
		RobotLocation newLoc = null;
		boolean findNewLoc = true;
		Point currPos;
		int tempDir;
		int currentStrategy = 0;
		int localDist = DIST;
		boolean step = true; // Step to traverse. TRUE: unlimited (until find border or another element on
								// the map). FALSE: One step
		boolean secondStep = false;
		boolean changeDir = false;

		// Set the current position from the initial position
		currPos = new Point(initialPosition.x, initialPosition.y);

//        System.out.printf("Current Pos: (%d,%d) -> %d\n", initialPosition.x, initialPosition.y, initialDirection);

		// Set the current NoGoZone maps elements.
		// Get the currentMap elements
		me = mapController.getMapElement();
		// If a element is a NoGoZone or a mine, add it to the NoGoZoneMapElement
		// ArrayList
		for (int i = 0; i < me.size(); i++) {
			// Add them, only if they are a noGoZone or a Mine
			if (me.get(i).getElementName().equals("noGoZone") || me.get(i).getElementName().equals("mine")) {
				noGoZoneMapElements.add(me.get(i));
			}
		}

//        System.out.printf("Calling selectStrategy\n");

		// First select the strategy to search the map. If a strategy can be defined
		// continue. Otherwise, cannot define a path
		// The strategy is stored at the robot strategy list.
		if (this.selectStrategy()) {
			// Add the initial location to the path
			newLoc = new RobotLocation(new Point(currPos.x, currPos.y), initialDirection);
			robotPath.add(newLoc);

// 			System.out.printf("Initial: (%d,%d) -> %d\n", currPos.x, currPos.y, initialDirection);

			// Find and store the followinf locations, until no new location can be
			// calculated
			while (findNewLoc) {
				// Calculate a possible new possition, according to the current location and the
				// strategy
				tempDir = robotStrategy[currentStrategy];

//				System.out.printf("CurPosition: (%d,%d) => Checking: %d ->", currPos.x, currPos.y, tempDir);

				// Determine if the new step is allowed. In this case, determine if continue in
				// the same direction and set the current position
				// Otherwise, means that a top has been reached which implies a new location
				// must be stored in the map.
				if (this.checkNewPosition(currPos, tempDir)) {
					if (step) {
						changeDir = false;
						localDist = DIST;
					} // Keep searching in the same direction
					if (!step) {
						changeDir = true;
						localDist = 2 * DIST;
					} // Must change the direction

					// Control the end of the searching
					secondStep = false;

					// Set the current position, according the current location and the direction of
					// the strategy
					tempDir = robotStrategy[currentStrategy];
					switch (tempDir) {
					case N:
						currPos.setLocation(currPos.x, currPos.y - localDist);
						break;
					case E:
						currPos.setLocation(currPos.x + localDist, currPos.y);
						break;
					case S:
						currPos.setLocation(currPos.x, currPos.y + localDist);
						break;
					case W:
						currPos.setLocation(currPos.x - localDist, currPos.y);
						break;
					default:
						findNewLoc = false; // There is no valid direction as input.
					}

//					System.out.printf("TRUE\n");
				} else {
//					System.out.printf("FALSE\n");
					changeDir = true; // There is no possible new location in the same direction. Must change to a new
										// direction
				}

				// If a change must be perform, save the location and set the new conditions
				if (changeDir) {
					// If it the first time in a row to change the direction, means a new location
					// and store it.
					if (!secondStep) {
						// set the new conditions
						step = !step;
						currentStrategy++;
						if (currentStrategy > 3) {
							currentStrategy = 0;
						}
						secondStep = true;

						// Add the new location
						newLoc = new RobotLocation(new Point(currPos.x, currPos.y), robotStrategy[currentStrategy]);
						robotPath.add(newLoc);

// 						System.out.printf("Adding: (%d,%d) - %d\n", newLoc.getPosition().x, newLoc.getPosition().y, newLoc.getDirection());
					} else // The second time in a row with a FALSE new position. Means no further step can
							// be reached. Then finish the searching
					{
						findNewLoc = false;
//                        System.out.printf("No futher location is calculated\n");
					}
				}
			}
		}
	}

	/**
	 * This method defines the strategy to move the Robot through the map. A
	 * strategy is selected considering the first change of the direction. The
	 * strategy is seleted from the list of possible strategies. The selected
	 * strategy is set to the robotStrategy list. Return a TRUE is a strategy was
	 * correctly selected, FALSE otherwise.
	 * 
	 * @return boolean TRUE noting a correct election of a strategy. FALSE
	 *         otherwise.
	 */
	public boolean selectStrategy() {
		boolean strategy = false;
		boolean front = false;
		boolean left = false;
		boolean right = false;
		Point tempPos;
//        Point currPos;
		int tempDir;
		int newDir = 0;

//        currPos = new Point(initialPosition.x, initialPosition.y);
		tempPos = new Point(initialPosition.x, initialPosition.y);
		tempDir = initialDirection;

		// While the robot can move forward in the same initial direction, try to move a
		// step beyond the current position.
		while (this.checkNewPosition(tempPos, tempDir)) {
//            //Set the current point.
//            currPos.x = tempPos.x;
//            currPos.y = tempPos.y;

			// If can move a new step, then set the new position and try again
			switch (tempDir) {
			case N:
				tempPos.setLocation(tempPos.x, tempPos.y - DIST);
				break;
			case E:
				tempPos.setLocation(tempPos.x + DIST, tempPos.y);
				break;
			case S:
				tempPos.setLocation(tempPos.x, tempPos.y + DIST);
				break;
			case W:
				tempPos.setLocation(tempPos.x - DIST, tempPos.y);
				break;
			default:
				return false; // There is no valid direction as input.
			}

			front = true;
//            System.out.printf("(%d,%d)\n", tempPos.x, tempPos.y);
		}

//        System.out.printf("FRONT: %b\n", front);

		// When cannot move a step beyond, check left or right new step, to set the
		// strategy

		// move a step behind
//        tempPos.x = currPos.x;
//        tempPos.y = currPos.y;

		// If Front step, try to go LEFT
		if (front) {
			if (tempDir < 90) {
				newDir = 270 + tempDir;
			} else {
				newDir = tempDir - 90;
			}
//            System.out.printf("ANGLE L %d: \n", newDir);
			if (checkNewPosition(tempPos, newDir)) {
				left = true;
			}
		}

//        System.out.printf("LEFT: %b\n", left);

		// If Front step, and not LEFT, then try RIGHT
		if (front && !left) {
			newDir = tempDir + 90;
			if (newDir == 360) {
				newDir = 0;
			}
//            System.out.printf("ANGLE R %d: \n", newDir);
			if (checkNewPosition(tempPos, newDir)) {
				right = true;
			}
		}

//        System.out.printf("RIGHT: %b\n", right);

		// If LEFT or RIGHT mean that a possible strategy can be used, then select it.
		// Otherwise, return with a FALSE
		if (left || right) {
//            System.out.printf("CHECK STRATEGIES\n");
			// Traverse the list of possible strategies
			for (int i = 0; i < STRATEGIES.length; i++) {
				// System.out.printf("%d: %d - %d\n", i, STRATEGIES[i][0], tempDir);
				if (STRATEGIES[i][0] == tempDir && STRATEGIES[i][1] == newDir) {
					// System.out.printf(" %d: %d - %d\n", 1, STRATEGIES[i][1], newDir);
					// Set the strategy
// 					System.out.printf("STRATEGY: ");
					for (int j = 0; j < STRATEGIES[i].length; j++) {
						robotStrategy[j] = STRATEGIES[i][j];
// 						System.out.printf("%d - ", robotStrategy[j]);
					}
					// Set the output of the method as true due a Strategy has been selected
					strategy = true;
// 					System.out.printf("\n");
				}
			}
		} else {
			strategy = false;
		}

		return strategy;
	}

	/**
	 * This method check if a new next position for the robot, given a current
	 * position and a direction (current or desirable), is valid. The new position
	 * is calculated by looking a next step at DIST distance from the current
	 * position. Checks: 1. The next new position is within the map 2. The next new
	 * position is valid considering the wheels of the robot outside a no go zone 3.
	 * The next new position is not in a no go zone (considering the sensor of the
	 * robot)
	 * 
	 * @param currentPoint The current position of the robot, to calculate a next
	 *                     position.
	 * @param currentDir   The current direction of the robot (or a desirable
	 *                     direction), to calculate a next position.
	 * @return TRUE is a next position is reachable. FALSE otherwise.
	 */
	public boolean checkNewPosition(Point currentPoint, int currentDir) {
		boolean newStep = true;
		int dSensorWheel = 100;
		int halfWidth = RWID / 2;
		int xPos, yPos;
		int xWheelLetf = 0;
		int yWheelLetf = 0;
		int xWheelRight = 0;
		int yWheelRight = 0;

		xPos = currentPoint.x;
		yPos = currentPoint.y;

//        System.out.printf("Calling checkNewPosition with (%d,%d)-%d\n", xPos, yPos, currentDir);

		// Define the possible new position, according the current point and direction.
		// Check DIST distance ahead.
		switch (currentDir) {
		case N:
			yPos -= DIST;
			xWheelLetf = xPos - halfWidth;
			yWheelLetf = yPos + dSensorWheel;
			xWheelRight = xPos + halfWidth;
			yWheelRight = yPos + dSensorWheel;
			break;
		case E:
			xPos += DIST;
			xWheelLetf = xPos - dSensorWheel;
			yWheelLetf = yPos - halfWidth;
			xWheelRight = xPos - dSensorWheel;
			yWheelRight = yPos + halfWidth;
			break;
		case S:
			yPos += DIST;
			xWheelLetf = xPos + halfWidth;
			yWheelLetf = yPos - dSensorWheel;
			xWheelRight = xPos - halfWidth;
			yWheelRight = yPos - dSensorWheel;
			break;
		case W:
			xPos -= DIST;
			xWheelLetf = xPos + dSensorWheel;
			yWheelLetf = yPos + halfWidth;
			xWheelRight = xPos + dSensorWheel;
			yWheelRight = yPos - halfWidth;
			break;
		default:
			// System.out.printf("INVALID ANGLE..!\n");
			newStep = false; // There is no valid direction as input.
		}

//        System.out.printf("Checking point (%d,%d)-%d\n", xPos, yPos, currentDir);

		// Check is if possible go to the calculated new position
		if (newStep) {
			// 1. Check if the new point is within the map. If not, the cannot go to that
			// position and return false
			if (xPos < 0 + 50 || xPos > FINAL_W_A1 - 50) {
//                System.out.printf("x point not in the map\n");
				newStep = false;
			}
			if (yPos < 0 + 50 || yPos > FINAL_H_A1 - 50) {
//                System.out.printf("y point not in the map\n");
				newStep = false;
			}

			// 2. Check if the wheels are going to a forbidden place. If they touch a
			// forbidden place, return false
//            System.out.printf("\tSensor: (%d,%d) L: (%d,%d) R: (%d,%d)\n", xPos, yPos, xWheelLetf, yWheelLetf, xWheelRight, yWheelRight);
			/*
			 * if (graphicDriver.isPointInNoGoArea(noGoZoneMapElements, new
			 * Point(xWheelLetf, yWheelLetf))) { newStep = false;
			 * System.out.printf("\tLeft Wheel in NGZ (%d,%d)\n", xWheelLetf, yWheelLetf); }
			 * 
			 * if (graphicDriver.isPointInNoGoArea(noGoZoneMapElements, new
			 * Point(xWheelRight, yWheelRight))) { newStep = false; //
			 * System.out.printf("\tRight Wheel in NGZ (%d,%d)\n", xWheelRight,
			 * yWheelRight); }
			 */
			// 3. Check if the new point is inside a No Go Zone. If it is the case, return
			// false
			if (graphicDriver.isPointInNoGoArea(noGoZoneMapElements, new Point(xPos, yPos))) {
				newStep = false;
//                System.out.printf("\tSensor in NGZ (%d,%d)\n", xPos, yPos);
			}
		}

		return newStep;
	}
}
