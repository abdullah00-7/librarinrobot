package sa.edu.sa.librarian.robot.gui.view;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.text.StyledDocument;

import sa.edu.sa.librarian.robot.gui.model.LogMessage;

/**
 * the log window showing in the application
 */
public class LogWindow extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel_MapEditOutPut;
	private JPanel jPanel_RobotControlOutPut;
	private JPanel jPanel_SystemOutPut;
	private JScrollPane jScrollPane_MapEditingOutPut;
	private JScrollPane jScrollPane_RobotControlOutPut;
	private JScrollPane jScrollPane_SysOutPut;
	private JTabbedPane jTabbedPane_OutPuts;
	private JTextPane jTextPane_MapEditingOutPut;
	private JTextPane jTextPane_RobotControlOutPut;
	private JTextPane jTextPane_SystemOutPut;
	// add the log document
	private StyledDocument doc_SysOutPut;
	private StyledDocument doc_RobotOutPut;
	private StyledDocument doc_MapOutPut;

	/**
	 * the default Constructor
	 */
	public LogWindow() {
		initComponents();
		doc_SysOutPut = this.jTextPane_SystemOutPut.getStyledDocument();
		doc_RobotOutPut = this.jTextPane_RobotControlOutPut.getStyledDocument();
		doc_MapOutPut = this.jTextPane_MapEditingOutPut.getStyledDocument();
		this.jTextPane_RobotControlOutPut.setEditable(true);
	}

	private void initComponents() {

		jTabbedPane_OutPuts = new JTabbedPane();
		jPanel_SystemOutPut = new JPanel();
		jScrollPane_SysOutPut = new JScrollPane();
		jTextPane_SystemOutPut = new JTextPane();
		jPanel_MapEditOutPut = new JPanel();
		jScrollPane_MapEditingOutPut = new JScrollPane();
		jTextPane_MapEditingOutPut = new JTextPane();
		jPanel_RobotControlOutPut = new JPanel();
		jScrollPane_RobotControlOutPut = new JScrollPane();
		jTextPane_RobotControlOutPut = new JTextPane();

		setLayout(new BorderLayout());

		jTabbedPane_OutPuts.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		jTabbedPane_OutPuts.setTabPlacement(JTabbedPane.BOTTOM);

		jPanel_SystemOutPut.setLayout(new BorderLayout());
		jTextPane_SystemOutPut.setBackground(new java.awt.Color(204, 255, 255));
		jScrollPane_SysOutPut.setViewportView(jTextPane_SystemOutPut);
		jPanel_SystemOutPut.add(jScrollPane_SysOutPut, BorderLayout.CENTER);
		jTabbedPane_OutPuts.addTab("Application Output", jPanel_SystemOutPut);
		jTextPane_SystemOutPut.setEditable(false);

		jPanel_MapEditOutPut.setLayout(new BorderLayout());
		jTextPane_MapEditingOutPut.setBackground(new java.awt.Color(204, 255, 204));
		jScrollPane_MapEditingOutPut.setViewportView(jTextPane_MapEditingOutPut);
		jPanel_MapEditOutPut.add(jScrollPane_MapEditingOutPut, BorderLayout.CENTER);
		jTabbedPane_OutPuts.addTab("Map Editing  OutPut", jPanel_MapEditOutPut);
		jTextPane_MapEditingOutPut.setEditable(false);

		jPanel_RobotControlOutPut.setLayout(new BorderLayout());
		jTextPane_RobotControlOutPut.setBackground(new java.awt.Color(255, 255, 204));
		jScrollPane_RobotControlOutPut.setViewportView(jTextPane_RobotControlOutPut);
		jPanel_RobotControlOutPut.add(jScrollPane_RobotControlOutPut, BorderLayout.CENTER);
		jTabbedPane_OutPuts.addTab("Robot Control OutPut", jPanel_RobotControlOutPut);
		jTextPane_RobotControlOutPut.setEnabled(false);

		add(jTabbedPane_OutPuts, BorderLayout.CENTER);
	}

	/**
	 * add the log message
	 * 
	 * @param lm
	 */
	public void newLog(LogMessage lm) {
		try {

			if (lm.getType() == LogMessage.TYPE_SYSTEM) {
				this.doc_SysOutPut.insertString(this.doc_SysOutPut.getLength(), lm.getContent(),
						lm.getSimpleAttributeSet());
				JScrollBar bar1 = this.jScrollPane_SysOutPut.getVerticalScrollBar();
				bar1.setValue(bar1.getMaximum());
			}

			// add the map log
			if (lm.getType() == LogMessage.TYPE_MAP) {
				this.doc_MapOutPut.insertString(this.doc_MapOutPut.getLength(), lm.getContent(),
						lm.getSimpleAttributeSet());
				JScrollBar bar = this.jScrollPane_MapEditingOutPut.getVerticalScrollBar();
				bar.setValue(bar.getMaximum());
				return;
			}

			// add the robot log
			if (lm.getType() == LogMessage.TYPE_ROBOT) {
				this.doc_RobotOutPut.insertString(this.doc_RobotOutPut.getLength(), lm.getContent(),
						lm.getSimpleAttributeSet());
				JScrollBar bar = this.jScrollPane_RobotControlOutPut.getVerticalScrollBar();
				bar.setValue(bar.getMaximum());
				return;
			}
		} catch (Exception e) {
		}

	}
}
