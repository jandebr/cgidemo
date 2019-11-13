package org.maia.cgi.demo.d3.butterfly.shading;

import java.awt.Color;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyWing;
import org.maia.cgi.geometry.d3.Point3D;

public abstract class VtmGoButterflyWingShadingModel implements ButterflyWingShadingModel {

	protected VtmGoButterflyWingShadingModel() {
	}

	protected Color applyWingLuminance(Color color, Point3D maskPosition, ButterflyWing wing) {
		double luminance = wing.getLuminanceMap().sampleDouble(maskPosition.getX(), maskPosition.getZ());
		double factor = luminance - 0.5;
		return Compositing.adjustBrightness(color, 0.5 * factor);
	}

	protected Color applyWingContour(Color color, Point3D maskPosition, ButterflyWing wing) {
		if (!wing.getWingContourMask().isMasked(maskPosition.getX(), maskPosition.getZ())) {
			return Compositing.adjustBrightness(color, 0.6);
		} else {
			return color;
		}
	}

	protected Color applyTransparency(Color color, Point3D maskPosition) {
		return Compositing.setTransparency(color, 0.1);// Could use a transparency map here
	}

}
