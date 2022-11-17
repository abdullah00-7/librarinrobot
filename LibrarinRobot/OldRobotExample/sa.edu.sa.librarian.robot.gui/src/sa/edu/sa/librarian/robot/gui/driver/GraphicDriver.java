package sa.edu.sa.librarian.robot.gui.driver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.Polygon;
import java.awt.Point;
import java.awt.BasicStroke;
import java.util.ArrayList;

import sa.edu.sa.librarian.robot.gui.model.MapElement;
import sa.edu.sa.librarian.robot.gui.model.RobotElement;
import sa.edu.sa.librarian.robot.gui.view.MapWindow;

/**
 * The Graphic Driver for Drawing the graphic in map widow , the drawing use
 * double buffer technology to draw the map panel
 */
public class GraphicDriver {

	// The A1 Size
	private static final int FINAL_W_A1 = 841;
	private static final int FINAL_H_A1 = 594;
	private static final double mapRatio = (FINAL_W_A1 * 1.0) / (FINAL_H_A1 * 1.0); // Relation Width/Height of the map
	private static final int GRID = 50;
	private static final double vGridRatio = (FINAL_W_A1 * 1.0) / GRID; // vertical ratio of grid
	private static final double hGridRatio = (FINAL_H_A1 * 1.0) / GRID; // horizontal ratio of grid
	private static final int robotBaseWidth = 120;
	private static final int robotHeadWidth = 20;
	// map size
	private static int W_MAP = FINAL_W_A1;
	private static int H_MAP = FINAL_H_A1;
	private static int currentWidth = FINAL_W_A1;
	private static int currentHeight = FINAL_H_A1;
	// Array of the elements on the map,a s Graphic objects
	private ArrayList<Polygon> polygonShapeArray = new ArrayList<Polygon>();
	private ArrayList<Ellipse2D> circleShapeArray = new ArrayList<Ellipse2D>();
	// the target map view
	private MapWindow mineMap = null; // the map instance
	private Graphics2D cpG2d = null; // the component graphic
	private Graphics2D bfG2d = null; // the buffered graphic

	/**
	 * The Constructor to invoke the map window to draw graphic on it
	 * 
	 * @param mineMap
	 */
	public GraphicDriver(MapWindow mineMap) {
		this.mineMap = mineMap;
	}

	/*
	 * public GraphicDriver() { }
	 */
	/**
	 * Set the new size of the map, called by tick
	 * 
	 * @param xWidth
	 * @param xHeight
	 */
	public void setNewSize(int xWidth, int xHeight) {
		// Set the current size of the panel
		currentWidth = xWidth;
		currentHeight = xHeight;

		// Get the current Ratio, due the current size of the screen
		double currentRatio = (currentWidth * 1.0 / currentHeight * 1.0);

		// Compares the current ratio with the required one, to get the new size of the
		// map
		if (currentRatio <= mapRatio) {
			W_MAP = currentWidth;
			H_MAP = (int) (currentWidth * (1 / mapRatio));
		} else {
			H_MAP = currentHeight;
			W_MAP = (int) (H_MAP * mapRatio);
		}
	}

	/**
	 * Before draw anything, this method gets the double graphics
	 */
	private void startPaint() {
		cpG2d = this.mineMap.getComponentGraphics();
		bfG2d = this.mineMap.getbufferdGraphics();
	}

	/**
	 * After drawing anything, this method disposes the double graphic
	 */
	private void endPaint() {
		cpG2d.dispose();
		bfG2d.dispose();
	}

	/**
	 * This method draws the boundary of the map, called by tick
	 */
	public void drawBoundary() {
		this.startPaint();

		Line2D hi = new Line2D.Double(0, 0, W_MAP, 0);
		Line2D hf = new Line2D.Double(0, 0, 0, H_MAP);
		Line2D vi = new Line2D.Double(W_MAP, 0, W_MAP, H_MAP);
		Line2D vf = new Line2D.Double(0 + 1, H_MAP - 1, W_MAP - 1, H_MAP - 1);

		// Border
		cpG2d.setColor(Color.RED);
		cpG2d.draw(hi);
		cpG2d.draw(hf);
		cpG2d.draw(vi);
		cpG2d.draw(vf);

		bfG2d.setColor(Color.RED);
		bfG2d.draw(hi);
		bfG2d.draw(hf);
		bfG2d.draw(vi);
		bfG2d.draw(vf);

		this.endPaint();
	}

