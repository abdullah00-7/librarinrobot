package sa.edu.sa.librarian.robot.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.*;
import sa.edu.sa.librarian.robot.gui.StartUp;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.controller.GuiController;
import sa.edu.sa.librarian.robot.gui.model.Command;

/**
 * The Main GUI view showing for the Users
 */
public class MainFrame extends JFrame implements IWindow {

	private static final long serialVersionUID = 2062760208030859011L;
	// the Controller
	private GuiController mainFormController = null;
	// menu
	private JMenu jMenu_Help;
	private JMenu jMenu_Robot;
	private JMenuItem editRobot;
	private JMenuItem upDateDuration;
	private JMenuBar jMenuBar1;
	private JMenuItem jMenuItem_File_Exit;
	private JMenuItem jMenuItem_File_OpenMapFile;
	private JMenuItem jMenuItem_File_SaveMapFile;
	private JMenuItem jMenuItem_Help_AboutBox;
	private JMenuItem jMenuItem_Help_HelpContent;
	private JMenu jMenu_File;
	// panel
	private JPanel jPanel_Left;
	private JPanel jPanel_Left_Top;
	private JPanel jPanel_Left_Buttom;
	private JPanel jPanel_Right;
	private JPanel jPanel_Right_Top;
	private JPanel jPanel_Right_Bottom;
	// the left panel width and the bottom panel_Height
	private int leftPannel_Width = 0;
	private int rightButtomPane_Height = 0;
	private int leftButtomPanel_Height = 0;

	// End of variables declaration
	// the default to build the GUI
	public MainFrame() {
		leftPannel_Width = StartUp.Application_Panel_Left_Width;
		leftButtomPanel_Height = StartUp.Application_Panel_Left_Buttom_Height;
		rightButtomPane_Height = StartUp.Application_Panel_Right_Buttom_Height;
		initComponents();
		this.setTitle("Green Zone Mine Detector");
	}

	@Override
	public void init() {
		// reffer the mainform controller
		this.mainFormController = CoreController.getMainFormController();

		// add the map and the Editor
		// this.jPanel_Right.add(this.mapWindow, java.awt.BorderLayout.CENTER);
		this.jPanel_Right_Top.add(CoreController.getMapWindow(), java.awt.BorderLayout.CENTER);
		this.jPanel_Left_Buttom.add(CoreController.getMapEditorWin(), java.awt.BorderLayout.CENTER);
		// add the robot control
		this.jPanel_Left_Top.add(CoreController.getRobotCtrWin(), java.awt.BorderLayout.CENTER);
		this.jPanel_Right_Bottom.add(CoreController.getLogWindow(), java.awt.BorderLayout.CENTER);

		// display
		this.setSize(1280, 900);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        this.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setResizable(true);
		this.setAlwaysOnTop(false);
		this.setVisible(true);
	}

