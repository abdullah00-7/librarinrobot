package sa.edu.sa.librarian.robot.gui.driver;

import java.util.ArrayList;
import sa.edu.sa.librarian.robot.gui.model.*;

/**
 * Support for Editing Zone on the map
 */
public class MapEditorDriver {

	private ArrayList<MapElement> allMapElementsList = null; // all the map elements
	private ArrayList<MapElement> TypedElementList = null; // represent one type of the elements
	private MapElement currentMapElement = null; // the current Selected/Created Map Elements

	/**
	 * The constructor to invoke the MapElements
	 * 
	 * @param LL_AllMapElements
	 */
	public MapEditorDriver(ArrayList<MapElement> LL_AllMapElements) {
		this.allMapElementsList = LL_AllMapElements;
		this.TypedElementList = new ArrayList<MapElement>();
	}

	/**
	 * Get A type of Map element into List
	 * 
	 * @param type element type
	 * @return list of one specific type of the map elements
	 */
	public ArrayList<MapElement> getElementsByType(String type) {
		TypedElementList.clear();
		// add elements to the list
		for (MapElement e : this.allMapElementsList) {
			if (e.getElementType().equals(type)) {
				TypedElementList.add(e);
			}
		}
		return TypedElementList;
	}

	/**
	 * get the list by using name and type
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public ArrayList<MapElement> getElementsByTypeAndName(String type, String name) {
		TypedElementList.clear();
		// add elements to the list
		for (MapElement e : this.allMapElementsList) {
			if (e.getElementType().equals(type)) {
				if (e.getElementName().equals(name)) {
					TypedElementList.add(e);
				}
			}
		}
		return TypedElementList;
	}

	/**
	 * Create a mapElement
	 * 
	 * @param elementType
	 * @param elementName
	 * @param elementId
	 */
	public void createElement(String elementType, String elementName, String elementId) {
		currentMapElement = new MapElement();
		currentMapElement.setElementType(elementType);
		currentMapElement.setElementName(elementName);
		currentMapElement.setElementId(elementId);
		// To-do here

		this.allMapElementsList.add(currentMapElement);
	}

	/**
	 * Add a point in the current MapElement
	 *
	 * @param x
	 * @param y
	 */
	public void addPoint(int x, int y) {
		// to do your code here
		currentMapElement.setPoint(x, y);
	}

	/**
	 * select a specific mapElement by UUID
	 * 
	 * @param UUID
	 * @return
	 * @throws .MapElementNoExistException
	 */
	public MapElement getElement(String UUID) throws MapElementNoExistException {
		for (MapElement e : this.allMapElementsList) {
			if (e.getElementUuid().equals(UUID)) {
				return e;
			}
		}
		// if not contain the UUID
		String msg = String.format("Elements(%s) is not exist", UUID);
		throw new MapElementNoExistException(msg);
	}

	/**
	 * delete the elements by using the UUID
	 * 
	 * @param UUID
	 * @return
	 * @throws MapElementNoExistException
	 */
	public boolean delElement(String UUID) throws MapElementNoExistException {
		return this.allMapElementsList.remove(getElement(UUID));
	}

	/**
	 * Check whether the element is a no go zone
	 *
	 * @param e
	 * @return
	 */
	private boolean isNoGoZone(MapElement e) {

		boolean isNoGoZone = false;
		if (e.getElementType().equals("zone")) {
			if (e.getElementName().equals("noGoZone")) {
				isNoGoZone = true;
			}
		}
		return isNoGoZone;
	}
}
