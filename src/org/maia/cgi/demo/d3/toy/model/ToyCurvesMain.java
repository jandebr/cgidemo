package org.maia.cgi.demo.d3.toy.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.maia.cgi.geometry.d2.LineSegment2D;
import org.maia.cgi.geometry.d2.Point2D;
import org.maia.cgi.geometry.d2.Polygon2D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.object.PolygonalObject3D;
import org.maia.cgi.transform.d3.Transformation;

public class ToyCurvesMain {

	public static void main(String[] args) {
		CanvasFrame frame = new CanvasFrame();
		CanvasPane pane = frame.getPane();
		addPolygonalShapeXY(pane, "resources/toy/bodypart2.csv", Color.WHITE, new Color(34, 68, 122));
		addPolylineXY(pane, "resources/elephant/head.csv", Color.YELLOW, new Color(74, 82, 54));
		frame.setVisible(true);
	}

	private static void addPolygonalShapeXY(CanvasPane pane, String shapeFilePath, Color outlineColor, Color vertexColor) {
		PolygonalObject3D polygon3D = ModelBuilderUtils.loadShapeXY(shapeFilePath, false);
		pane.addShape(new PolygonalShape(projectXY(polygon3D), outlineColor, vertexColor));
		polygon3D.translateX(pane.getWidth() / 2);
		pane.addShape(new PolygonalShape(projectXY(ModelBuilderUtils.smoothenPolygonalShape(polygon3D, 100)),
				outlineColor, vertexColor));
	}

	private static void addPolylineXY(CanvasPane pane, String shapeFilePath, Color outlineColor, Color vertexColor) {
		List<Point3D> vertices = ModelBuilderUtils.loadVerticesXY(shapeFilePath);
		vertices = Transformation.getTranslationMatrix(1.5, 0, 0)
				.postMultiply(Transformation.getScalingMatrix(30, 30, 30)).transform(vertices);
		pane.addShape(new PolylineShape(projectXY(vertices), outlineColor, vertexColor));
		vertices = Transformation.getTranslationMatrix(pane.getWidth() / 2, 0, 0).transform(vertices);
		pane.addShape(new PolylineShape(projectXY(ModelBuilderUtils.smoothenPlanarPolyline(vertices, 100)),
				outlineColor, vertexColor));
	}

	private static Polygon2D projectXY(PolygonalObject3D polygon3D) {
		List<Point2D> vertices2D = projectXY(polygon3D.getVerticesInWorldCoordinates());
		return new Polygon2D(Polygon2D.deriveCentroid(vertices2D), vertices2D);
	}

	private static List<Point2D> projectXY(List<Point3D> points3D) {
		List<Point2D> points2D = new Vector<Point2D>(points3D.size());
		for (Point3D p : points3D) {
			points2D.add(projectXY(p));
		}
		return points2D;
	}

	private static Point2D projectXY(Point3D point3D) {
		return new Point2D(point3D.getX(), point3D.getY());
	}

	@SuppressWarnings("serial")
	private static class CanvasFrame extends JFrame {

		private CanvasPane pane;

		public CanvasFrame() {
			super("Canvas");
			this.pane = new CanvasPane(1024, 800);
			buildUI();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		protected void buildUI() {
			getContentPane().add(getPane());
			pack();
		}

		public CanvasPane getPane() {
			return pane;
		}

	}

	@SuppressWarnings("serial")
	private static class CanvasPane extends JLabel {

		private List<CanvasShape> shapes;

		public CanvasPane(int width, int height) {
			this.shapes = new Vector<CanvasShape>();
			fixedSize(width, height);
			setBackground(Color.BLACK);
		}

		public void addShape(CanvasShape shape) {
			getShapes().add(shape);
		}

		private void fixedSize(int width, int height) {
			Dimension size = new Dimension(width, height);
			setMinimumSize(size);
			setPreferredSize(size);
			setMaximumSize(size);
			setSize(size);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.scale(1.0, -1.0);
			for (CanvasShape shape : getShapes()) {
				shape.draw(g2);
			}
		}

		private List<CanvasShape> getShapes() {
			return shapes;
		}

	}

	private static abstract class CanvasShape {

		private Color color;

		protected CanvasShape(Color color) {
			this.color = color;
		}

		protected abstract void draw(Graphics2D g2);

		public Color getColor() {
			return color;
		}

	}

	private static class PolygonalShape extends CanvasShape {

		private Polygon2D polygon;

		private Color vertexColor;

		public PolygonalShape(Polygon2D polygon, Color outlineColor, Color vertexColor) {
			super(outlineColor);
			this.polygon = polygon;
			this.vertexColor = vertexColor;
		}

		@Override
		protected void draw(Graphics2D g2) {
			drawVertices(g2);
			drawOutline(g2);
		}

		protected void drawVertices(Graphics2D g2) {
			g2.setColor(getVertexColor());
			for (Point2D vertex : getPolygon().getVertices()) {
				int x = (int) Math.round(vertex.getX());
				int y = (int) Math.round(vertex.getY());
				g2.drawOval(x - 3, y - 3, 6, 6);
			}
		}

		protected void drawOutline(Graphics2D g2) {
			g2.setColor(getColor());
			for (LineSegment2D edge : getPolygon().getEdges()) {
				Point2D p1 = edge.getP1();
				Point2D p2 = edge.getP2();
				int x1 = (int) Math.round(p1.getX());
				int y1 = (int) Math.round(p1.getY());
				int x2 = (int) Math.round(p2.getX());
				int y2 = (int) Math.round(p2.getY());
				g2.drawLine(x1, y1, x2, y2);
			}
		}

		public Polygon2D getPolygon() {
			return polygon;
		}

		public Color getVertexColor() {
			return vertexColor;
		}

	}

	private static class PolylineShape extends CanvasShape {

		private List<Point2D> vertices;

		private Color vertexColor;

		public PolylineShape(List<Point2D> vertices, Color outlineColor, Color vertexColor) {
			super(outlineColor);
			this.vertices = vertices;
			this.vertexColor = vertexColor;
		}

		@Override
		protected void draw(Graphics2D g2) {
			drawVertices(g2);
			drawOutline(g2);
		}

		protected void drawVertices(Graphics2D g2) {
			g2.setColor(getVertexColor());
			for (Point2D vertex : getVertices()) {
				int x = (int) Math.round(vertex.getX());
				int y = (int) Math.round(vertex.getY());
				g2.drawOval(x - 3, y - 3, 6, 6);
			}
		}

		protected void drawOutline(Graphics2D g2) {
			g2.setColor(getColor());
			for (int i = 0; i < getVertices().size() - 1; i++) {
				Point2D p1 = getVertices().get(i);
				Point2D p2 = getVertices().get(i + 1);
				int x1 = (int) Math.round(p1.getX());
				int y1 = (int) Math.round(p1.getY());
				int x2 = (int) Math.round(p2.getX());
				int y2 = (int) Math.round(p2.getY());
				g2.drawLine(x1, y1, x2, y2);
			}
		}

		public List<Point2D> getVertices() {
			return vertices;
		}

		public Color getVertexColor() {
			return vertexColor;
		}

	}

}