	/**
	 * This method draws the grid of the map, called by tick
	 */
	public void drawGrid() {
		this.startPaint();
		// Draw the grid

		// Vertical grid
		int vGrid = 0;
		cpG2d.setColor(Color.GRAY);
		bfG2d.setColor(Color.GRAY);
		while (vGrid <= W_MAP) {
			cpG2d.drawLine(vGrid, 0, vGrid, H_MAP); // ZOOM);
			bfG2d.drawLine(vGrid, 0, vGrid, H_MAP); // ZOOM);
			vGrid += (int) (W_MAP / vGridRatio); // (int)(GRID*dWidth);
		}

		// Horizontal grid
		int hGrid = 0;
		cpG2d.setColor(Color.GRAY);
		bfG2d.setColor(Color.GRAY);
		while (hGrid <= H_MAP) {
			cpG2d.drawLine(0, hGrid, W_MAP, hGrid);
			bfG2d.drawLine(0, hGrid, W_MAP, hGrid);
			hGrid += (int) (H_MAP / hGridRatio);
		}

		this.endPaint();
	}

	/**
	 * This method draw a set of points on the map. Called when a new element is
	 * being added to the map
	 * 
	 * @param setPoints ArrayList of Points, with normalised points
	 */
	public void drawSetPoints(ArrayList<Point> setPoints) {
		Point p;
		Polygon polyPoint;
		int xPoint, yPoint;

		this.startPaint();

		try {
			cpG2d.setColor(Color.BLACK);
			bfG2d.setColor(Color.BLACK);

			for (int i = 0; i < setPoints.size(); i++) {
				// get the x and y position of the points
				xPoint = setPoints.get(i).x;
				yPoint = setPoints.get(i).y;

				p = new Point(xPoint, yPoint);

				polyPoint = this.drawPoint(p);
				cpG2d.fillPolygon(polyPoint);
				bfG2d.fillPolygon(polyPoint);
			}
		} catch (Exception e) {
			// System.out.printf("Point outside of the map");
			e.printStackTrace();
		}

		this.endPaint();
	}

	/**
	 * Draw the elements (zones, mines and beacons) on the map, from an ArrayList of
	 * mapElements
	 * 
	 * @param mapElement ArrayList of mapElements
	 */
	public void drawElements(ArrayList<MapElement> mapElement) {

		String elementType;
		this.startPaint();

		try {
			for (int i = 0; i < mapElement.size(); i++) {
				elementType = mapElement.get(i).getElementType();

				// Draw a ZONE
				if (elementType.equals("zone")) {
					Polygon pp = drawPolygon(mapElement.get(i).getPointsArrayList());

					// Paint a SAFE ZONE
					if (mapElement.get(i).getElementName().equals("safeZone")) {
						cpG2d.setColor(Color.GREEN);
						bfG2d.setColor(Color.GREEN);
					}

					// Paint a SEARCHING ZONE
					if (mapElement.get(i).getElementName().equals("searchingZone")) {
						cpG2d.setColor(Color.YELLOW);
						bfG2d.setColor(Color.YELLOW);
					}

					// Paint a SEARCHING ZONE
					if (mapElement.get(i).getElementName().equals("noGoZone")) {
						cpG2d.setColor(Color.RED);
						bfG2d.setColor(Color.RED);
					}

					cpG2d.fillPolygon(pp);
					bfG2d.fillPolygon(pp);

					// Add the element to the Array of Polygons
					polygonShapeArray.add(pp);
				}

				// Draw a mine
				if (elementType.equals("mine")) {
					Ellipse2D cc = this.drawCircle(mapElement.get(i).getPoint(0), mapElement.get(i).getRadius());
					cpG2d.setColor(Color.RED);
					cpG2d.draw(cc);
					cpG2d.fill(cc);
					bfG2d.setColor(Color.RED);
					bfG2d.draw(cc);
					bfG2d.fill(cc);

					// Add mine to the circle array
					circleShapeArray.add(cc);
				}

				// Draw a mine
				if (elementType.equals("beacon")) {
					Ellipse2D cc = this.drawCircle(mapElement.get(i).getPoint(0), mapElement.get(i).getRadius());
					cpG2d.setColor(Color.BLUE);
					cpG2d.draw(cc);
					cpG2d.fill(cc);
					bfG2d.setColor(Color.BLUE);
					bfG2d.draw(cc);
					bfG2d.fill(cc);

					// Add beacon to the circle array
					circleShapeArray.add(cc);
				}
			}
		} catch (Exception e) {
			// System.out.printf("Error when draw a element");
			// e.printStackTrace();
		}

		this.endPaint();
	}

