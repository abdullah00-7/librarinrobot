package sa.edu.sa.librarian.robot.gui.view;

import javax.swing.*;

import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.controller.RobotController;
import sa.edu.sa.librarian.robot.gui.model.Command;

/**
 * the Robot Controller panel
 */
public class RobotControlFrame extends JPanel implements IWindow {

	private static final long serialVersionUID = 1L;
	private JButton jBtn_AutoRun;
	private JButton jBtn_Bwd;
	private JButton jBtn_ChangeModel;
	private JButton jBtn_Connect;
	private JButton jBtn_Fwd;
	private JButton jBtn_L_Turn;
	private JButton jBtn_R_Turn;
	private JButton jBtn_Stop;
	// labels for status diplay
	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel angleLabel;
	private JLabel speedLabel;
	private JLabel turnLabel;
	private JLabel sensorLabel;
	private JLabel lightLabel;
	private JLabel signalLabel;
	private JLabel batteryLabel;
	private JLabel soundLabel;
	private JLabel durationLabel;
	private JLabel jLb_RbInfo;
	private JPanel jPanel_Center;
	private JPanel jPanel_Top;
	// controller
	private RobotController robotController;

	/**
	 * the default constructor
	 */
	public RobotControlFrame() {
		initComponents();
	}

	@Override
	public void init() {
		switchLayerOut(1);
		this.robotController = CoreController.getRobotController();
	}

