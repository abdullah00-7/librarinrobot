package sa.edu.sa.librarian.robot.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.controller.RobotController;
import sa.edu.sa.librarian.robot.gui.model.NXTConnectionTabelModel;

/**
 * a window to manage the robot's connection
 */
public class ConnectionWindow extends JDialog implements IWindow {

	private static final long serialVersionUID = 1L;
	// when 0 get robots , when 1 get file
	private JButton jButton_Connect;
	private JButton jButton_Search;
	private JLabel jLabel_Title;
	private JPanel jPanel_Button;
	private JPanel jPanel_Container;
	private JPanel jPanel_Content;
	// connection
	private JScrollPane jScrollPane1;
	private JTable jTable_Content;
	// Connect to robot Controller
	private RobotController robotController = null;

	/**
	 * default constructor to build the window
	 */
	public ConnectionWindow() {
		initComponents();
	}

	/**
	 * Fill the Table of robots
	 *
	 * @param tableModel
	 */
	public void fillTable(NXTConnectionTabelModel tableModel) {
		this.jTable_Content.removeAll();
		this.jTable_Content.setModel(tableModel);
		this.jTable_Content.setVisible(true);
	}

	@Override
	public void init() {
		this.robotController = CoreController.getRobotController();
		// title
		this.setTitle("Robots Connection Managerment");
		// location
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - this.getWidth()) / 2;
		int y = (screenSize.height - this.getHeight()) / 2;
		int width = 480;
		int height = 420;
		this.setBounds(x, y, width, height);
		// set the top most but invisible
		this.setModal(true);
		this.setAlwaysOnTop(true);
		this.setVisible(false);
	}

	/**
	 * The initialize component
	 */
	private void initComponents() {
		jPanel_Container = new JPanel();
		jPanel_Button = new JPanel();
		jButton_Connect = new JButton();
		jButton_Search = new JButton();
		jPanel_Content = new JPanel();
		jScrollPane1 = new JScrollPane();
		jTable_Content = new JTable();
		jLabel_Title = new JLabel();

		setLayout(new java.awt.BorderLayout());

		jPanel_Container.setBackground(java.awt.Color.yellow);
		jPanel_Container.setLayout(new java.awt.BorderLayout());

		// set the button panel with a border
		jPanel_Button.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		jButton_Connect.setText("Connect");
		jPanel_Button.add(jButton_Connect);

		jButton_Search.setText("Refresh");
		jPanel_Button.add(jButton_Search);

		jPanel_Container.add(jPanel_Button, BorderLayout.PAGE_END);

		jPanel_Content.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel_Content.setLayout(new BorderLayout());
		jScrollPane1.setViewportView(jTable_Content);

		jPanel_Content.add(jScrollPane1, BorderLayout.CENTER);
		jLabel_Title.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel_Title.setText("Available Robots");
		jPanel_Content.add(jLabel_Title, BorderLayout.PAGE_START);
		jPanel_Container.add(jPanel_Content, BorderLayout.CENTER);

		add(jPanel_Container, BorderLayout.CENTER);

		// listner for the Connect button
		jButton_Connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				ButtonPressHandle(evt);
			}
		});
		// listner for the search button
		jButton_Search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				ButtonPressHandle(evt);
			}
		});
	}

	/**
	 * handle the button event
	 * 
	 * @param evt
	 */
	public void ButtonPressHandle(ActionEvent evt) {
		String Command = evt.getActionCommand();
		if (Command.equals("Connect")) {
			int selectIndex = this.jTable_Content.getSelectedRow();
			this.robotController.connectToNXT(selectIndex);
		}

		if (Command.equals("Refresh")) {
			CoreController.getNxtDriver().search();
		}

	}
}
