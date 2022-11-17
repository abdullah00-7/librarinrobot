package sa.edu.sa.librarian.robot.gui.model;

import java.awt.Point;

/**
 * This class represents a location to be reached by the robot.
 */
public class RobotLocation {

	private Point position;
	private int direction;

	/**
	 * default constructor
	 * 
	 * @param pos robot position point
	 * @param dir the heading of robot
	 */
	public RobotLocation(Point pos, int dir) {
// 		position = pos;
// 		direction = dir;
// 		if (dir >= 360) direction = 0;
// 		if (dir < 0) direction = 0;

		this.setPosition(pos);
		this.setDirection(dir);
	}

	public RobotLocation(int x, int y, int dir) {
		this.position.setLocation(x, y);
		this.direction = dir;
	}

	/**
	 * return the distance from this point to another point
	 * 
	 * @param desPoint
	 * @return
	 */
	public double distanceto(Point desPoint) {
		return this.position.distance(desPoint);
	}

	/**
	 * Set a new position
	 * 
	 * @param pos A new position
	 */
	public void setPosition(Point pos) {
		this.position = pos;
	}

	/**
	 * Set a new direction, between 0 and 360. Otherwise, set direction to 0.
	 * 
	 * @param dir A new direction
	 */
	public void setDirection(int dir) {
		this.direction = dir;
		if (dir >= 360) {
			this.direction = 0;
		}
		if (dir < 0) {
			this.direction = 0;
		}
	}

	/**
	 * Get the position of the location
	 * 
	 * @return The position
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * Get the direction of the location
	 * 
	 * @return The direction
	 */
	public int getDirection() {
		return this.direction;
	}

	public String[] getPositionParameters() {
		String[] temp = new String[3];
		temp[0] = String.valueOf(this.position.getX() / 10);
		temp[1] = String.valueOf(this.position.getY() / 10);
		return temp;
	}

	@Override
	public String toString() {
		return "RobotLocation{" + "position=" + this.position.toString() + "direction=" + direction + '}';
	}

	/**
	 * return the robot destination for go to function
	 */
	public String getRobotDestination() {
		return String.format("%d,%d", (int) this.position.getX(), (int) this.position.getY());
	}
}