	/**
	 * Initialize the Components
	 */
	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			// a listenner for closing the application

			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				winClosingHandel();
			}
		});

		// panel
		jPanel_Left = new JPanel();
		jPanel_Left_Top = new JPanel();
		jPanel_Left_Buttom = new JPanel();
		jPanel_Right = new JPanel();
		jPanel_Right_Top = new JPanel();
		jPanel_Right_Bottom = new JPanel();

		// Set the panels
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		// Left Panel
		jPanel_Left.setLayout(new java.awt.BorderLayout());
		jPanel_Left.setPreferredSize(new Dimension(leftPannel_Width, 0));
		// left top panel
		jPanel_Left_Top.setLayout(new java.awt.BorderLayout());
		jPanel_Left_Top.setBorder(border);
		jPanel_Left_Top.setBackground(Color.red);
		// jPanel_Left_Top.setPreferredSize(new java.awt.Dimension(0,
		// letTopPanel_Height));
		// left buttom
		jPanel_Left_Buttom.setLayout(new java.awt.BorderLayout());
		jPanel_Left_Buttom.setBorder(border);
		jPanel_Left_Buttom.setPreferredSize(new java.awt.Dimension(0, this.leftButtomPanel_Height));
		jPanel_Left_Buttom.setBackground(Color.BLUE);

		// add to left
		jPanel_Left.add(this.jPanel_Left_Top, java.awt.BorderLayout.CENTER);
		jPanel_Left.add(this.jPanel_Left_Buttom, java.awt.BorderLayout.SOUTH);
		// add the left to the frame panel
		getContentPane().add(jPanel_Left, java.awt.BorderLayout.WEST);

		// the Right Panel
		jPanel_Right.setLayout(new java.awt.BorderLayout());

		// right buttom
		jPanel_Right_Bottom.setLayout(new java.awt.BorderLayout());
		jPanel_Right_Bottom.setBorder(border);
		jPanel_Right_Bottom.setBackground(Color.yellow);
		jPanel_Right_Bottom.setPreferredSize(new Dimension(0, rightButtomPane_Height));
		// right top
		jPanel_Right_Top.setLayout(new java.awt.BorderLayout(15, 10));
		jPanel_Right_Top.setBorder(border);
		// jPanel_Right_Top.setBackground(Color.green);
		// add to right panel
		jPanel_Right.add(this.jPanel_Right_Top, java.awt.BorderLayout.CENTER);
		jPanel_Right.add(this.jPanel_Right_Bottom, java.awt.BorderLayout.SOUTH);
		// add right to frame
		getContentPane().add(jPanel_Right, java.awt.BorderLayout.CENTER);

		// menu
		jMenuBar1 = new JMenuBar();

		// Implement menu_File
		jMenu_File = new JMenu();
		jMenu_File.setMnemonic('f');
		jMenu_File.setText("File");

		jMenuItem_File_OpenMapFile = new JMenuItem();
		jMenuItem_File_OpenMapFile.setMnemonic('o');
		jMenuItem_File_OpenMapFile.setText("Open Map File");
		jMenuItem_File_OpenMapFile.setActionCommand("openMap");
		jMenuItem_File_OpenMapFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				menuClickHandle(e);
			}
		});

		jMenu_File.add(jMenuItem_File_OpenMapFile);

		jMenuItem_File_SaveMapFile = new JMenuItem();
		jMenuItem_File_SaveMapFile.setMnemonic('s');
		jMenuItem_File_SaveMapFile.setText("Save Map File");
		jMenuItem_File_SaveMapFile.setActionCommand("saveMap");
		jMenuItem_File_SaveMapFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				menuClickHandle(e);
			}
		});

		jMenu_File.add(jMenuItem_File_SaveMapFile);
		// Separator
		jMenu_File.add(new JPopupMenu.Separator());

		// the exit
		jMenuItem_File_Exit = new JMenuItem();
		jMenuItem_File_Exit.setMnemonic('e');
		jMenuItem_File_Exit.setText("Exit");
		jMenuItem_File_Exit.setActionCommand("exit");
		jMenuItem_File_Exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				menuClickHandle(e);
			}
		});
		jMenu_File.add(jMenuItem_File_Exit);

		jMenuBar1.add(jMenu_File);

		// Edit Robot Menu

		jMenu_Robot = new JMenu();
		jMenu_Robot.setText("Robot");
		jMenu_Robot.setMnemonic('r');
		jMenuBar1.add(jMenu_Robot);

		editRobot = new JMenuItem();
		editRobot.setText("Edit");
		editRobot.setMnemonic('e');
		jMenu_Robot.add(editRobot);
		editRobot.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				menuClickHandle(e);
			}
		});

		upDateDuration = new JMenuItem();
		upDateDuration.setText("Duration time");
		upDateDuration.setMnemonic('D');
		jMenu_Robot.add(upDateDuration);
		upDateDuration.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int duration = 300;
				String inputValue = JOptionPane.showInputDialog("Enter duration value:", null);

				try {
					duration = Integer.parseInt(inputValue);
					if (duration < 300 || duration < 1 || duration > 5000) {
						duration = 300;
					}
				} catch (Exception ex) {
					duration = 300;
					JOptionPane.showMessageDialog(null, "the input is wrong");
				}

				String para[] = new String[1];
				para[0] = String.valueOf(duration);
				CoreController.getRobotController().sendCmdtoRobot(Command.SET_CHECK_STATUS_DURATION, para);
				return;

			}
		});

		// help
		jMenu_Help = new JMenu();
		jMenu_Help.setMnemonic('h');
		jMenu_Help.setText("Help");

		jMenuItem_Help_AboutBox = new JMenuItem();
		jMenuItem_Help_AboutBox.setMnemonic('a');
		jMenuItem_Help_AboutBox.setText("About");
		jMenu_Help.add(jMenuItem_Help_AboutBox);
		jMenuItem_Help_AboutBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				menuClickHandle(e);
			}
		});

		jMenuItem_Help_HelpContent = new JMenuItem();
		jMenuItem_Help_HelpContent.setMnemonic('c');
		jMenuItem_Help_HelpContent.setText("User Manual");
		jMenu_Help.add(jMenuItem_Help_HelpContent);
		jMenuItem_Help_HelpContent.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// open user manual pdf file
				try {
					java.awt.Desktop.getDesktop().open(new java.io.File("./UserManual.pdf"));
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(null, "Cannot open user manual file");
				}
			}
		});

		jMenuBar1.add(jMenu_Help);

		setJMenuBar(jMenuBar1);
		pack();
	}

	/**
	 * handle the window closing
	 */
	private void winClosingHandel() {
		int opt = JOptionPane.OK_CANCEL_OPTION;
		int type = JOptionPane.QUESTION_MESSAGE;
		String msg = "Are you going to exit the application?";
		String title = "Application Closing";
		int rev = JOptionPane.showConfirmDialog(this, msg, title, opt, type);
		if (rev == 0) {
			System.exit(0);
		}
	}

	/**
	 * handle the menu click
	 * 
	 * @param the Action Event e
	 */
	private void menuClickHandle(ActionEvent e) {
		String command = e.getActionCommand();

		// Handle Exit
		if (command.equals("exit")) {
			winClosingHandel();
			return;
		}

		// Handle Open Map File
		if (command.equals("openMap")) {
			this.mainFormController.LoadMap();
			return;
		}
		// Handle Save Map File
		if (command.equals("saveMap")) {
			this.mainFormController.saveMap();
			return;
		}
		if (command.equals("Edit")) {
			CoreController.getRobotEditSetting().popUp();
		}
	}
}