	/**
	 * Draw the robot described by a RobotElement object, which contains the robot's
	 * head position and the angle of the robot.
	 * 
	 * @param r The robot object
	 */
	public void drawRobot(RobotElement r) {
		this.startPaint();

		// If the RobotElement is null, the return without draw the robot
		if (r == null) {
			return;
		}

		// Get head position and the angle of its position
		Point pr = r.getPoint();
		int angle = Integer.parseInt(r.getDirection());

		try {
			// Draw the body of the robot (a black triangle)
			Polygon robot_body = drawRobot(pr, robotBaseWidth, angle);
			cpG2d.setColor(Color.BLACK);
			cpG2d.fillPolygon(robot_body);

			// Draw the head of the robot (a red small triangle)
			Polygon robot_head = drawRobot(pr, robotHeadWidth, angle);
			cpG2d.setColor(Color.RED);
			cpG2d.fillPolygon(robot_head);

		} catch (Exception e) {
			// System.out.printf("Error when draw robot");
			// e.printStackTrace();
		}
		this.endPaint();
	}

	/**
	 * Draw a polygon from a list of (x,y) points
	 * 
	 * @param xPos Arraylist with the x points
	 * @param yPos Arraylist with the y points
	 * @return Valid polygon, formed by the list of (x,y) points
	 * @throws java.lang.Exception
	 */
	private Polygon drawPolygon(ArrayList<Point> xPoints) throws Exception {
		Polygon poly = new Polygon();
		int xPoint, yPoint;

		for (int i = 0; i < xPoints.size(); i++) {
			// Normalise the point according the size of the map
			xPoint = this.normPosX(xPoints.get(i).x);
			yPoint = this.normPosY(xPoints.get(i).y);

			// Any of the point is out-of-range
			if ((xPoint > W_MAP) || (yPoint > H_MAP)) {
				throw new Exception();
			}

			// Add a valid point to the polygon
			poly.addPoint(xPoint, yPoint);
		}
		return poly;
	}

	private Ellipse2D drawCircle(Point xCentre, int radious) throws Exception {
		int xPoint, yPoint, xRadious;
		double midRadious;

		// Calculate the centre point according to the map size
		xRadious = this.normPosX(radious);
		midRadious = xRadious / 2;
		xPoint = (int) (((xCentre.x * W_MAP) / FINAL_W_A1) - midRadious); // denormalised due due the substraction
		yPoint = (int) (((xCentre.y * H_MAP) / FINAL_H_A1) - midRadious); // denormalised due due the substraction

		// Validate the centre is not out-of-range
		if ((xPoint > W_MAP) || (yPoint > H_MAP)) {
			throw new Exception();
		}

		// Add a valid point to the polygon
		Ellipse2D circle = new Ellipse2D.Double(xPoint, yPoint, xRadious, xRadious);

		return circle;
	}

	private Polygon drawPoint(Point newPoint) throws Exception {
		Polygon poly = new Polygon();
		int xPoint, yPoint;

		// normalise the points
		xPoint = this.normPosX(newPoint.x); // ((newPoint.x * W_MAP) / FINAL_W_A1);
		yPoint = this.normPosY(newPoint.y); // (int) ((newPoint.y * W_MAP) / FINAL_W_A1);

		// Any of the point is out-of-range
		if ((xPoint > W_MAP) || (yPoint > H_MAP)) {
			throw new Exception();
		}

		poly.addPoint(xPoint - 2, yPoint - 2);
		poly.addPoint(xPoint + 2, yPoint - 2);
		poly.addPoint(xPoint + 2, yPoint + 2);
		poly.addPoint(xPoint - 2, yPoint + 2);

		return poly;
	}

