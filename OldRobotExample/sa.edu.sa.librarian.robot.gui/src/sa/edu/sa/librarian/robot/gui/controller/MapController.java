package sa.edu.sa.librarian.robot.gui.controller;

import java.awt.Point;
import java.util.ArrayList;
import sa.edu.sa.librarian.robot.gui.driver.GraphicDriver;
import sa.edu.sa.librarian.robot.gui.driver.RobotPathDriver;
import sa.edu.sa.librarian.robot.gui.model.MapElement;
import sa.edu.sa.librarian.robot.gui.model.RobotElement;
import sa.edu.sa.librarian.robot.gui.model.RobotLocation;
import sa.edu.sa.librarian.robot.gui.view.MapWindow;
import sa.edu.sa.librarian.robot.gui.view.MapEditorWindow;

/**
 * This class is the controller for the Map Window
 * 
 */
public class MapController extends Controller {

	// view
	private MapWindow mapWindow = null;
	private MapEditorWindow mapEditor = null;
	// Driver
	private GraphicDriver graphicDriver;
	// int y = 0;
	// int x = 0;
	private ArrayList<MapElement> mapElements = new ArrayList<MapElement>();
	private RobotElement robotElements = null; // new RobotElement();
	// status
	private int currentState = 0;
	private int status_init = 0;
	private int status_LoadMap = 1;
	private int status_AddZone = 2;
	private int status_SaveZone = 3;
	private ArrayList<Point> zonePoints = new ArrayList<Point>();
	private String UuidToSelect = new String();
	private boolean firstTime = true;

	/**
	 * the constructor of the map controller
	 * 
	 * @param period
	 */
	public MapController(long period) {
		super(period);
	}

	@Override
	public void run() {
		this.rePaintGraphic();
	}
	
	@Override
	public void init() {
		this.mapWindow = CoreController.getMapWindow();
		this.mapEditor = CoreController.getMapEditorWin();
		this.mapWindow.resetBufferdGraphicSize(1, 1);
		// connect the map window to Driver
		this.graphicDriver = new GraphicDriver(this.mapWindow);
		this.startTimer();
	}

	/**
	 * Return the mapWindow instance of the map Controller
	 * 
	 * @return a MapWindow object
	 */
	public MapWindow getMapWindow() {
		return this.mapWindow;
	}

	/**
	 * Return the mapEditor instance of the map Controller
	 * 
	 * @return a MapEditor object
	 */
	public MapEditorWindow getMapEditorWindow() {
		return this.mapEditor;
	}

	/**
	 * Set the Map Element Array, to be later drawn
	 * 
	 * @param mapElements The Map Element ArrayList
	 */
	public void setMapElement(ArrayList<MapElement> mapElements) {
		this.mapElements = mapElements;
		// nextState(this.status_LoadMap);
		this.rePaintGraphic();
	}

	/**
	 * Set the Robot Element, to be later drawn
	 * 
	 * @param r The robot element object
	 */
	public void setRobotElement(RobotElement r) {
		this.robotElements = r;
		this.rePaintGraphic();
	}

	/**
	 * Get the current mapElements Array, with the elements present on the map
	 * 
	 * @return The Map Element ArrayList with the current elements on it
	 */
	public ArrayList<MapElement> getMapElement() {
		return this.mapElements;
	}

	/**
	 * Get the information of the Robot, if it is present on the map
	 * 
	 * @return The Robot Element
	 */
	public RobotElement getRobotElement() {
		return this.robotElements;
	}

	/**
	 * Set the new state of the Map Controller
	 * 
	 * @param newState Integer with the value of the new state. Init = 0 MapLoaded =
	 *                 1 AddZone = 2 SaveZone = 3
	 */
	public void nextState(int newState) {
		this.currentState = newState;
		if (newState == status_AddZone) {
			zonePoints.clear();
		}
		this.rePaintGraphic();

	}

	/**
	 * repaint the graphic
	 */
	public void rePaintGraphic() {
		// System.out.println("repaint graphic");
		// Reset the size of the map
		this.graphicDriver.setNewSize(mapWindow.getWidth(), mapWindow.getHeight());

		// Clean the map
		this.graphicDriver.CLS();

		// draw the boundary of the map
		this.graphicDriver.drawBoundary();

		// draw elements from the Elements object
		this.graphicDriver.drawElements(this.mapElements);

		// draw robot element
		this.graphicDriver.drawRobot(this.robotElements);

		// select an element by its id
		if (UuidToSelect != null) {
			this.graphicDriver.selectByUuid(this.mapElements, UuidToSelect);
		}

		// If the state has been seted to AddZone, then display the click on the map
		// area
		if (currentState == this.status_AddZone) {
			this.graphicDriver.drawSetPoints(this.zonePoints);
		}

		// draw the grid cells
		this.graphicDriver.drawGrid();
	}

