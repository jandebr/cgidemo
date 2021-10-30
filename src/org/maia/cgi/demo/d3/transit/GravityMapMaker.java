package org.maia.cgi.demo.d3.transit;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.geometry.d2.Point2D;

public class GravityMapMaker {

	public static void main(String[] args) {
		GravityMapMaker gmm = new GravityMapMaker();
		BufferedImage map = gmm.makeMap(640, 480);
		Compositing.writeImageToFile(map, "resources/gravity.png");
	}

	private BufferedImage makeMap(int width, int height) {
		BufferedImage map = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Collection<GravityCenter> centers = createGravityCenters(12, width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double gravity = computeGravity(new Point2D(x, y), centers); // between -1 and +1
				int c = (int) Math.round((gravity + 1) / 2 * 255);
				Color color = new Color(c, c, c);
				map.setRGB(x, y, color.getRGB());
			}
		}
		return map;
	}

	private double computeGravity(Point2D point, Collection<GravityCenter> centers) {
		double gravity = 0;
		double norm = 0;
		for (GravityCenter center : centers) {
			double g = computeGravity(point, center);
			double d = center.distanceTo(point);
			double n = 1.0 / (1.0 + d * d * d);
			gravity += n * g;
			norm += n;
		}
		return gravity / norm;
	}

	private double computeGravity(Point2D point, GravityCenter center) {
		double dist = center.distanceTo(point);
		double stdev = center.getMagnitude();
		double g = Math.pow(Math.E, -dist * dist / (2 * stdev * stdev));
		g = Math.min(g * 1.0, 1.0);
		return g * center.getPolarity();
	}

	private Collection<GravityCenter> createGravityCenters(int n, int width, int height) {
		Collection<GravityCenter> centers = new Vector<GravityCenter>(n);
		while (centers.size() < n) {
			double x = Math.random() * width;
			double y = Math.random() * height;
			Point2D p = new Point2D(x, y);
			double minDist = Double.MAX_VALUE;
			for (GravityCenter center : centers) {
				minDist = Math.min(minDist, center.distanceTo(p));
			}
			if (minDist >= width / 10) {
				double magnitude = width * (1.0 + Math.random());
				int polarity = Math.random() < 0.5 ? -1 : 1;
				centers.add(new GravityCenter(p, magnitude, polarity));
			}
		}
		return centers;
	}

	private static class GravityCenter {

		private Point2D position;

		private double magnitude;

		private int polarity;

		public GravityCenter(Point2D position, double magnitude, int polarity) {
			this.position = position;
			this.magnitude = magnitude;
			this.polarity = polarity;
		}

		public double distanceTo(Point2D p) {
			double dx = getPosition().getX() - p.getX();
			double dy = getPosition().getY() - p.getY();
			return Math.sqrt(dx * dx + dy * dy);
		}

		public Point2D getPosition() {
			return position;
		}

		public double getMagnitude() {
			return magnitude;
		}

		public int getPolarity() {
			return polarity;
		}

	}

}