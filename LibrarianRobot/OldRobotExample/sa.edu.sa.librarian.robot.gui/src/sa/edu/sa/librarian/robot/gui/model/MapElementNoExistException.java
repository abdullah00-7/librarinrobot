package sa.edu.sa.librarian.robot.gui.model;

/**
 * The Map element not Exist Exception
 * 
 */
public class MapElementNoExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param cause
	 */
	public MapElementNoExistException(Throwable cause) {
		super(cause);
	}

	/**
	 * constructor
	 * 
	 * @param message
	 * @param cause
	 */
	public MapElementNoExistException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * constructor
	 * 
	 * @param message
	 */
	public MapElementNoExistException(String message) {
		super(message);
	}

	/**
	 * default constructor
	 */
	public MapElementNoExistException() {
	}
}
