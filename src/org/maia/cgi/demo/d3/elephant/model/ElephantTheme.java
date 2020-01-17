package org.maia.cgi.demo.d3.elephant.model;

import java.awt.Color;

import org.maia.cgi.shading.d3.FlatShadingModel;
import org.maia.cgi.shading.d3.FlatShadingModelImpl;

public class ElephantTheme {

	private Color bodyPartColor;

	private FlatShadingModel bodyPartShadingModel;

	public ElephantTheme() {
		this.bodyPartColor = new Color(237, 217, 157);
		this.bodyPartShadingModel = new FlatShadingModelImpl(0.9, 1.0);
	}

	public Color getBodyPartColor() {
		return bodyPartColor;
	}

	public void setBodyPartColor(Color bodyPartColor) {
		this.bodyPartColor = bodyPartColor;
	}

	public FlatShadingModel getBodyPartShadingModel() {
		return bodyPartShadingModel;
	}

	public void setBodyPartShadingModel(FlatShadingModel bodyPartShadingModel) {
		this.bodyPartShadingModel = bodyPartShadingModel;
	}

}