package sa.edu.sa.librarian.robot.gui.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.DefaultListModel;
import java.util.ArrayList;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.controller.MapController;
import sa.edu.sa.librarian.robot.gui.model.MapElement;

public class MapEditorWindow extends JPanel implements IWindow {

	private JList jList_Elements;
	private JScrollPane jScrollPane_List;
	private JButton jbtn_Add;
	private JButton jbtn_delete;
	private boolean state_add_pasive = true;
	Object[] possibilities = { "No Go Zone", "Safe Zone", "Searching Zone", "Mine", "Beacon" };
	private int status_AddZone = 2;
	private int listElementSelected = -1;
	DefaultListModel model = new DefaultListModel();
	private String elementType = new String();
	private String elementName = new String();
	private ArrayList<MapElement> me = new ArrayList<MapElement>();
	private ArrayList<String> elementListData = new ArrayList<String>();
	private ArrayList<String> elementListId = new ArrayList<String>();
	// the controller
	private MapController mapController = null;

	/**
	 * the default constructor of the map editor
	 */
	public MapEditorWindow() {
		initComponents();

		// Add listener to the mouse, when click on the list
		MouseListener mouseListener = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				selectMapElement(e);
			}
		};
		jList_Elements.addMouseListener(mouseListener);
	}

	@Override
	public void init() {
		this.mapController = CoreController.getMapController();
	}

	private void initComponents() {

		jScrollPane_List = new JScrollPane();
		jList_Elements = new JList();
		jbtn_Add = new JButton();
		jbtn_delete = new JButton();

		jList_Elements = new JList(model);
		jList_Elements.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jList_Elements.setLayoutOrientation(JList.VERTICAL);
		jList_Elements.setVisibleRowCount(-1);
		jList_Elements.setBackground(Color.yellow);
		jScrollPane_List.setViewportView(jList_Elements);

		jbtn_Add.setText("Add Element");
		jbtn_Add.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtn_AddActionPerformed(evt);
			}
		});

		jbtn_delete.setText("Delete Element");
		jbtn_delete.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbtn_deleteActionPerformed(evt);
			}
		});

		this.setLayout(null);
		this.add(this.jScrollPane_List);
		jScrollPane_List.setBorder(BorderFactory
				.createTitledBorder(new LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Map Information"));
		this.jScrollPane_List.setBounds(5, 5, 250, 240);

		// add btn
		this.add(this.jbtn_Add);
		this.jbtn_Add.setBounds(5, 250, 120, 20);

		this.add(this.jbtn_delete);
		this.jbtn_delete.setBounds(130, 250, 120, 20);

	}

	/**
	 * This method take the action after click the button add a new element to the
	 * map If the bottom is in a Passive State: Display "Add Element" and begins the
	 * action of adding clicks on the map area. Set the bottom to active mode If the
	 * bottom is in a Active State: Display "Finish..." and ends the action of
	 * adding an element on the map. Set the bottom to pasive mode
	 * 
	 * @param evt Action event of the bottom
	 */
	private void jbtn_AddActionPerformed(ActionEvent evt) {
		// Pasive state: As the windows if loaded. No new element is been added.
		// Toogle to adding mode
		if (state_add_pasive) {
			String s = (String) JOptionPane.showInputDialog(null, "Choose the type of element to be created:\n",
					"Add Element", JOptionPane.PLAIN_MESSAGE, null, possibilities, "No Go Zone");

			// If a string was returned, then call to create the new element
			if ((s != null) && (s.length() > 0)) {
				if (s.equals("No Go Zone")) {
					elementType = "zone";
					elementName = "noGoZone";
				}
				if (s.equals("Safe Zone")) {
					elementType = "zone";
					elementName = "safeZone";
				}
				if (s.equals("Searching Zone")) {
					elementType = "zone";
					elementName = "searchingZone";
				}
				if (s.equals("Mine")) {
					elementType = "mine";
					elementName = "mine";
				}
				if (s.equals("Beacon")) {
					elementType = "beacon";
					elementName = "beacon";
				}

				// System.out.printf("Selected: " + elementType + "!\n");

				// HERE: CALL THE NEW ELEMENT METHOD
				mapController.nextState(status_AddZone);
			} else {
				return; // When nothing has been selected
			}
			// Change the label of the botton.
			jbtn_Add.setText("Finish...");
			jbtn_delete.setEnabled(false);

			// Change the state
			state_add_pasive = false;
			return;
		}

		// Active state: Finish the adding mode, call the ending adding mode and toggle
		// the text of the bottom
		if (!state_add_pasive) {
			// System.out.printf("No more points: !\n");

			// HERE: CALL THE END OF NEW ELEMENT METHOD
			mapController.createElement(elementType, elementName, "00"); // CHECK THE ID OF THE NEW ELEMENT
			// mapController.nextState(status_SaveZone);

			// Change the label of the botton.
			jbtn_Add.setText("Add Element");

			// Change the state
			state_add_pasive = true;
			jbtn_delete.setEnabled(true);

			// refresh the list of elements
			this.fillList();
			return;
		}
	}

	/**
	 * This method take the action after click the bottom delete to remove an
	 * element from the map Checks if an element has been selected from the list. In
	 * this case, delete the selected element. Otherwise, do nothing.
	 * 
	 * @param evt Acttion event of the bottom
	 */
	private void jbtn_deleteActionPerformed(java.awt.event.ActionEvent evt) {

		// Check whether an element has been selected in the list
		if (listElementSelected > -1) {
			// HERE CALL THE METHOD TO DELETE AN ELEMENT MAP
			// System.out.printf("Delete element (%s)-%s !\n",
			// this.elementListId.get(listElementSelected),
			// this.elementListData.get(listElementSelected));
			mapController.deleteElement(this.elementListId.get(listElementSelected));
		}

		// refresh the list of elements
		this.fillList();
	}

	/**
	 * This method fill the JList with all the current mapElements in the Array It
	 * sets the global lists with the data (display in the List) and its relate Uuid
	 */
	public void fillList() {
		// Get the current elements
		me = mapController.getMapElement();

		// Clear previous items on the list
		elementListData.clear();
		elementListId.clear();
		model.clear();

		// Set the arrays for the list and their id
		for (int i = 0; i < me.size(); i++) {
			elementListData.add("[" + me.get(i).getElementType() + "] : " + me.get(i).getElementName() + "-"
					+ me.get(i).getElementId());
			elementListId.add(me.get(i).getElementUuid());
		}

		// Fill the list
		for (int i = 0; i < elementListData.size(); i++) {
			model.add(i, elementListData.get(i));
		}

		// Update the elements on the list and repaint
		jList_Elements.setModel(model);
	}

	/**
	 * This method collect the information of the mapElement selected, by a click,
	 * in the JList It sets the global index of the element being set, to let it
	 * available in the case it's going to be deleted.
	 * 
	 * @param e
	 */
	private void selectMapElement(MouseEvent e) {
		int index = jList_Elements.locationToIndex(e.getPoint());

		// set the position
		listElementSelected = index;

		// call to select the element
		mapController.setElementToSelect(this.elementListId.get(index));
		CoreController.getMapController().rePaintGraphic();
	}
}
