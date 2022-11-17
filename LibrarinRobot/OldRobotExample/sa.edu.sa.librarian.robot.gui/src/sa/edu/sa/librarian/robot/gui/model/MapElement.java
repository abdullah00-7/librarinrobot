package sa.edu.sa.librarian.robot.gui.model;

import java.util.ArrayList;
import java.awt.Point;
import java.util.UUID;

/**
 * This class represent each element in the map e.g. zone ,robot position etc.
 */
public class MapElement {

	private String elementUuid; // the UUID
	private String elementType; // the Type
	private String elementName; // the name
	private String elementId; // the Id
	private ArrayList<Point> point; // the point List
	private ArrayList<String> attributeKey;
	private ArrayList<String> attributeValue;
	private int radius; // if type is cycle the radius > 0

	/**
	 * default Constructor
	 */
	public MapElement() {
		elementUuid = UUID.randomUUID().toString();
		elementType = "";
		elementName = "";
		elementId = "";
		point = new ArrayList<Point>();
		attributeKey = new ArrayList<String>();
		attributeValue = new ArrayList<String>();
		radius = -1;
	}

	/**
	 * set up the type of the element
	 *
	 * setElementType
	 * 
	 * @param xElementType void
	 */
	public void setElementType(String xElementType) {
		elementType = xElementType;
	}

	/**
	 * set up the Name of the element
	 *
	 * setElementName
	 * 
	 * @param xElementName void
	 */
	public void setElementName(String xElementName) {
		elementName = xElementName;
	}

	/**
	 * set up the id of the element
	 *
	 * setElementId
	 * 
	 * @param xElementId void
	 */
	public void setElementId(String xElementId) {
		elementId = xElementId;
	}

	/**
	 * set the point
	 * 
	 * @param x
	 * @param y
	 */
	public void setPoint(int x, int y) {
		Point temp = new Point();
		temp.setLocation(x, y);
		point.add(temp);
	}

	/**
	 * setAttributeKey
	 * 
	 * @param xAttributeKey
	 */
	public void setAttributeKey(String xAttributeKey) {
		attributeKey.add(xAttributeKey);
	}

	/**
	 * set up attribute value and add it to an array of attributes
	 *
	 * setAttributeVAlue
	 * 
	 * @param xAttributeValue void
	 */
	public void setAttributeValue(String xAttributeValue) {
		attributeValue.add(xAttributeValue);
	}

	/**
	 * set up radius
	 *
	 * setRadius
	 * 
	 * @param xRadius void
	 */
	public void setRadius(int xRadius) {
		radius = xRadius;
	}

	/**
	 * return the type of element
	 * 
	 * @return type of element
	 */
	public String getElementType() {
		return elementType;
	}

	/**
	 * return the name of element
	 *
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * return the id of element
	 * 
	 * @return id of element
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * return the point that has index
	 * 
	 * @return point that has index
	 */
	public Point getPoint(int index) {
		return point.get(index);
	}

	/**
	 * return the point list
	 * 
	 * @return point list
	 */
	public ArrayList<Point> getPointsArrayList() {
		return point;
	}

	/**
	 * return the key of the of attribute index
	 *
	 * getAttributeKey
	 * 
	 * @param index String
	 */
	public String getAttributeKey(int index) {
		return attributeKey.get(index);
	}

	/**
	 * return the value of the of attribute index
	 *
	 * getAttributeValue
	 * 
	 * @param index String
	 */
	public String getAttributeValue(int index) {
		return attributeValue.get(index);
	}

	/**
	 * return the Radius
	 * 
	 * @return
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * return the number of the points
	 * 
	 * @return
	 */
	public int getPointsCount() {
		return point.size();
	}

	/**
	 * return the UUID of the Map Element
	 * 
	 * @return
	 */
	public String getElementUuid() {
		return elementUuid;
	}

	/**
	 * set the UUID of the Map Element
	 * 
	 * @param uuid
	 */
	public void setElementUuid(String uuid) {
		this.elementUuid = uuid;
	}

	@Override
	public String toString() {
		return "MapElement{" + "elementType=" + elementType + " | elementName=" + elementName + " | elementId="
				+ elementId + " | elementUuid=" + elementUuid + '}';
	}

	/**
	 * Function to return the whole arrayList of attribute keys
	 * 
	 * getAttributeKeys
	 * 
	 * @param ArrayList<String>
	 */
	public ArrayList<String> getAttributeKeys() {
		return attributeKey;
	}

	/**
	 * Function to return the whole arrayList of attribute values
	 * 
	 * getAttributeValues
	 * 
	 * @param ArrayList<String>
	 */
	public ArrayList<String> getAttributeValues() {
		return attributeValue;
	}
}
