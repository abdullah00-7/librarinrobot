package sa.edu.sa.librarian.robot.gui.driver;

import org.w3c.dom.*;

import sa.edu.sa.librarian.robot.gui.model.*;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * used exchange a XML file and XmlMap instance XmlMapAdapter used to read xml
 * file and write into XML.
 */

public class ReadXML {

	ArrayList<MapElement> xmlElements = new ArrayList<MapElement>(); // all the element in this array list.

	/**
	 * Read all the map element's part from xml file
	 *
	 * @param sourceXmlFilePath
	 * @return the mapElements
	 * @throws java.io.FileNotFoundException
	 */
	public ArrayList<MapElement> readFromXML(String sourceXmlFilePath) throws java.io.FileNotFoundException {

		// System.out.printf("%s",sourceXmlFilePath);

		try {
			// create and open document
			File map = new File(sourceXmlFilePath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(map);
			doc.getDocumentElement().normalize();
			MapElement mine_map = new MapElement();
			NodeList narea = null; // store tag name temp
			narea = doc.getElementsByTagName("minefield-map");
			mine_map.setElementType("minefield-map");
			NodeList listOfAttributes = doc.getElementsByTagName("attribute");
			if (listOfAttributes.item(0) == null) {
				mine_map.setAttributeKey("");
				mine_map.setAttributeValue("");
			} else {
				Node AttributeNode = listOfAttributes.item(0);

				if (AttributeNode.getNodeType() == Node.ELEMENT_NODE) {
					Element AttributeElement = (Element) AttributeNode;
					NodeList KeyList = AttributeElement.getElementsByTagName("key");
					int nkeys = KeyList.getLength();
					NodeList ValueList = AttributeElement.getElementsByTagName("value");
					int nvalues = ValueList.getLength();

					if (nkeys != nvalues) {
						// System.out.println("Different number of keys and values");
					} else {
						for (int key_pair = 0; key_pair < nkeys; key_pair++) {
							for (int childnodes = 0; childnodes < KeyList.item(key_pair).getChildNodes()
									.getLength(); childnodes++) {
								mine_map.setAttributeKey(
										KeyList.item(key_pair).getChildNodes().item(childnodes).getNodeValue());
								// System.out.println(KeyList.item(key_pair).getChildNodes().item(childnodes).getNodeValue());
							}
							for (int childnodes = 0; childnodes < ValueList.item(key_pair).getChildNodes()
									.getLength(); childnodes++) {
								mine_map.setAttributeValue(
										ValueList.item(key_pair).getChildNodes().item(childnodes).getNodeValue());
								// System.out.println(ValueList.item(key_pair).getChildNodes().item(childnodes).getNodeValue());
							}
						}
					}
				} else {
					throw new Exception("can not find attribute");
				}
			}
			// Xml.add(Mine_map);
			// }
			xmlElements.add(mine_map);
			// read zone and set type, name and point position.
			narea = doc.getElementsByTagName("zone");

			if (narea.getLength() >= 0) {

				for (int streetIdx = 0; streetIdx < narea.getLength(); streetIdx++) {
					MapElement ZoneTemp = new MapElement();
					ZoneTemp.setElementType("zone");
					String ZoneName = narea.item(streetIdx).getAttributes().getNamedItem("name").getNodeValue();
					ZoneTemp.setElementName(ZoneName);
					String ZoneId = narea.item(streetIdx).getAttributes().getNamedItem("id").getNodeValue();
					ZoneTemp.setElementId(ZoneId);

					NodeList listOfZoneAttributes = doc.getElementsByTagName("attribute");

					if (listOfAttributes.item(0) == null) {
						ZoneTemp.setAttributeKey("");
						ZoneTemp.setAttributeValue("");
					} else {
						Node ZoneAttributeNode = listOfZoneAttributes.item(0);

						if (ZoneAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
							Element AttributeElement = (Element) ZoneAttributeNode;
							NodeList KeyList = AttributeElement.getElementsByTagName("key");
							int nkeys = KeyList.getLength();
							NodeList ValueList = AttributeElement.getElementsByTagName("value");
							int nvalues = ValueList.getLength();

							if (nkeys != nvalues) {
								// System.out.println("Different number of keys and values");
							} else {
								for (int key_pair = 0; key_pair < nkeys; key_pair++) {
									for (int childnodes = 0; childnodes < KeyList.item(key_pair).getChildNodes()
											.getLength(); childnodes++) {
										mine_map.setAttributeKey(
												KeyList.item(key_pair).getChildNodes().item(childnodes).getNodeValue());
										// System.out.println(KeyList.item(key_pair).getChildNodes().item(childnodes).getNodeValue());
									}
									for (int childnodes = 0; childnodes < ValueList.item(key_pair).getChildNodes()
											.getLength(); childnodes++) {
										mine_map.setAttributeValue(ValueList.item(key_pair).getChildNodes()
												.item(childnodes).getNodeValue());
										// System.out.println(ValueList.item(key_pair).getChildNodes().item(childnodes).getNodeValue());
									}
								}
							}
						} else {
							throw new Exception("can not find attribute");
						}
					}

					NodeList Points = ((Element) narea.item(streetIdx)).getElementsByTagName("point");
					int nPoints = Points.getLength();

					// length -= nPoints;

					// Loop through points
					for (int pointIdx = 0; pointIdx < nPoints; pointIdx++) {
						NamedNodeMap pointData = Points.item(pointIdx).getAttributes();
						int x = Integer.parseInt(pointData.getNamedItem("x").getNodeValue());
						int y = Integer.parseInt(pointData.getNamedItem("y").getNodeValue());
						ZoneTemp.setPoint(x, y);
						// ZoneTemp.setYPos(y);
						// System.out.println("X: " + x + " Y:" + y);
					}

					xmlElements.add(ZoneTemp);
				}

			}

			// read area and set type and point position.
			// read beacon and set type and point position.
			narea = doc.getElementsByTagName("beacon");

			if (narea.getLength() >= 0) {

				for (int streetIdx = 0; streetIdx < narea.getLength(); streetIdx++) {
					String BeaconName = narea.item(streetIdx).getAttributes().getNamedItem("name").getNodeValue();
					String BeaconId = narea.item(streetIdx).getAttributes().getNamedItem("id").getNodeValue();
					int BeaconRadius = Integer
							.parseInt(narea.item(streetIdx).getAttributes().getNamedItem("radius").getNodeValue());
					MapElement BeaconTemp = new MapElement();
					BeaconTemp.setElementName(BeaconName);
					BeaconTemp.setElementType("beacon");
					BeaconTemp.setElementId(BeaconId);
					BeaconTemp.setRadius(BeaconRadius);
					NodeList Points = ((Element) narea.item(streetIdx)).getElementsByTagName("point");
					int nPoints = Points.getLength();

					for (int pointIdx = 0; pointIdx < nPoints; pointIdx++) { // Loop
																				// through
																				// points
						NamedNodeMap pointData = Points.item(pointIdx).getAttributes();
						int x = Integer.parseInt(pointData.getNamedItem("x").getNodeValue());
						int y = Integer.parseInt(pointData.getNamedItem("y").getNodeValue());

						BeaconTemp.setPoint(x, y);
						// BeaconTemp.setYPos(y);
						// System.out.println("X: "+x+" Y:"+y);
					}

					xmlElements.add(BeaconTemp);
				}

			}

			// read mine and set type and point position.
			narea = doc.getElementsByTagName("mine");

			if (narea.getLength() >= 0) {

				for (int streetIdx = 0; streetIdx < narea.getLength(); streetIdx++) {
					String mineName = narea.item(streetIdx).getAttributes().getNamedItem("name").getNodeValue();
					String mineId = narea.item(streetIdx).getAttributes().getNamedItem("id").getNodeValue();
					int mineRadius = Integer
							.parseInt(narea.item(streetIdx).getAttributes().getNamedItem("radius").getNodeValue());
					MapElement mineElement = new MapElement();
					mineElement.setElementName(mineName);
					mineElement.setElementType("mine");
					mineElement.setElementId(mineId);
					mineElement.setRadius(mineRadius);
					NodeList Points = ((Element) narea.item(streetIdx)).getElementsByTagName("point");
					int nPoints = Points.getLength();

					for (int pointIdx = 0; pointIdx < nPoints; pointIdx++) {
						NamedNodeMap pointData = Points.item(pointIdx).getAttributes();
						int x = Integer.parseInt(pointData.getNamedItem("x").getNodeValue());
						int y = Integer.parseInt(pointData.getNamedItem("y").getNodeValue());

						mineElement.setPoint(x, y);
					}

					xmlElements.add(mineElement);
				}
			}

			return xmlElements;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("read finish");
		return xmlElements;

	}

	/**
	 * Read robot's part in xml file
	 *
	 * @param sourceXmlFilePath
	 * @return
	 * @throws java.io.FileNotFoundException
	 */
	public RobotElement ReadRobot(String sourceXmlFilePath) throws java.io.FileNotFoundException {
		try {
			File map = new File(sourceXmlFilePath);
			RobotElement robot = new RobotElement();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(map);

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList narea = null;
			Element elearea = null;
			NodeList nareap = null;

			// Search the ROBOT tag on the file
			narea = doc.getElementsByTagName("robot");

			if (narea.getLength() == 0) // There is not Robot Tag on the XML File
			{
				return null;
			} else {
				// There is a robot on the XML File, the get it and fill teh RobotElement Object
				String RobotDirection = narea.item(0).getAttributes().getNamedItem("direction").getNodeValue();
				robot.setType("robot");
				robot.setDirection(RobotDirection);

				for (int i = 0; i < narea.getLength(); i++) {
					elearea = (Element) narea.item(i);
					nareap = elearea.getElementsByTagName("point");

					for (int j = 0; j < nareap.getLength(); j++) {
						Element eleareap = (Element) nareap.item(j);
						robot.setPoint(Integer.parseInt(eleareap.getAttribute("x")),
								Integer.parseInt(eleareap.getAttribute("y")));
						// robot.setYPos(Integer.parseInt(eleareap.getAttribute("y")));
					}
				}

				return robot;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
