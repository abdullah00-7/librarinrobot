package sa.edu.sa.librarian.robot.gui.model;

import java.awt.Point;
import java.util.ArrayList;

public class RobotElement {

	private String elementType; // ROBOT
	private String direction; // robot
	private Point point;
	private ArrayList<String> attributeKey;
	private ArrayList<String> attributeValue;

	/**
	 * default constructor
	 */
	public RobotElement() {
		point = new Point();
		direction = "0";
		elementType = "ROBOT";
		attributeKey = new ArrayList<String>();
		attributeValue = new ArrayList<String>();
	}

	/**
	 * Method getType returns the Type of the element
	 *
	 * @return String Type of the object element
	 */
	public String getType() {
		return elementType;
	}

	/**
	 * Method getName returns the name of the element
	 *
	 * @return String Name of the object element
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 *
	 * getPoint
	 *
	 * @return Point
	 */
	public Point getPoint() {
		return this.point;
	}

	/**
	 * return key getAttributeKey
	 * 
	 * @param index
	 * @return key getAttributeKey
	 */
	public String getAttributeKey(int index) {
		return this.attributeKey.get(index);
	}

	/**
	 * getAttributeValue
	 * 
	 * @param index
	 * @return AttributeValue
	 */
	public String getAttributeValue(int index) {
		return this.attributeValue.get(index);
	}

	/**
	 * Function to return whole AtrributeKeys List
	 * 
	 * @return whole AtrributeKeys List
	 */
	public ArrayList<String> getAttributeKeys() {
		return this.attributeKey;
	}

	/**
	 * whole AtrributeKeys List
	 * 
	 * @return whole AtrributeKeys List
	 */
	public ArrayList<String> getAttributeValues() {
		return this.attributeValue;
	}

	/**
	 * modify points x,y with new locationsetPoint *
	 * 
	 * @param x
	 * @param y
	 */
	public void setPoint(int x, int y) {
		this.point.setLocation(x, y);
	}

	/**
	 *
	 * Method setName to set the name of element setName
	 *
	 * @param elementDirection
	 */
	public void setDirection(String elementDirection) {
		this.direction = elementDirection;
	}

	/**
	 * Method to set the type of element setType
	 *
	 * @param type
	 */
	public void setType(String type) {
		elementType = type;
	}

	/**
	 * Method to set the key of element if any or "" empty string
	 *
	 * setAttributeKey
	 *
	 * @param key
	 */
	public void setAttributeKey(String key) {
		this.attributeKey.add(key);
	}

	/**
	 * Method to set the value of robot attribute
	 *
	 * setAttributeValue
	 *
	 * @param value
	 */
	public void setAttributeValue(String value) {
		this.attributeValue.add(value);
	}
}
