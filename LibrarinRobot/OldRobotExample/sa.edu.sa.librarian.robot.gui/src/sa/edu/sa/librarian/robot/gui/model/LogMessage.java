package sa.edu.sa.librarian.robot.gui.model;

import javax.swing.text.SimpleAttributeSet;

/**
 * The Log Message to show in the output panel
 */
public class LogMessage {

	/**
	 * Identify the log is general system message
	 */
	public static final int TYPE_SYSTEM = 100;
	/**
	 * Identify the log related to Robot
	 */
	public static final int TYPE_ROBOT = 200;
	/**
	 * Identify the log related to Map
	 */
	public static final int TYPE_MAP = 300;
	// the type of the LogMessage
	private int type = -1;
	// the stye for showing the text
	private SimpleAttributeSet simpleAttributeSet = null;
	// the content of the Message
	private String content = null;

	/**
	 * return the content of the message
	 * 
	 * @return the content of the message
	 */
	public String getContent() {
		return content;
	}

	/**
	 * set the content of the message
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * return the attribute set
	 * 
	 * @return attribute set
	 */
	public SimpleAttributeSet getSimpleAttributeSet() {
		return simpleAttributeSet;
	}

	/**
	 * set the attribute set
	 * 
	 * @param simpleAttributeSet
	 */
	public void setSimpleAttributeSet(SimpleAttributeSet simpleAttributeSet) {
		this.simpleAttributeSet = simpleAttributeSet;
	}

	/**
	 * return the type
	 * 
	 * @return type of the log message
	 */
	public int getType() {
		return type;
	}

	/**
	 * set the the type
	 * 
	 * @param type type of the log message
	 */
	public void setType(int type) {
		this.type = type;
	}
}
