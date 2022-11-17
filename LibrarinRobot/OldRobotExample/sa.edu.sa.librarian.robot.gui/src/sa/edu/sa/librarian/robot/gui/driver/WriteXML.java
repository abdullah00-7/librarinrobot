package sa.edu.sa.librarian.robot.gui.driver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import sa.edu.sa.librarian.robot.gui.model.*;

public class WriteXML {

	/**
	 * Write XML
	 * 
	 * @param tarXmlFilePath file path
	 * @param mapObjects     mapObjects
	 * @param robotElement   robotElement
	 * @return true for successfully written
	 * @throws IOException
	 */
	public boolean writeToXML(String tarXmlFilePath, ArrayList<MapElement> mapObjects, RobotElement robotElement)
			throws IOException {
		if (mapObjects == null)
			return false;
		Stack<Integer> stackOfIndices = new Stack<Integer>();
		int index;
		int NumberOfPoints;
		Element mainTag = null;
		Element temp = null;
		Document doc = DocumentHelper.createDocument();

		// main tag (map starting line)
		doc.setXMLEncoding("UTF-8");

		index = selectElementByType(mapObjects, "minefield-map");

		if (index != -1) {
			mainTag = doc.addElement(mapObjects.get(index).getElementType());
			addAttributes(mainTag, mapObjects.get(index));
		} else {
			mainTag = doc.addElement("minefield-map");
		}

		// start of zones
		// zone: safeZone
		countOfObjectsByName(mapObjects, stackOfIndices, "safeZone");

		while (!stackOfIndices.empty()) {
			index = stackOfIndices.pop();
			temp = mainTag.addElement("zone");

			if (mapObjects.get(index).getElementName() != null && mapObjects.get(index).getElementName().length() > 0) {
				temp.addAttribute("name", mapObjects.get(index).getElementName());
			}
			if (mapObjects.get(index).getElementId() != null && mapObjects.get(index).getElementId().length() > 0) {
				temp.addAttribute("id", mapObjects.get(index).getElementId());
			}

			addAttributes(temp, mapObjects.get(index)); // 1
			NumberOfPoints = mapObjects.get(index).getPointsCount();

			if (NumberOfPoints > 0) {
				addPoints(temp, mapObjects.get(index), NumberOfPoints);
			}
		}

		// zone: searchingZone
		countOfObjectsByName(mapObjects, stackOfIndices, "searchingZone");

		while (!stackOfIndices.empty()) {
			index = stackOfIndices.pop();
			temp = mainTag.addElement("zone");

			if (mapObjects.get(index).getElementName() != null && mapObjects.get(index).getElementName().length() > 0) {
				temp.addAttribute("name", mapObjects.get(index).getElementName());
			}
			if (mapObjects.get(index).getElementId() != null && mapObjects.get(index).getElementId().length() > 0) {
				temp.addAttribute("id", mapObjects.get(index).getElementId());
			}

			addAttributes(temp, mapObjects.get(index)); // 2
			NumberOfPoints = mapObjects.get(index).getPointsCount();

			if (NumberOfPoints > 0) {
				addPoints(temp, mapObjects.get(index), NumberOfPoints);
			}
		}

		// zone: noGoZone
		countOfObjectsByName(mapObjects, stackOfIndices, "noGoZone");

		while (!stackOfIndices.empty()) {
			index = stackOfIndices.pop();
			temp = mainTag.addElement("zone");

			if (mapObjects.get(index).getElementName() != null && mapObjects.get(index).getElementName().length() > 0) {
				temp.addAttribute("name", mapObjects.get(index).getElementName());
			}
			if (mapObjects.get(index).getElementId() != null && mapObjects.get(index).getElementId().length() > 0) {
				temp.addAttribute("id", mapObjects.get(index).getElementId());
			}

			addAttributes(temp, mapObjects.get(index)); // 3
			NumberOfPoints = mapObjects.get(index).getPointsCount();

			if (NumberOfPoints > 0) {
				addPoints(temp, mapObjects.get(index), NumberOfPoints);
			}
		}

		// robot
		if (robotElement != null) {
			CopyRobotElementToMapElement(mapObjects, robotElement);
			index = selectElementByType(mapObjects, "robot");

			if (index != -1) {
				Element robotTag = mainTag.addElement("robot");
				if (robotElement.getDirection().length() > 0) {
					robotTag.addAttribute("direction", robotElement.getDirection());
				}
				addAttributes(robotTag, mapObjects.get(index));
				NumberOfPoints = mapObjects.get(index).getPointsCount();

				if (NumberOfPoints > 0) {
					addPoints(robotTag, mapObjects.get(index), NumberOfPoints);
				}
			}
		}

		// beacons
		addBeacon(mainTag, stackOfIndices, mapObjects);

		// mines
		addMines(mainTag, stackOfIndices, mapObjects);

		try {
			File f = new File(tarXmlFilePath);
			XMLWriter output;
			OutputFormat format = OutputFormat.createPrettyPrint();
			output = new XMLWriter(new FileWriter(f), format);
			output.write(doc);
			output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * copy the element robot and add it to mapElements
	 *
	 * CopyRobotElementToMapElement
	 *
	 * @param mapObjects , robotElement
	 *
	 *                   void
	 */
	private void CopyRobotElementToMapElement(ArrayList<MapElement> mapObjects, RobotElement robotElement) {
		MapElement newRobotElement = new MapElement();

		// copy the values of element robot;
		newRobotElement.setElementType(robotElement.getType());
		newRobotElement.setElementName(robotElement.getDirection());

		if (robotElement.getAttributeKeys() != null && robotElement.getAttributeKeys().size() > 0) {
			newRobotElement.setAttributeKey(robotElement.getAttributeKey(0));
		}
		if (robotElement.getAttributeValues() != null && robotElement.getAttributeValues().size() > 0) {
			newRobotElement.setAttributeValue(robotElement.getAttributeValue(0));
		}

		newRobotElement.setElementId("");
		newRobotElement.setRadius(0);
		newRobotElement.setPoint(robotElement.getPoint().x, robotElement.getPoint().y);
		mapObjects.add(newRobotElement);
	}

	/**
	 * addAttributes
	 *
	 * @param current , objects, indexofObject
	 */
	public void addAttributes(Element current, MapElement object) {

		if (object.getAttributeKeys() == null || object.getAttributeValues() == null) {
			return;
		}

		if (object.getAttributeKeys().size() == 0 || object.getAttributeValues().size() == 0) {
			return;
		}

		Element attribute = null;

		if (object.getAttributeKey(0) != null && object.getAttributeKey(0).length() > 0
				|| object.getAttributeValue(0) != null && object.getAttributeValue(0).length() > 0) {
			attribute = current.addElement("attribute");

			if (object.getAttributeKey(0).length() > 0) {
				Element key = attribute.addElement("key");
				key.setText(object.getAttributeKey(0));
			}
			if (object.getAttributeValue(0).length() > 0) {
				Element key = attribute.addElement("value");
				key.setText(object.getAttributeValue(0));
			}
		}
	}

	/*
	 * This function is called to search for particular element by Element name
	 * return the index of corresponding object or -1 otherwise.
	 *
	 * selectElementByName
	 *
	 * @param objects, targetName
	 */
	@SuppressWarnings("unused")
	private int selectElementByName(ArrayList<MapElement> objects, String targetName) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).getElementName().matches(targetName)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * This function is called to search for particular element by Element type
	 * return the index of corresponding object or -1 otherwise
	 *
	 * selectElementByType
	 *
	 * @param objects, targetType
	 */
	private int selectElementByType(ArrayList<MapElement> objects, String targetType) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).getElementType().matches(targetType)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * This function is called to add element for current Element
	 *
	 * addElements
	 *
	 * @param current, objects, indexOfobject, NumberOfPoints
	 */
	private void addPoints(Element current, MapElement objects, int NumberOfPoints) {
		Element points;

		for (int i = 0; i < NumberOfPoints; i++) {
			int x = (int) objects.getPoint(i).getX();
			int y = (int) objects.getPoint(i).getY();
			points = current.addElement("point");
			points.addAttribute("x", "" + x);
			points.addAttribute("y", "" + y);
		}
	}

