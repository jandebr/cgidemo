package org.maia.cgi.demo.d3.butterfly.shading;

import java.awt.Color;

import org.maia.cgi.demo.d3.butterfly.model.ButterflyScene;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyWing;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.shading.d3.ShadingModel;

public interface ButterflyWingShadingModel extends ShadingModel {

	Color applyShading(Color color, Point3D positionInCamera, Point3D maskPosition, ButterflyWing wing,
			ButterflyScene scene);

}