	private Polygon drawRobot(Point headPoint, int robotWidht, int robotAngle) throws Exception {
		Polygon robot = new Polygon();
		int xPoint, yPoint;
		int side1 = -120; // -30; //240;
		int side2 = -60; // 30; //300;

		// normalise the point and width of the robot
		xPoint = this.normPosX(headPoint.x); // ((headPoint.x * W_MAP) / FINAL_W_A1);
		yPoint = this.normPosY(headPoint.y); // (int) ((headPoint.y * H_MAP) / FINAL_H_A1);
		robotWidht = this.normPosX(robotWidht); // ((robotWidht * W_MAP) / FINAL_W_A1);

		// Any of the point is out-of-range
		if ((xPoint > W_MAP) || (yPoint > H_MAP)) {
			throw new Exception();
		}

		// Get the sin and cos of the side, considering the robot is a equilateral
		// triangule
		double robotSinX1 = Math.sin((side1 - robotAngle) / (180 / Math.PI)) * robotWidht; // Then check the angle
		double robotCosY1 = Math.cos((side1 - robotAngle) / (180 / Math.PI)) * robotWidht; // Then check the angle
		double robotSinX2 = Math.sin((side2 - robotAngle) / (180 / Math.PI)) * robotWidht; // Then check the angle
		double robotCosY2 = Math.cos((side2 - robotAngle) / (180 / Math.PI)) * robotWidht; // Then check the angle

		/*
		 * // System.out.printf("SIN: %f - COS: %f\n", robotSinX1, robotCosY1); //
		 * System.out.printf("SIN: %f - COS: %f\n", robotSinX2, robotCosY2); //
		 * System.out.printf("x: %d - y: %d\n", xPoint, yPoint); //
		 * System.out.printf("x: %d - y: %d\n", (int) (robotSinX1) + xPoint, (int)
		 * (robotCosY1) + yPoint); // System.out.printf("x: %d - y: %d\n", (int)
		 * (robotSinX2) + xPoint, (int) (robotCosY2) + yPoint);
		 */

		// Add points to the robot
		robot.addPoint(xPoint, yPoint);
		robot.addPoint((int) (robotSinX1) + xPoint, (int) (robotCosY1) + yPoint);
		robot.addPoint((int) (robotSinX2) + xPoint, (int) (robotCosY2) + yPoint);

		return robot;
	}

	/**
	 * Clean the whole Screen
	 */
	public void CLS() {
		this.startPaint();
		cpG2d.setBackground(Color.WHITE);
		bfG2d.setBackground(Color.WHITE);
		cpG2d.clearRect(0, 0, W_MAP, H_MAP);
		bfG2d.clearRect(0, 0, W_MAP, H_MAP);

		this.endPaint();
	}

	/**
	 * check if a point is inside any area on the map
	 */
	public boolean isPointInArea(Point checkPoint) throws Exception {
		int xPoint, yPoint;
		boolean output = false;

		xPoint = this.normPosX(checkPoint.x);
		yPoint = this.normPosY(checkPoint.y);

		// Check the point is outside the map
		if ((xPoint > W_MAP) || (yPoint > H_MAP)) {
			throw new Exception();
		}

		// Check the point in any registered polygon
		for (int i = 0; i < polygonShapeArray.size(); i++) {
			if (polygonShapeArray.get(i).contains(xPoint, yPoint)) {
				return true;
			}
		}

		// Check the point in any registered polygon
		for (int i = 0; i < circleShapeArray.size(); i++) {
			if (circleShapeArray.get(i).contains(xPoint, yPoint)) {
				return true;
			}
		}
		return output;
	}

