package org.maia.cgi.demo.d3.curve;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.geometry.d3.ApproximatingCurve3D;
import org.maia.cgi.geometry.d3.Curve3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d2.ShapeContourTracer;

public class CurveDemo {

	public CurveDemo() {
	}

	public void show() {
		List<Point3D> controlPoints = createControlPoints();
		System.out.println(controlPoints.size() + " control points");
		Curve3D curve = ApproximatingCurve3D.createStandardBezierCurve(controlPoints);
		BufferedImage image = loadShapeImage();
		Graphics2D g = (Graphics2D) image.getGraphics();
		drawCurve(curve, g, Color.WHITE);
		drawVertices(controlPoints, g, Color.YELLOW);
		g.dispose();
		saveImage(image);
	}

	private BufferedImage loadShapeImage() {
		return Compositing.readImageFromFile("resources/curve/shape.png");
	}

	private void saveImage(BufferedImage image) {
		Compositing.writeImageToFile(image, "resources/curve/output.png");
	}

	private List<Point3D> createControlPoints() {
		BufferedImage image = loadShapeImage();
		ShapeContourTracer tracer = new ShapeContourTracer();
		return tracer.traceContour(image, new Color(30, 30, 30), 50).getContourPoints3D();
	}

	private void drawCurve(Curve3D curve, Graphics2D g, Color c) {
		g.setColor(c);
		Point3D p0 = null;
		int n = 100;
		for (int i = 0; i <= n; i++) {
			Point3D p1 = curve.sample(i * 1.0 / n);
			if (p0 != null) {
				int x0 = (int) Math.round(p0.getX());
				int y0 = (int) Math.round(-p0.getY());
				int x1 = (int) Math.round(p1.getX());
				int y1 = (int) Math.round(-p1.getY());
				g.drawLine(x0, y0, x1, y1);
			}
			p0 = p1;
		}
	}

	private void drawVertices(List<Point3D> vertices, Graphics2D g, Color c) {
		g.setColor(c);
		for (Point3D vertex : vertices) {
			int x = (int) Math.round(vertex.getX());
			int y = (int) Math.round(-vertex.getY());
			g.fillRect(x - 1, y - 1, 3, 3);
		}
	}

}