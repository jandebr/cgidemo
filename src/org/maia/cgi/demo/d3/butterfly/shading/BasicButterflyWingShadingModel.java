package org.maia.cgi.demo.d3.butterfly.shading;

import java.awt.Color;

import org.maia.cgi.demo.d3.butterfly.model.ButterflyScene;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyWing;
import org.maia.cgi.geometry.d3.Point3D;

public class BasicButterflyWingShadingModel implements ButterflyWingShadingModel {

	public BasicButterflyWingShadingModel() {
	}

	@Override
	public Color applyShading(Color color, Point3D positionInCamera, Point3D maskPosition, ButterflyWing wing,
			ButterflyScene scene) {
		return color;
	}

}
