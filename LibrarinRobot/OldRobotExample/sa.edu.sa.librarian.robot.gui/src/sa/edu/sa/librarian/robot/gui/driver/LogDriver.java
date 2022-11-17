package sa.edu.sa.librarian.robot.gui.driver;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.model.LogMessage;

public class LogDriver {

	/**
	 * add log to system
	 * 
	 * @param message
	 */
	public static void addLog_System(String message) {
		LogMessage logMessage = new LogMessage();
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		// the display font style and color
		StyleConstants.setFontFamily(simpleAttributeSet, "SansSerif");
		StyleConstants.setFontSize(simpleAttributeSet, 14);
		StyleConstants.setBold(simpleAttributeSet, true);
		StyleConstants.setForeground(simpleAttributeSet, Color.BLACK);

		// create the instance of the log message
		logMessage.setType(LogMessage.TYPE_SYSTEM);
		logMessage.setContent(message + "\r\n");
		logMessage.setSimpleAttributeSet(simpleAttributeSet);
		// add the log message to the window
		CoreController.getLogWindow().newLog(logMessage);
	}

	/**
	 * add error log to system
	 * 
	 * @param message
	 */
	public static void addLog_System_Error(String message) {
		LogMessage logMessage = new LogMessage();
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		// the display font style and color
		StyleConstants.setFontFamily(simpleAttributeSet, "SansSerif");
		StyleConstants.setFontSize(simpleAttributeSet, 14);
		StyleConstants.setBold(simpleAttributeSet, true);
		StyleConstants.setForeground(simpleAttributeSet, Color.red);

		// create the instance of the log message
		logMessage.setType(LogMessage.TYPE_SYSTEM);
		logMessage.setContent(message + "\r\n");
		logMessage.setSimpleAttributeSet(simpleAttributeSet);
		// add the log message to the window
		CoreController.getLogWindow().newLog(logMessage);
	}

	/**
	 * add log to system
	 * 
	 * @param message
	 */
	public static void addLog_Map(String message) {
		LogMessage logMessage = new LogMessage();
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		// the display font style and color
		StyleConstants.setFontFamily(simpleAttributeSet, "SansSerif");
		StyleConstants.setFontSize(simpleAttributeSet, 14);
		StyleConstants.setBold(simpleAttributeSet, true);
		StyleConstants.setForeground(simpleAttributeSet, Color.BLACK);

		// create the instance of the log message
		logMessage.setType(LogMessage.TYPE_MAP);
		logMessage.setContent(message + "\r\n");
		logMessage.setSimpleAttributeSet(simpleAttributeSet);
		// add the log message to the window
		CoreController.getLogWindow().newLog(logMessage);
	}

	/**
	 * add error log to system
	 * 
	 * @param message
	 */
	public static void addLog_Map_Error(String message) {
		LogMessage logMessage = new LogMessage();
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		// the display font style and color
		StyleConstants.setFontFamily(simpleAttributeSet, "SansSerif");
		StyleConstants.setFontSize(simpleAttributeSet, 14);
		StyleConstants.setBold(simpleAttributeSet, true);
		StyleConstants.setForeground(simpleAttributeSet, Color.red);

		// create the instance of the log message
		logMessage.setType(LogMessage.TYPE_MAP);
		logMessage.setContent(message + "\r\n");
		logMessage.setSimpleAttributeSet(simpleAttributeSet);
		// add the log message to the window
		CoreController.getLogWindow().newLog(logMessage);
	}

	/**
	 * add log to system
	 * 
	 * @param message
	 */
	public static void addLog_Robot(String message) {
		LogMessage logMessage = new LogMessage();
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		// the display font style and color
		StyleConstants.setFontFamily(simpleAttributeSet, "SansSerif");
		StyleConstants.setFontSize(simpleAttributeSet, 14);
		StyleConstants.setBold(simpleAttributeSet, true);
		StyleConstants.setForeground(simpleAttributeSet, Color.BLACK);

		// create the instance of the log message
		logMessage.setType(LogMessage.TYPE_ROBOT);
		logMessage.setContent(message + "\r\n");
		logMessage.setSimpleAttributeSet(simpleAttributeSet);
		// add the log message to the window
		CoreController.getLogWindow().newLog(logMessage);
	}

	/**
	 * add error log to system
	 * 
	 * @param message
	 */
	public static void addLog_Robot_Error(String message) {
		LogMessage logMessage = new LogMessage();
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		// the display font style and color
		StyleConstants.setFontFamily(simpleAttributeSet, "SansSerif");
		StyleConstants.setFontSize(simpleAttributeSet, 14);
		StyleConstants.setBold(simpleAttributeSet, true);
		StyleConstants.setForeground(simpleAttributeSet, Color.red);

		// create the instance of the log message
		logMessage.setType(LogMessage.TYPE_ROBOT);
		logMessage.setContent(message + "\r\n");
		logMessage.setSimpleAttributeSet(simpleAttributeSet);
		// add the log message to the window
		CoreController.getLogWindow().newLog(logMessage);
	}
}
