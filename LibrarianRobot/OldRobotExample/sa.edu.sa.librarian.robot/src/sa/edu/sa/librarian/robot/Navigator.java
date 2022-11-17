package sa.edu.sa.librarian.robot;

import lejos.robotics.Pose;
import lejos.geom.Point;

public class Navigator {

	private Pose robotPose = new Pose();
	private float movedDistance = 0;
	private float currnetAngle = 0;
	private boolean _current = false;
	private Pilot pilot;

	/**
	 * Use Green Zone Pilot to start the navigator
	 * 
	 * @param pilot can be any class that implements the pilot interface
	 */
	public Navigator(Pilot pilot) {
		this.pilot = pilot;
		this.robotPose = new Pose();
	}

	/**
	 * go forward
	 */
	public void forward() {
		updatePose();
		_current = false;
		pilot.forward();
	}

	/**
	 * go backward
	 */
	public void backward() {
		updatePose();
		_current = false;
		pilot.backward();
	}

	/**
	 * sets the robot pose to the new coordinates and heading
	 * 
	 * @param x       coordinate
	 * @param y       coordinate
	 * @param heading direction of robot forward movement
	 */
	public void setPosition(float x, float y, float heading) {
		robotPose.setLocation(new Point(x, y));
		robotPose.setHeading(heading);
	}

	/**
	 * sets the robots movement speed - distance units/second
	 * 
	 * @param speed
	 */
	public void setMoveSpeed(float speed) {
		pilot.setMoveSpeed(speed);
	}

	/**
	 * sets the robot turn speed -degrees/second
	 * 
	 * @param speed
	 */
	public void setTurnSpeed(float speed) {
		pilot.setTurnSpeed(speed);
	}

	/**
	 * Stops the robot. Depending on the robot speed, it travels a bit before
	 * actually coming to a complete halt.
	 */
	public void stop() {
		pilot.stop();
		updatePose();
		_current = true;
	}

	/**
	 * returns true if the robot is moving
	 * 
	 * @return true if it is moving
	 */
	public boolean isMoving() {
		return pilot.isMoving();
	}

	/**
	 * Moves the NXT robot a specific distance. A positive value moves it forwards
	 * and a negative value moves it backwards.
	 * 
	 * @param distance The positive or negative distance to move the robot, same
	 *                 units as _wheelDiameter
	 */
	public void travel(float distance) {
		travel(distance, false);
	}

	/**
	 * Moves the NXT robot a specific distance. A positive value moves it forwards
	 * and a negative value moves it backwards.
	 * 
	 * @param distance        The positive or negative distance to move the robot,
	 *                        same units as _wheelDiameter
	 * @param immediateReturn if true, the method returns immediately
	 */
	public void travel(float distance, boolean immediateReturn) {
		updatePose();
		_current = false;
		pilot.travel(distance, immediateReturn);
	}

	/**
	 * Rotates the NXT robot through a specific number of degrees in a direction (+
	 * or -).
	 * 
	 * @param angle Angle to rotate in degrees. A positive value rotates left, a
	 *              negative value right.
	 **/
	public void rotate(float angle) {
		rotate(angle, false);
	}

	/**
	 * Rotates the NXT robot through a specific number of degrees in a direction (+
	 * or -)
	 * 
	 * @param angle           Angle to rotate in degrees. A positive value rotates
	 *                        left, a negative value right.
	 * @param immediateReturn if true, the method returns immediately
	 */
	public void rotate(float angle, boolean immediateReturn) {
		int turnAngle = Math.round(angle);
		updatePose();
		_current = false;
		pilot.rotate(turnAngle, immediateReturn);
	}

	/**
	 * Rotates the NXT robot to point in a specific direction, using the smallest
	 * rotation necessary
	 * 
	 * @param angle The angle to rotate to, in degrees.
	 */
	public void rotateTo(float angle) {
		rotateTo(angle, false);
	}

	/**
	 * Rotates the NXT robot to point in a specific direction relative to the x
	 * axis. It make the smallest rotation necessary . If immediateReturn is true,
	 * method returns immidiately
	 * 
	 * @param angle           The angle to rotate to, in degrees.
	 * @param immediateReturn if true, method returns immediately
	 */
	public void rotateTo(float angle, boolean immediateReturn) {
		float turnAngle = angle - robotPose.getHeading();
		while (turnAngle < -180) {
			turnAngle += 360;
		}
		while (turnAngle > 180) {
			turnAngle -= 360;
		}
		rotate(turnAngle, immediateReturn);
	}

