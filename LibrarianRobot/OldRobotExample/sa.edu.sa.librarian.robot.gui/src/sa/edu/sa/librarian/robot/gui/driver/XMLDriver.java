package sa.edu.sa.librarian.robot.gui.driver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import sa.edu.sa.librarian.robot.gui.model.MapElement;
import sa.edu.sa.librarian.robot.gui.model.RobotElement;

/**
 * This Class provide functions to read XML from a file or write XML to a file
 * 
 */
public class XMLDriver {

	/**
	 * Get the Array of Map Elements from a XML file
	 *
	 * @param filePath The XML file where to read the maps elements
	 * @return An Array of Map Elements, with the elements on the map
	 * @throws java.io.FileNotFoundException
	 */
	public ArrayList<MapElement> readMapElementsXML(String filePath) throws FileNotFoundException {
		ReadXML xmlFile = new ReadXML();
		return xmlFile.readFromXML(filePath);
	}

	/**
	 * Get the Robot element from a XML file
	 *
	 * @param filePath The XML file where to read the robot element
	 * @return A Robot Element with the information of the robot on the map
	 * @throws java.io.FileNotFoundException
	 */
	public RobotElement readRobotXML(String filePath) throws FileNotFoundException {
		ReadXML xmlRobot = new ReadXML();
		return xmlRobot.ReadRobot(filePath);
	}

	/**
	 * Write to a XML file the information about the elements on the map and the
	 * Robot on the map
	 *
	 * @param filePath     The XML file where to store the information
	 * @param mapElements  The ArrayList of map elements on the map
	 * @param robotElement The Robot information on the map
	 * @return Whether the operation success
	 * @throws java.io.IOException
	 */
	public boolean writeToXML(String filePath, ArrayList<MapElement> mapElements, RobotElement robotElement)
			throws IOException {
		WriteXML object = new WriteXML();
		return object.writeToXML(filePath, mapElements, robotElement);
	}
}