	public boolean isPointInNoGoArea(ArrayList<MapElement> NGZmapElement, Point centerPoint) {
		boolean output = false;
		int pointsToCheck = 9;
		int radious = 15;
		int angle = 45;
		int xPos, yPos;
		// Array of No Go Elements
		ArrayList<Polygon> noGoPolygon = new ArrayList<Polygon>();
		ArrayList<Ellipse2D> noGoCircle = new ArrayList<Ellipse2D>();
		String elementType;

		Point[] checkPoints = new Point[pointsToCheck];

		// Add a point to the list, to every 45 degrees
		for (int i = 0; i < pointsToCheck - 1; i++) {
			xPos = (int) (Math.sin((angle * i) / (180 / Math.PI)) * radious);
			yPos = (int) (Math.cos((angle * i) / (180 / Math.PI)) * radious);
			checkPoints[i] = new Point(xPos + centerPoint.x, yPos + centerPoint.y);
		}

		// Add the input point: the center
		checkPoints[pointsToCheck - 1] = centerPoint;

		try {
			// For every element in the input NoGoZone Map Element arrayList
			for (int i = 0; i < NGZmapElement.size(); i++) {
				// get the type
				elementType = NGZmapElement.get(i).getElementType();

				// Draw a ZONE
				if (elementType.equals("zone")) {
					// Draw a polygon
					Polygon pp = drawPolygon(NGZmapElement.get(i).getPointsArrayList());
					// If it is a noGoZone, add it to noGoPolygon arrayList
					if (NGZmapElement.get(i).getElementName().equals("noGoZone")) {
						noGoPolygon.add(pp);
					}
				}

				// Draw a mine
				if (elementType.equals("mine")) {
					// Draw a circle
					Ellipse2D cc = this.drawCircle(NGZmapElement.get(i).getPoint(0), NGZmapElement.get(i).getRadius());
					// Add it to a noGoCircle arrayList
					noGoCircle.add(cc);
				}
			}
		} catch (Exception e) {
//            System.out.printf("Error when draw a element");
//            e.printStackTrace();
		}

		// Check if every one of the points in the list belongs to a no go zone area.
		// If a point belongs to a no go zone area, end with a true: At least one point
		// belong to a no go zone area.
//        ArrayList<Point> setPoints = new ArrayList<Point>();
		for (int i = 0; i < checkPoints.length; i++) {
			// System.out.printf("%d: (%d, %d)\n", i, checkPoints[i].x, checkPoints[i].y);
			// Check the point in any registered no go zone
			for (int j = 0; j < noGoPolygon.size(); j++) {
				if (noGoPolygon.get(j).contains(normPosX(checkPoints[i].x), normPosY(checkPoints[i].y))) {
//                    System.out.printf("Polygon %d: (%d, %d)\n", j, normPosX(checkPoints[i].x), normPosY(checkPoints[i].y));
					/*
					 * this.startPaint(); cpG2d.setColor(Color.BLACK); bfG2d.setColor(Color.BLACK);
					 * cpG2d.fillPolygon(noGoPolygon.get(j)); bfG2d.fillPolygon(noGoPolygon.get(j));
					 * setPoints.add(checkPoints[i]); this.drawSetPoints(setPoints);
					 * this.endPaint();
					 */
					return true;
				}
			}

			// Check the point in any registered no go circle
			for (int j = 0; j < noGoCircle.size(); j++) {
				if (noGoCircle.get(j).contains(normPosX(checkPoints[i].x), normPosY(checkPoints[i].y))) {
//                    System.out.printf("Mine %d: (%d, %d)\n", i, normPosX(checkPoints[i].x), normPosY(checkPoints[i].y));
					return true;
				}
			}
		}

		return output;
	}

	/*
	 * //JUST TEST FOR THE ROBOT PATH TESTER public void
	 * drawElementsTest(ArrayList<MapElement> mapElement) {
	 * 
	 * String elementType;
	 * 
	 * try { for (int i = 0; i < mapElement.size(); i++) { elementType =
	 * mapElement.get(i).getElementType();
	 * 
	 * //Draw a ZONE if (elementType.equals("zone")) { Polygon pp =
	 * drawPolygon(mapElement.get(i).getPointsArrayList());
	 * 
	 * //Paint a SAFE ZONE if
	 * (mapElement.get(i).getElementName().equals("safeZone")) {
	 * polygonShapeArray.add(pp); }
	 * 
	 * //Paint a SEARCHING ZONE if
	 * (mapElement.get(i).getElementName().equals("searchingZone")) {
	 * polygonShapeArray.add(pp); }
	 * 
	 * //Paint a SEARCHING ZONE if
	 * (mapElement.get(i).getElementName().equals("noGoZone")) {
	 * polygonShapeArray.add(pp); //Add to No Go Zone Polygon //
	 * noGoPolygon.add(pp); } }
	 * 
	 * //Draw a mine if (elementType.equals("mine")) { Ellipse2D cc =
	 * this.drawCircle(mapElement.get(i).getPoint(0),
	 * mapElement.get(i).getRadius()); //Add mine to the circle array
	 * circleShapeArray.add(cc); //Add mine to no go circle array //
	 * noGoCircle.add(cc); }
	 * 
	 * //Draw a mine if (elementType.equals("beacon")) { Ellipse2D cc =
	 * this.drawCircle(mapElement.get(i).getPoint(0),
	 * mapElement.get(i).getRadius()); //Add beacon to the circle array
	 * circleShapeArray.add(cc); } } } catch (Exception e) {
	 * System.out.printf("Error when draw a element"); e.printStackTrace(); } }
	 */
	/**
	 * Select an Area in the map
	 */
	public Polygon selectPolygon(Point checkPoint) throws Exception {
		Polygon pp = null;
		int xPoint, yPoint;

		xPoint = this.normPosX(checkPoint.x);
		yPoint = this.normPosY(checkPoint.y);

		// Check the point is within the map
		if ((xPoint > W_MAP) || (yPoint > H_MAP)) {
			throw new Exception();
		}

		// Check the point in any registered polygon
		for (int i = 0; i < polygonShapeArray.size(); i++) {
			if (polygonShapeArray.get(i).contains(xPoint, yPoint)) {
				pp = polygonShapeArray.get(i);
			}
		}

		return pp;
	}

