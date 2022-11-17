package sa.edu.sa.librarian.robot.gui.controller;

import java.util.ArrayList;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.edu.sa.librarian.robot.gui.driver.XMLDriver;
import sa.edu.sa.librarian.robot.gui.model.MapElement;
import sa.edu.sa.librarian.robot.gui.model.RobotElement;
import sa.edu.sa.librarian.robot.gui.view.MainFrame;
import sa.edu.sa.librarian.robot.gui.view.MapEditorWindow;

/**
 * The Controller of the application
 */
public class GuiController extends Controller {
	// the view will going to shown in the MainWindow
	private MainFrame mainWindow = null;
	private MapEditorWindow mapEditorWindow = null;
	// the Controllers
	private MapController mapController = null;
	// the components in Window
	private JFileChooser chooser;
	private FileNameExtensionFilter filter;
	// the XML Driver
	private XMLDriver xmlDriver = null;
	private ArrayList<MapElement> mapElements = null;
	private RobotElement RobotElements = null;

	/**
	 * Constructor of the class
	 * 
	 * @param period Time of ticks
	 */
	public GuiController(long period) {
		super(period);
	}

	@Override
	public void run() {
	}

	@Override
	public void init() {
		this.mainWindow = CoreController.getMainWindow();
		this.mapController = CoreController.getMapController();
		this.mapEditorWindow = CoreController.getMapEditorWin();
		xmlDriver = new XMLDriver();

		chooser = new JFileChooser(System.getProperty("user.dir"));
		filter = new FileNameExtensionFilter("XML(*.xml)", "xml");
		chooser.setFileFilter(filter);
	}

	/**
	 * Load a map with the information got from an XML File
	 */
	public void LoadMap() {
		String filePath = null;
		int returnVal = chooser.showOpenDialog(this.mainWindow);

		// Choose the name of the XML file to load
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filePath = chooser.getSelectedFile().toString();

			// Load the map
			try {
				// Read the elements from a XML file and set the mapElements and RobotElements
				mapElements = xmlDriver.readMapElementsXML(filePath);
				RobotElements = xmlDriver.readRobotXML(filePath);

				// Call the map controller to set the mapElement and Robot Element
				this.mapController.setMapElement(mapElements);
				this.mapController.setRobotElement(RobotElements);

				// Call to fill the list of the elements loaded
				mapEditorWindow.fillList();

				CoreController.getMapController().rePaintGraphic();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this.mainWindow, "Cannot load this file" + ex.toString());
			}
		}
	}

	/**
	 * Save the current map to a XML file
	 */
	public void saveMap() {
		String filePath = null;
		int returnVal = chooser.showSaveDialog(this.mainWindow);

		// Choose the name of the XML file to save
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filePath = chooser.getSelectedFile().toString();

			// ADD extension if no XML at the end
			if (!filePath.endsWith(".xml") || !filePath.endsWith(".XML")) {
				filePath += ".xml";
			}

			// Save the map
			try {
				// Get the map elements and the robot element
				mapElements = this.mapController.getMapElement();
				RobotElements = this.mapController.getRobotElement();
				// Call to save the map
				this.xmlDriver.writeToXML(filePath, mapElements, RobotElements);
				// Output Message
				String msg = "The map has been saved to a file  \n" + filePath;
				JOptionPane.showMessageDialog(this.mainWindow, msg);
			} catch (IOException ex) {
				String msg = "The map can not be save to the file system";
				JOptionPane.showMessageDialog(this.mainWindow, msg);
			}
		}
	}
}
