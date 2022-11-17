package sa.edu.sa.librarian.robot.gui.view;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.model.Command;

/**
 * the class for reset the configuration of the robot
 */
public class RobotConfigWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JButton jBtn_Ok;
	private JTextField directionValue;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JLabel jLabel7;
	private JLabel jLabel8;
	private JLabel jLabel9;
	private JTextField lightValue;
	private JPanel mainPanel;
	private JPanel robotMovementPanel;
	private JPanel sensorPanel;
	private JSlider sensorSpeed;
	private JButton setMovementButton;
	private JButton setSensorButton;
	private JSlider speedController;
	private JSlider turningController;
	private JTextField xValue;
	private JTextField yValue;
	private JCheckBox sensorEnable;
	private JCheckBox sensorSwippingEnable;
	private JLabel sensorSpeedLabel;
	private JLabel speedControllerLabel;
	private JLabel turningControllerLabel;

	/**
	 * the constructor of the editDialig
	 * 
	 * @param owner
	 * @param modal
	 */
	public RobotConfigWindow(Frame owner, boolean modal) {
		super(owner, modal);
		this.initComponents();
		setVisible(false);
	}

	/**
	 * initialize the component
	 */
	private void initComponents() {
		sensorEnable = new javax.swing.JCheckBox();
		sensorSwippingEnable = new javax.swing.JCheckBox();
		mainPanel = new javax.swing.JPanel();
		robotMovementPanel = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		xValue = new javax.swing.JTextField("0");
		directionValue = new javax.swing.JTextField("0");
		yValue = new javax.swing.JTextField("0");
		setMovementButton = new javax.swing.JButton();
		speedController = new javax.swing.JSlider();
		turningController = new javax.swing.JSlider();
		sensorPanel = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		lightValue = new javax.swing.JTextField("1");
		sensorSpeed = new javax.swing.JSlider();
		setSensorButton = new javax.swing.JButton();
		jBtn_Ok = new javax.swing.JButton();
		sensorSpeedLabel = new JLabel();
		speedControllerLabel = new JLabel();
		turningControllerLabel = new JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		mainPanel.setLayout(null);

		robotMovementPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		robotMovementPanel.setLayout(null);

		jLabel1.setText("Robot Movement Configuration");
		robotMovementPanel.add(jLabel1);
		jLabel1.setBounds(15, 1, 250, 17);

		jLabel3.setText("X:");
		robotMovementPanel.add(jLabel3);
		jLabel3.setBounds(15, 47, 20, 17);

		jLabel4.setText("Y:");
		robotMovementPanel.add(jLabel4);
		jLabel4.setBounds(15, 80, 20, 17);

		jLabel5.setText("Direction");
		robotMovementPanel.add(jLabel5);
		jLabel5.setBounds(15, 113, 80, 17);

		jLabel6.setText("Robot Speed");
		robotMovementPanel.add(jLabel6);
		jLabel6.setBounds(245, 49, 100, 17);

		jLabel7.setText("Turning Speed");
		robotMovementPanel.add(jLabel7);
		jLabel7.setBounds(245, 115, 100, 17);

		robotMovementPanel.add(xValue);
		xValue.setBounds(40, 42, 50, 27);
		robotMovementPanel.add(yValue);
		yValue.setBounds(40, 75, 50, 27);
		robotMovementPanel.add(directionValue);
		directionValue.setBounds(82, 108, 50, 27);

		setMovementButton.setText("Set");
		setMovementButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (xValue.getText().length() == 0 || yValue.getText().length() == 0
						|| directionValue.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "Enter valid values (no more than three digits)", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (xValue.getText().length() > 6 || yValue.getText().length() > 6
						|| directionValue.getText().length() == 6) {
					JOptionPane.showMessageDialog(null, "Enter valid values (no more than three digits)", "Error",
							JOptionPane.ERROR_MESSAGE);

					return;
				}
				String[] para = new String[3];
				para[0] = String.valueOf(xValue.getText());
				para[1] = String.valueOf(yValue.getText());
				para[2] = String.valueOf(directionValue.getText());
				CoreController.getRobotController().sendCmdtoRobot(Command.SET_MOVE_POSITION, para);
			}
		});

		robotMovementPanel.add(setMovementButton);
		setMovementButton.setBounds(82, 141, 129, 29);

		speedController.setMaximum(100);
		speedController.setMinimum(0);
		speedController.setValue(5);
		speedControllerLabel.setText("10");
		speedController.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				String[] para = new String[1];
				para[0] = String.valueOf(speedController.getValue());
				float value = Float.parseFloat(para[0]);
				speedControllerLabel.setText(String.format("%d", (int) value));
				CoreController.getRobotController().sendCmdtoRobot(Command.SET_MOVE_MOVESPEED, para);
			}
		});
		robotMovementPanel.add(speedController);
		speedController.setBounds(345, 20, 200, 39);
		robotMovementPanel.add(speedControllerLabel);
		speedControllerLabel.setBounds(345, 50, 150, 20);

		turningController.setMaximum(100);
		turningController.setMinimum(20);
		turningController.setValue(20);
		turningControllerLabel.setText("20");
		turningController.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				String[] para = new String[1];
				para[0] = String.valueOf(turningController.getValue());
				float value = Float.parseFloat(para[0]);
				turningControllerLabel.setText(String.format("%d", (int) value));
				CoreController.getRobotController().sendCmdtoRobot(Command.SET_MOVE_TURNSPEED, para);
			}
		});
		robotMovementPanel.add(turningController);
		turningController.setBounds(349, 86, 200, 39);
		robotMovementPanel.add(turningControllerLabel);
		turningControllerLabel.setBounds(349, 130, 150, 20);

		mainPanel.add(robotMovementPanel);
		robotMovementPanel.setBounds(12, 0, 586, 183);

		sensorPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		sensorPanel.setLayout(null);

		jLabel2.setText("Sensor Configuration");
		sensorPanel.add(jLabel2);
		jLabel2.setBounds(13, 1, 200, 17);

		jLabel8.setText("Light Value");
		sensorPanel.add(jLabel8);
		jLabel8.setBounds(13, 42, 80, 17);

		jLabel9.setText("Sensor Speed");
		sensorPanel.add(jLabel9);
		jLabel9.setBounds(13, 93, 100, 17);
		sensorPanel.add(lightValue);
		lightValue.setBounds(96, 37, 50, 27);

		sensorSpeed.setMaximum(900);
		sensorSpeed.setMinimum(0);
		sensorSpeed.setValue(100);
		sensorSpeedLabel.setText("100");
		sensorSpeed.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				String[] para = new String[1];
				para[0] = String.valueOf(sensorSpeed.getValue());
				float value = Float.parseFloat(para[0]);
				sensorSpeedLabel.setText(String.format("%.2f", value));
				CoreController.getRobotController().sendCmdtoRobot(Command.SET_SENSOR_SWIPER_SPEED, para);
			}
		});
		sensorPanel.add(sensorSpeed);
		sensorSpeed.setBounds(121, 77, 200, 39);
		sensorPanel.add(sensorSpeedLabel);
		sensorSpeedLabel.setBounds(340, 80, 100, 20);

		setSensorButton.setText("Set");
		setSensorButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (lightValue.getText().length() > 10 || lightValue.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "Enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				String[] para = new String[1];
				para[0] = lightValue.getText();
				CoreController.getRobotController().sendCmdtoRobot(Command.SET_SENSOR_DETECT_LIGHT_VALUE, para);
			}
		});

		sensorPanel.add(setSensorButton);
		setSensorButton.setBounds(165, 36, 113, 29);

		sensorEnable.setSelected(false);
		sensorEnable.setText("Enable Sensor");
		sensorPanel.add(sensorEnable);
		sensorEnable.setBounds(290, 36, 130, 29);
		sensorEnable.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String[] para = new String[1];
				if (sensorEnable.isSelected()) {
					// send command to activate sensor
					CoreController.getRobotController().sendCmdtoRobot(Command.SET_SENSOR_ON, para);
				} else {
					// disable sensor
					CoreController.getRobotController().sendCmdtoRobot(Command.SET_SENSOR_OFF, para);
				}

			}
		});

		sensorSwippingEnable.setSelected(false);
		sensorSwippingEnable.setText("Swipping Enable");
		sensorPanel.add(sensorSwippingEnable);
		sensorSwippingEnable.setBounds(420, 36, 150, 29);
		sensorSwippingEnable.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String[] para = new String[1];
				if (sensorSwippingEnable.isSelected()) {
					// enable swipping and sensor and sent commands
					CoreController.getRobotController().sendCmdtoRobot(Command.SET_SWIPER_ON, para);
				} else {
					// dispble swopping and sent commands to robot
					CoreController.getRobotController().sendCmdtoRobot(Command.SET_SWIPER_OFF, para);
				}
			}
		});

		mainPanel.add(sensorPanel);
		sensorPanel.setBounds(12, 216, 586, 139);

		jBtn_Ok.setText("OK");
		jBtn_Ok.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancleButtonActionPerformed(evt);
			}
		});
		mainPanel.add(jBtn_Ok);
		jBtn_Ok.setBounds(493, 364, 93, 29);

		getContentPane().add(mainPanel);
		mainPanel.setBounds(12, 12, 598, 405);

		pack();
	}

	/**
	 * when click the OK button
	 * 
	 * @param evt
	 */
	private void cancleButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.setVisible(false);
	}

	/**
	 * pop up the
	 */
	public void popUp() {
		this.setBounds(300, 300, 640, 480);
		this.setVisible(true);
	}
}