	private void initComponents() {
		jPanel_Top = new JPanel();
		jPanel_Center = new JPanel();

		jBtn_Connect = new JButton();
		jBtn_ChangeModel = new JButton();
		jBtn_AutoRun = new JButton();
		jBtn_Bwd = new JButton();
		jBtn_R_Turn = new JButton();
		jBtn_Fwd = new JButton();
		jBtn_L_Turn = new JButton();
		jBtn_Stop = new JButton();

		jLb_RbInfo = new JLabel("Robot Status ");
		speedLabel = new JLabel();
		angleLabel = new JLabel();
		batteryLabel = new JLabel();
		signalLabel = new JLabel();
		lightLabel = new JLabel();
		xLabel = new JLabel();
		yLabel = new JLabel();
		turnLabel = new JLabel();
		sensorLabel = new JLabel();
		soundLabel = new JLabel();
		durationLabel = new JLabel();

		setLayout(new java.awt.BorderLayout());
		jPanel_Top.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel_Top.setPreferredSize(new java.awt.Dimension(150, 50));

		jBtn_Connect.setText("Connect");
		jPanel_Top.add(jBtn_Connect);

		jBtn_ChangeModel.setText("Mode");

		jPanel_Top.add(jBtn_ChangeModel);

		add(jPanel_Top, java.awt.BorderLayout.PAGE_START);

		jPanel_Center.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel_Center.setLayout(null);

		jBtn_AutoRun.setIcon(new ImageIcon("images/play.png")); // NOI18N
		jPanel_Center.add(jBtn_AutoRun);
		jBtn_AutoRun.setBounds(10, 10, 70, 70);

		jBtn_Bwd.setIcon(new ImageIcon("images/back.png")); // NOI18N
		jPanel_Center.add(jBtn_Bwd);
		jBtn_Bwd.setBounds(90, 170, 70, 60);

		jBtn_R_Turn.setIcon(new ImageIcon("images/right_R.png")); // NOI18N
		jPanel_Center.add(jBtn_R_Turn);
		jBtn_R_Turn.setBounds(170, 90, 60, 60);

		jBtn_Fwd.setIcon(new ImageIcon("images/forward.png")); // NOI18N

		jPanel_Center.add(jBtn_Fwd);
		jBtn_Fwd.setBounds(90, 10, 70, 60);

		jBtn_L_Turn.setIcon(new ImageIcon("images/left_R.png")); // NOI18N

		jPanel_Center.add(jBtn_L_Turn);
		jBtn_L_Turn.setBounds(20, 90, 60, 60);

		jBtn_Stop.setIcon(new ImageIcon("images/stop.png")); // NOI18N
		jPanel_Center.add(jBtn_Stop);
		jBtn_Stop.setBounds(90, 90, 70, 70);

		// create labels for display
		xLabel.setText("X: ");
		yLabel.setText("Y: ");
		angleLabel.setText("Direction: ");
		speedLabel.setText("Speed: ");
		turnLabel.setText("Turning Speed ");
		sensorLabel.setText("Sensor Speed ");
		lightLabel.setText("Light Value: ");
		signalLabel.setText("Signal Value");
		batteryLabel.setText("Battery Value: ");
		soundLabel.setText("Sound Volume: ");
		durationLabel.setText("Duration Time Value: ");

		jPanel_Center.add(jLb_RbInfo);
		jPanel_Center.add(xLabel);
		jPanel_Center.add(yLabel);
		jPanel_Center.add(angleLabel);
		jPanel_Center.add(speedLabel);
		jPanel_Center.add(turnLabel);
		jPanel_Center.add(sensorLabel);
		jPanel_Center.add(lightLabel);
		jPanel_Center.add(signalLabel);
		jPanel_Center.add(batteryLabel);
		jPanel_Center.add(soundLabel);
		jPanel_Center.add(durationLabel);
		// the information of the robot
		jLb_RbInfo.setBounds(10, 245, 250, 15);
		xLabel.setBounds(10, 270, 250, 20);
		yLabel.setBounds(10, 290, 250, 20);
		angleLabel.setBounds(10, 310, 250, 20);
		speedLabel.setBounds(10, 330, 250, 20);
		turnLabel.setBounds(10, 350, 250, 20);
		sensorLabel.setBounds(10, 370, 250, 20);
		lightLabel.setBounds(10, 390, 250, 20);
		signalLabel.setBounds(10, 410, 250, 20);
		batteryLabel.setBounds(10, 430, 250, 20);
		soundLabel.setBounds(10, 450, 250, 20);
		durationLabel.setBounds(10, 470, 250, 20);

		add(jPanel_Center, java.awt.BorderLayout.CENTER);

		// add listener connect button
		jBtn_Connect.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				keyEventHandle(evt);
			}
		});

		jBtn_Stop.setActionCommand("STOP");
		jBtn_Stop.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				keyEventHandle(evt);
			}
		});

		jBtn_AutoRun.setActionCommand("AutoRun");
		jBtn_AutoRun.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				keyEventHandle(evt);
			}
		});

		// add mouse listener fwd button
		jBtn_Fwd.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				sentCommad(Command.FWD, null);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				sentCommad(Command.STOP, null);
			}
		});

		// add mouse listener bwd button
		this.jBtn_Bwd.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				sentCommad(Command.BWD, null);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				sentCommad(Command.STOP, null);
			}
		});

		// add mouse listener L turn button
		this.jBtn_L_Turn.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				sentCommad(Command.LTN, null);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				sentCommad(Command.STOP, null);
			}
		});

		// add mouse listener R Turn button
		this.jBtn_R_Turn.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				sentCommad(Command.RTN, null);

			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				sentCommad(Command.STOP, null);

			}
		});
	}

	/**
	 * handle the key connect status and etc.
	 * 
	 * @param evt
	 */
	private void keyEventHandle(java.awt.event.ActionEvent evt) {
		String strCommand = evt.getActionCommand();
		if (strCommand.equals("connect")) {
			CoreController.getNxtDriver().search();
			CoreController.getRobotConnWin().init();
			CoreController.getNxtDriver().search();
			CoreController.getRobotConnWin().fillTable(CoreController.getNxtDriver().getConnTableModel());
			CoreController.getRobotConnWin().setVisible(true);
		}

		if (strCommand.equals("STOP")) {
			sentCommad(Command.EMERGENCY_STOP, null);
		}

		if (strCommand.equals("disconnect")) {
			CoreController.getRobotController().disConnect();
		}
		// do the Auto run
		if (strCommand.equals("AutoRun")) {
			this.robotController.autoRun();
		}
	}

	/**
	 *
	 * @param model
	 */
	public void switchLayerOut(int model) {
		switch (model) {
		case 1: {
			this.jBtn_Connect.setText("connect");
			this.jBtn_Connect.setActionCommand("connect");
			this.jBtn_ChangeModel.setVisible(false);
			this.jPanel_Center.setVisible(false);
			break;
		}
		// the default manual control
		case 2: {
			this.jBtn_Connect.setText("Disconnect");
			this.jBtn_Connect.setActionCommand("disconnect");
			this.jBtn_Fwd.setActionCommand("fwd");
			this.jPanel_Center.setVisible(true);
			break;
		}
		case 3: {
			break;
		}

		case 4: {
			break;
		}
		default: {
		}
		}
	}

	/**
	 * send command with parameter to the controller
	 * 
	 * @param cmd
	 * @param Parameter
	 */
	protected void sentCommad(int cmd, String[] Parameter) {
		this.robotController.sendCmdtoRobot(cmd, Parameter);
	}

	/**
	 * Update information value parameters are following this order
	 * 0)[robot_x],1)[robot_y],2)[robot_heading] 3)[robotMovementSpeed],4)[turning
	 * speed],5)swipperSpeed,6)Light value 7)[signal
	 * strength],8)[battery],9)[Sound],10)[duration]
	 * 
	 * @param info the information
	 */
	public void updateRobotInfo(String[] info) {

		// System.out.println(Arrays.toString(info));
		float value;

		value = Float.parseFloat(info[0]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		xLabel.setText(String.format("X: %.3f", value));

		value = Float.parseFloat(info[1]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		yLabel.setText(String.format("Y: %.3f", value));

		value = Float.parseFloat(info[2]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		angleLabel.setText(String.format("Direction: %.3f", value));

		value = Float.parseFloat(info[3]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		speedLabel.setText(String.format("Robot Speed: %d/100", (int) value));

		value = Float.parseFloat(info[4]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		turnLabel.setText(String.format("Turning Speed: %d/100", (int) value));

		value = Float.parseFloat(info[5]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		sensorLabel.setText(String.format("Sensor Speed: %.3f/900", value));

		value = Float.parseFloat(info[6]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		lightLabel.setText(String.format("Light Value: %d/100", (int) value));

		value = Float.parseFloat(info[7]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		signalLabel.setText(String.format("Signal: %d/255", (int) value));

		int value2 = (int) Float.parseFloat(info[8]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		batteryLabel.setText(String.format("Battery: %d/9000", value2));

		value = Float.parseFloat(info[9]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		soundLabel.setText(String.format("Sound: %d/100", (int) value));

		value = Float.parseFloat(info[10]);
		// value = AppRunTime.round(value, 3, java.math.BigDecimal.ROUND_HALF_UP);
		durationLabel.setText(String.format("Duration Time: %d ms", (int) value));
	}
}