	/**
	 * Capture the point correspondent to the clicks done in the map area. This is
	 * invoked by the Window_Map_Editor. The clicks are normalised according to the
	 * relative position on the map area.
	 * 
	 * @param x The position x of the click
	 * @param y The position y of the click
	 */
	public void newPoint(int x, int y) {
		Point p;
		int nx = this.graphicDriver.normClickX(x);
		int ny = this.graphicDriver.normClickY(y);
		p = new Point(nx, ny);

		// CHECK IF THE POINT IS INSIDE ANOTHER AREA
		zonePoints.add(p);
		this.rePaintGraphic();
	}

	/**
	 * Create a new element in the map, by adding a new element in the ArrayList
	 * MapElement.
	 * 
	 * @param elementType Type of the new element
	 * @param elementName Name of the new element
	 * @param elementId   Id of the new element. This is not the Uuid of the element
	 */
	public void createElement(String elementType, String elementName, String elementId) {
		MapElement e = new MapElement();
		e.setElementType(elementType);
		e.setElementName(elementName);
		e.setElementId(elementId);

		// FIX THE PROBLEM OF ADDING ELEMENTS (MINES) WITHOUT ANY POINTS

		// Gathering the points
		for (int i = 0; i < zonePoints.size(); i++) {
			e.setPoint(zonePoints.get(i).x, zonePoints.get(i).y);
		}

		if (elementType.equals("mine")) {
			e.setRadius(50);
		}
		if (elementType.equals("beacon")) {
			e.setRadius(10);
		}

		// Add the new element to the array of Map Elements
		mapElements.add(e);

		// Remove all the points from the array of points
		zonePoints.clear();

		this.rePaintGraphic();
		// Return to the original status
		nextState(status_LoadMap);
	}

	/**
	 * create the Elements
	 * 
	 * @param elementType elementType
	 * @param elementName elementType
	 * @param elementId   elementType
	 * @param x           position x
	 * @param y           position y
	 * @return the UUID of the elements
	 */
	public String createElement(String elementType, String elementName, String elementId, int x, int y) {
		String id;
		// the mine should be drawn on the screen and then ask user whether is mine or
		// not
		// in case of no, the creaated mine will be deleted otherwise will be still
		// there
		MapElement e = new MapElement();

		e.setElementType(elementType);
		e.setElementName(elementName);
		e.setElementId(elementId);
		e.setPoint(x, y);
		if (elementType.equals("mine")) {
			e.setRadius(50);
		}
		// Add the new element to the array of Map Elements
		mapElements.add(e);

		// Get and return the id of the new mine element
		id = e.getElementUuid();
		this.rePaintGraphic();
		return id;
	}

	/**
	 * set the element to selected status
	 * 
	 * @param Uuid
	 */
	public void setElementToSelect(String Uuid) {
		// Set the element to be selected
		this.UuidToSelect = Uuid;

		/** TESTING THE PATH */
		ArrayList<RobotLocation> robotPath = new ArrayList<RobotLocation>();
		Point p = new Point(50, 50);
		RobotPathDriver rp = new RobotPathDriver(p, 90);
//        System.out.printf("Calling to get the path....\n");
		robotPath = rp.getRobotPath();
		for (int i = 0; i < robotPath.size(); i++) {
//            System.out.printf("%d: (%d,%d) -> %d\n", i, robotPath.get(i).getPosition().x, robotPath.get(i).getPosition().y, robotPath.get(i).getDirection());
		}
//        System.out.printf("That was the path....\n");
		/** END TESTING THE PATH */
		this.rePaintGraphic();
	}

	/**
	 * If the input Uuid correspond to an element on the ArrayList MapElement, then
	 * remove from the Array. This causes the element is deleted from the map
	 * 
	 * @param Uuid A string with a valid Uuid to identify a map element
	 */
	public void deleteElement(String Uuid) {
		// Find the element on the Array of elements
		for (int i = 0; i < this.mapElements.size(); i++) {
			// If found the same Uuid, then remove from the list
			if (this.mapElements.get(i).getElementUuid().equals(Uuid)) {
				this.mapElements.remove(i);
			}
		}
		this.rePaintGraphic();
	}
}
