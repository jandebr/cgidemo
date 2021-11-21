package org.maia.cgi.demo.d3.butterfly.shading;

import java.awt.Color;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyScene;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyWing;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.render.d3.RenderOptions;

public class HandsButterflyWingShadingModel extends VtmGoButterflyWingShadingModel {

	public HandsButterflyWingShadingModel() {
	}

	@Override
	public Color applyShading(Color surfaceColor, Point3D surfacePositionInCamera, Point3D maskPosition,
			ButterflyWing wing, ButterflyScene scene, RenderOptions options) {
		Color color = surfaceColor;
		color = applyLighting(color, wing, scene);
		color = applyWingLuminance(color, maskPosition, wing);
		color = applyWingContour(color, maskPosition, wing);
		color = applyTransparency(color, maskPosition);
		return color;
	}

	private Color applyLighting(Color color, ButterflyWing wing, ButterflyScene scene) {
		Vector3D normal = wing.getPlaneInCameraCoordinates(scene.getCamera()).getNormalUnitVector();
		Vector3D light = ((ButterflyScene) scene).getLightDirection();
		double lightExposure = Math.pow(Math.abs(light.getAngleBetween(normal) / Math.PI * 2.0 - 1.0), 3.0);
		if (normal.getZ() < 0) {
			// Wing is back facing
			color = Compositing.adjustBrightness(color, 0.2 * lightExposure - 0.2);
		} else {
			// Wing is front facing
			color = Compositing.adjustBrightness(color, 0.8 * lightExposure);
		}
		return color;
	}

}