	/*
	 * This function is called to add all beacon to xml file by using stack of
	 * indices of elements
	 *
	 * addBeacon
	 *
	 * @param current, objects
	 */
	private void addBeacon(Element current, Stack<Integer> stackOfIndices, ArrayList<MapElement> objects) {
		countOfObjectsByType(objects, stackOfIndices, "beacon");
		int index;
		int count = stackOfIndices.size();

		for (int i = 1; i <= count; i++) {
			Element beacon = current.addElement("beacon");
			index = stackOfIndices.pop();

			int x = (int) objects.get(index).getPoint(0).x;
			int y = (int) objects.get(index).getPoint(0).y;

			beacon.addAttribute("name", "beacon");
			if (objects.get(index).getElementId().length() > 0) {
				beacon.addAttribute("id", "" + objects.get(index).getElementId());
			}
			if (objects.get(index).getRadius() > 0) {
				beacon.addAttribute("radius", "" + objects.get(index).getRadius());
			}
			// each mine has one point only
			Element point = beacon.addElement("point");
			point.addAttribute("x", "" + x);
			point.addAttribute("y", "" + y);
		}
	}

	/*
	 * This function is called to add all mines to a mineField file by using stack
	 * of indices of elements addMines
	 *
	 * @param current, objects
	 */
	private void addMines(Element current, Stack<Integer> stackOfIndices, ArrayList<MapElement> objects) {
		countOfObjectsByType(objects, stackOfIndices, "mine");
		int index;
		int count = stackOfIndices.size();

		for (int i = 1; i <= count; i++) {
			Element mine = current.addElement("mine");
			index = stackOfIndices.pop();

			int x = (int) objects.get(index).getPoint(0).x;
			int y = (int) objects.get(index).getPoint(0).y;

			mine.addAttribute("name", "mine");
			if (objects.get(index).getElementId().length() > 0) {
				mine.addAttribute("id", "" + objects.get(index).getElementId());
			}
			if (objects.get(index).getRadius() > 0) {
				mine.addAttribute("radius", "" + objects.get(index).getRadius());
			}
			// each mine has one point only
			Element point = mine.addElement("point");
			point.addAttribute("x", "" + x);
			point.addAttribute("y", "" + y);
		}
	}

	/**
	 * Method to search and get the indices of objects in MapElements Array by Name
	 * of Element
	 *
	 * countOfObjects
	 *
	 * @param MapObject , indexOfObjects. targetObject
	 *
	 */
	private void countOfObjectsByName(ArrayList<MapElement> mapObjects, Stack<Integer> stackOfIndices,
			String targetObject) {
		stackOfIndices.clear();

		for (int i = 0; i < mapObjects.size(); i++) {
			if (mapObjects.get(i).getElementName().matches(targetObject)) {
				stackOfIndices.push(i);
			}
		}
	}

	/**
	 * Method to search and get the indices of objects in MapElements Array by type
	 * of Element countOfObjectsByType
	 *
	 * @param mapObjects , stackOfIndices, targetObject void
	 */
	private void countOfObjectsByType(ArrayList<MapElement> mapObjects, Stack<Integer> stackOfIndices,
			String targetObject) {
		stackOfIndices.clear();

		for (int i = 0; i < mapObjects.size(); i++) {
			if (mapObjects.get(i).getElementType().matches(targetObject)) {
				stackOfIndices.push(i);
			}
		}
	}
}
