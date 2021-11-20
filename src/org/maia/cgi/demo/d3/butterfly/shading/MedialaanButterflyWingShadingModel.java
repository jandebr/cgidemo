package org.maia.cgi.demo.d3.butterfly.shading;

import java.awt.Color;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyScene;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyWing;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.render.d3.RenderOptions;

public class MedialaanButterflyWingShadingModel extends VtmGoButterflyWingShadingModel {

	public MedialaanButterflyWingShadingModel() {
	}

	@Override
	public Color applyShading(Color surfaceColor, Point3D surfacePositionInCamera, Point3D maskPosition,
			ButterflyWing wing, ButterflyScene scene, RenderOptions options) {
		Color color = surfaceColor;
		color = applyWingLuminance(color, maskPosition, wing);
		color = applyWingContour(color, maskPosition, wing);
		color = applyLighting(color, surfacePositionInCamera, scene);
		color = applyTransparency(color, maskPosition);
		return color;
	}

	private Color applyLighting(Color color, Point3D surfacePositionInCamera, ButterflyScene scene) {
		double z = surfacePositionInCamera.getZ();
		if (z < -11.0) {
			double distMax = 0.3 + 0.25 * z / scene.getCamera().getViewVolume().getFarPlaneZ();
			double dist = Math.min(scene.getDistanceToNearestSpotLight(surfacePositionInCamera), distMax);
			double r = Math.pow(dist / distMax, 1.0);
			double brightnessFactor = 1.0 - 1.6 * r;
			color = Compositing.adjustBrightness(color, brightnessFactor);
			double saturationFactor = 1.0 - 1.3 * r;
			if (saturationFactor < 0) {
				color = Compositing.adjustSaturation(color, saturationFactor);
			}
		}
		return color;
	}

}
