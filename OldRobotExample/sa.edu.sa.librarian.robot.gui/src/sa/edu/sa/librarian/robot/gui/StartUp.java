package sa.edu.sa.librarian.robot.gui;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import sa.edu.sa.librarian.robot.gui.controller.CoreController;

public class StartUp {

	/**
	 * The width of Application's left Panel
	 */
	public static final int Application_Panel_Left_Width = 260;
	/**
	 * the left panel height
	 */
	public static final int Application_Panel_Left_Buttom_Height = 280;
	/**
	 * the Right Buttom panel Height
	 */
	public static final int Application_Panel_Right_Buttom_Height = 160;

	private static final String ApplicationName = "Librarian Robot";
	private static final String SEP = System.getProperty("file.separator");
	private static final String USER_HOME = System.getProperty("user.home");
	private static String cacheFilePath = USER_HOME + SEP + "app.cache";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		chkApplicationRunning();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					// JDialog.setDefaultLookAndFeelDecorated(true);
					// JFrame.setDefaultLookAndFeelDecorated(true);
					// look and feel
					// UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
					// create runtime
					CoreController.createRunTime();
				} catch (Exception e) {
					e.printStackTrace();
					// System.exit(1);
				}

			}
		});

	}

	/**
	 * check the application running application will exit if it is already running
	 */
	private static void chkApplicationRunning() {
		RandomAccessFile raf = null;
		try {
			File catchFile = new File(cacheFilePath);

			if (catchFile == null || !catchFile.exists()) {
				catchFile.createNewFile();
			}
			// access the File
			raf = new RandomAccessFile(cacheFilePath, "rwd");

			FileChannel lockfc = raf.getChannel();
			FileLock flock = lockfc.tryLock();
			raf.writeBytes(ApplicationName);
			if (flock == null)
				throw new Exception();
			raf.close();
		} catch (Exception ex) {
			String msg = "Application is already running!\n" + ex.getMessage();
			JOptionPane.showMessageDialog(null, msg, "Running Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

}