	/**
	 * Robot moves to grid coordinates x,y. First it rotates to the proper direction
	 * then travels in a straight line to that point
	 * 
	 * @param x destination X coordinate
	 * @param y destination Y coordinate
	 */
	public void goTo(float x, float y) {
		goTo(x, y, false);
	}

	/**
	 * Robot moves to grid coordinates x,y. First it rotates to the proper direction
	 * then travels in a straight line to that point
	 * 
	 * @param x               destination X coordinate
	 * @param y               destination Y coordinate
	 * @param immediateReturn come to destination and return
	 */
	public void goTo(float x, float y, boolean immediateReturn) {
		rotateTo(angleTo(x, y));
		travel(distanceTo(x, y), immediateReturn);
	}

	/**
	 * Returns the distance from robot current location to the point with
	 * coordinates x,y
	 * 
	 * @param x coordinate of destination
	 * @param y coordinate of destination
	 * @return the distance
	 */
	public float distanceTo(float x, float y) {
		updatePose();
		_current = !isMoving();
		return robotPose.distanceTo(new Point(x, y));
	}

	/**
	 * Returns the angle from robot current location to the point with coordinates
	 * x,y
	 * 
	 * @param x coordinate of destination
	 * @param y coordinate of destination
	 * @return angle
	 */
	public float angleTo(float x, float y) {
		updatePose();
		_current = !isMoving();
		return robotPose.angleTo(new Point(x, y));
	}

	/**
	 * update pose when stop
	 */
	public void updatePose() {
		if (_current) {
			return;
		}
		float distance = pilot.getTravelDistance() - movedDistance;
		float turnAngle = pilot.getAngle() - currnetAngle;
		double dx = 0;
		double dy = 0;
		double headingRad = (Math.toRadians(robotPose.getHeading()));
		if (Math.abs(turnAngle) > .5) {
			double turnRad = Math.toRadians(turnAngle);
			double radius = distance / turnRad;
			dy = radius * (Math.cos(headingRad) - Math.cos(headingRad + turnRad));
			dx = radius * (Math.sin(headingRad + turnRad) - Math.sin(headingRad));
		} else if (Math.abs(distance) > .01) {
			dx = distance * (float) Math.cos(headingRad);
			dy = distance * (float) Math.sin(headingRad);
		}
		robotPose.translate((float) dx, (float) dy);
		robotPose.rotateUpdate(turnAngle);
		currnetAngle = pilot.getAngle();
		movedDistance = pilot.getTravelDistance();
	}

	/**
	 * update the pose when moving
	 */
	public void updatePose2() {

		float distance = pilot.getTravelDistance() - movedDistance;
		movedDistance = pilot.getTravelDistance();
		float turnAngle = pilot.getAngle() - currnetAngle;

		double dx = 0;
		double dy = 0;
		double headingRad = (Math.toRadians(robotPose.getHeading()));
		if (Math.abs(turnAngle) > .5) {
			double turnRad = Math.toRadians(turnAngle);
			double radius = distance / turnRad;
			dy = radius * (Math.cos(headingRad) - Math.cos(headingRad + turnRad));
			dx = radius * (Math.sin(headingRad + turnRad) - Math.sin(headingRad));
		} else if (Math.abs(distance) > .01) {
			dx = distance * (float) Math.cos(headingRad);
			dy = distance * (float) Math.sin(headingRad);
		}
		robotPose.translate((float) dx, (float) dy);
		robotPose.rotateUpdate(turnAngle);
		currnetAngle = pilot.getAngle();
	}

	/**
	 * gets the current value of the X coordinate
	 * 
	 * @return current x
	 */
	public float getX2() {
		updatePose2();
		return robotPose.getX();
	}

	/**
	 * gets the current value of the Y coordinate
	 * 
	 * @return current Y
	 */
	public float getY2() {
		updatePose2();
		return robotPose.getY();
	}

	/**
	 * gets the current value of the robot heading
	 * 
	 * @return current heading
	 */
	public float getHeading2() {
		updatePose2();
		return robotPose.getHeading();
	}
}
