package org.maia.cgi.demo.d3.butterfly.shading;

import java.awt.Color;

import org.maia.cgi.demo.d3.butterfly.model.ButterflyScene;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyWing;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.render.d3.shading.ShadingModel;

public interface ButterflyWingShadingModel extends ShadingModel {

	Color applyShading(Color surfaceColor, Point3D surfacePositionInCamera, Point3D maskPosition, ButterflyWing wing,
			ButterflyScene scene, RenderOptions options);

}