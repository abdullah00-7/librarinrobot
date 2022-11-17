package sa.edu.sa.librarian.robot.gui.view;

import javax.swing.JPanel;
import sa.edu.sa.librarian.robot.gui.controller.CoreController;
import sa.edu.sa.librarian.robot.gui.controller.MapController;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * this panel view for showing the map, using double buffered drawing
 */
public class MapWindow extends JPanel implements IWindow {

	private static final long serialVersionUID = 1485909830002633539L;
	// the Controller
	private MapController mapController = null;
	// for the double buffered graphic showing
	private BufferedImage img;

	/**
	 * the default constructor
	 */
	public MapWindow() {
		this.repaint();
		// mouse Pressed listenner
		this.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				mouseclick(e);
			}
		});

	}

	/**
	 * Override the paint function to draw the buffered image
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see greenzone.view.IWindow#init()
	 */
	@Override
	public void init() {
		this.mapController = CoreController.getMapController();
	}

	/**
	 *
	 * Return the instance Graphics in Graphics2D
	 *
	 * @return Graphics2D
	 */
	public Graphics2D getComponentGraphics() {
		Graphics2D g2d = (Graphics2D) this.getGraphics();
		return g2d;
	}

	/**
	 * return the buffered Graphic2D
	 *
	 * @return
	 */
	public Graphics2D getbufferdGraphics() {
		Graphics2D g2d = (Graphics2D) this.img.getGraphics();
		return g2d;
	}

	/**
	 * Reset the buffered graphic size
	 *
	 * @param width  the width of the buffer graphic
	 * @param height the height of the buffer graphic
	 */
	public void resetBufferdGraphicSize(int width, int height) {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
	}

	/**
	 * implement the mouse
	 * 
	 * @param e
	 */
	public void mouseclick(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		this.mapController.newPoint(x, y);
	}
}
