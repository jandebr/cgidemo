package org.maia.cgi.demo.d3.elephant.model;

import java.awt.Color;

import org.maia.cgi.render.d3.shading.FlatShadingModel;
import org.maia.cgi.render.d3.shading.FlatShadingModelImpl;

public class ElephantTheme {

	private Color bodyPartColor;

	private FlatShadingModel bodyPartShadingModel;

	private Color eyeColor;

	private FlatShadingModel eyeShadingModel;

	private Color outerEarColor;

	private Color innerEarColor;

	private FlatShadingModel earShadingModel;

	private Color outerFootColor;

	private Color innerFootColor;

	private Color toeColor;

	private FlatShadingModel footShadingModel;

	private Color tailColor;

	private FlatShadingModel tailShadingModel;

	public ElephantTheme() {
		this.bodyPartColor = new Color(91, 145, 199);
		this.bodyPartShadingModel = new FlatShadingModelImpl(0.9, 1.0);
		this.eyeColor = new Color(22, 23, 28);
		this.eyeShadingModel = new FlatShadingModelImpl(0.5, 6.0);
		this.outerEarColor = new Color(217, 91, 82);
		this.innerEarColor = new Color(255, 184, 179);
		this.earShadingModel = new FlatShadingModelImpl(0.9, 1.0);
		this.outerFootColor = bodyPartColor;
		this.innerFootColor = innerEarColor;
		this.toeColor = new Color(188, 202, 214);
		this.footShadingModel = new FlatShadingModelImpl(0.9, 1.0);
		this.tailColor = new Color(175, 199, 219);
		this.tailShadingModel = new FlatShadingModelImpl(0.9, 1.0);
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

	public Color getEyeColor() {
		return eyeColor;
	}

	public void setEyeColor(Color eyeColor) {
		this.eyeColor = eyeColor;
	}

	public FlatShadingModel getEyeShadingModel() {
		return eyeShadingModel;
	}

	public void setEyeShadingModel(FlatShadingModel eyeShadingModel) {
		this.eyeShadingModel = eyeShadingModel;
	}

	public Color getOuterEarColor() {
		return outerEarColor;
	}

	public void setOuterEarColor(Color outerEarColor) {
		this.outerEarColor = outerEarColor;
	}

	public Color getInnerEarColor() {
		return innerEarColor;
	}

	public void setInnerEarColor(Color innerEarColor) {
		this.innerEarColor = innerEarColor;
	}

	public FlatShadingModel getEarShadingModel() {
		return earShadingModel;
	}

	public void setEarShadingModel(FlatShadingModel earShadingModel) {
		this.earShadingModel = earShadingModel;
	}

	public Color getOuterFootColor() {
		return outerFootColor;
	}

	public void setOuterFootColor(Color outerFootColor) {
		this.outerFootColor = outerFootColor;
	}

	public Color getInnerFootColor() {
		return innerFootColor;
	}

	public void setInnerFootColor(Color innerFootColor) {
		this.innerFootColor = innerFootColor;
	}

	public Color getToeColor() {
		return toeColor;
	}

	public void setToeColor(Color toeColor) {
		this.toeColor = toeColor;
	}

	public FlatShadingModel getFootShadingModel() {
		return footShadingModel;
	}

	public void setFootShadingModel(FlatShadingModel footShadingModel) {
		this.footShadingModel = footShadingModel;
	}

	public Color getTailColor() {
		return tailColor;
	}

	public void setTailColor(Color tailColor) {
		this.tailColor = tailColor;
	}

	public FlatShadingModel getTailShadingModel() {
		return tailShadingModel;
	}

	public void setTailShadingModel(FlatShadingModel tailShadingModel) {
		this.tailShadingModel = tailShadingModel;
	}

}