	/**
	 * Select an Circle in the map
	 */
	public Ellipse2D selectCircle(Point checkPoint) throws Exception {
		Ellipse2D cc = null;
		int xPoint, yPoint;

		xPoint = this.normPosX(checkPoint.x);
		yPoint = this.normPosY(checkPoint.y);

		// Check the point is within the map
		if ((xPoint > W_MAP) || (yPoint > H_MAP)) {
			throw new Exception();
		}

		// Check the point in any registered polygon
		for (int i = 0; i < circleShapeArray.size(); i++) {
			if (circleShapeArray.get(i).contains(xPoint, yPoint)) {
				cc = circleShapeArray.get(i);
			}
		}

		return cc;
	}

	/**
	 * Normalise the value of a x position on the map to a real position on the map
	 * 
	 * @param posX The position x to be normalised
	 * @return The value normalised
	 */
	public int normPosX(int posX) {
		// System.out.printf("NORM: %d %d %d\n",posX, W_MAP, FINAL_W_A1);
		return (int) ((posX * W_MAP) / FINAL_W_A1);
	}

	/**
	 * Normalise the value of a y position on the map to a real position on the map
	 * 
	 * @param posY The position y to be normalised
	 * @return The value normalised
	 */
	public int normPosY(int posY) {
		// System.out.printf("NORM: %d %d %d\n",posY, H_MAP, FINAL_H_A1);
		return (int) ((posY * H_MAP) / FINAL_H_A1);
	}

	public int normClickX(int posX) {
		return (int) ((FINAL_W_A1 * posX) / W_MAP);
	}

	public int normClickY(int posY) {
		return (int) ((FINAL_H_A1 * posY) / H_MAP);
	}

	public void selectByUuid(ArrayList<MapElement> mapElement, String Uuid) {
		this.startPaint();

		String elementUuid = new String();
		String elementType = new String();

		cpG2d.setStroke(new BasicStroke(3.0f));
		bfG2d.setStroke(new BasicStroke(3.0f));
		cpG2d.setColor(Color.BLACK);
		bfG2d.setColor(Color.BLACK);

		try {
			for (int i = 0; i < mapElement.size(); i++) {
				elementUuid = mapElement.get(i).getElementUuid();
				elementType = mapElement.get(i).getElementType();

				// Select a ZONE
				if (elementUuid.equals(Uuid)) {
					if (elementType.equals("zone")) {
						Polygon pp = drawPolygon(mapElement.get(i).getPointsArrayList());
						cpG2d.draw(pp);
						bfG2d.draw(pp);
					}

					if (elementType.equals("mine")) {
						Ellipse2D cc = this.drawCircle(mapElement.get(i).getPoint(0), mapElement.get(i).getRadius());
						cpG2d.draw(cc);
						bfG2d.draw(cc);
					}

					if (elementType.equals("beacon")) {
						Ellipse2D cc = this.drawCircle(mapElement.get(i).getPoint(0), mapElement.get(i).getRadius());
						cpG2d.draw(cc);
						bfG2d.draw(cc);
					}
				}
			}

		} catch (Exception e) {
//            System.out.printf("Error selection the element");
//            e.printStackTrace();
		}

		this.endPaint();
	}
}
