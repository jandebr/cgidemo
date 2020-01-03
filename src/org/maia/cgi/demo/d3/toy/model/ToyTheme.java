package org.maia.cgi.demo.d3.toy.model;

import java.awt.Color;

import org.maia.cgi.shading.d3.FlatShadingModel;
import org.maia.cgi.shading.d3.FlatShadingModelImpl;

public class ToyTheme {

	private Color wheelColor;

	private FlatShadingModel wheelShadingModel;

	private Color wheelRodColor;

	private FlatShadingModel wheelRodShadingModel;

	private Color bodyPartColor;

	private FlatShadingModel bodyPartShadingModel;

	private Color bodyPartConnectorStripColor;

	private FlatShadingModel bodyPartConnectorStripShadingModel;

	private Color noseColor;

	private FlatShadingModel noseShadingModel;

	private Color eyeColor;

	private FlatShadingModel eyeShadingModel;

	private Color mouthColor;

	private FlatShadingModel mouthShadingModel;

	private Color neckRingColor;

	private FlatShadingModel neckRingShadingModel;

	private Color tailColor;

	private FlatShadingModel tailShadingModel;

	private Color tailCordColor;

	private FlatShadingModel tailCordShadingModel;

	private Color earOutsideColor;

	private Color earInsideColor;

	private FlatShadingModel earShadingModel;

	public ToyTheme() {
		this.bodyPartColor = new Color(237, 217, 157);
		this.bodyPartShadingModel = new FlatShadingModelImpl(0.9, 6.0);
		this.bodyPartConnectorStripColor = new Color(245, 133, 64);
		this.bodyPartConnectorStripShadingModel = new FlatShadingModelImpl(0.8, 2.0);
		this.wheelColor = new Color(204, 54, 0);
		this.wheelShadingModel = new FlatShadingModelImpl(0.8, 2.0);
		this.wheelRodColor = bodyPartColor;
		this.wheelRodShadingModel = bodyPartShadingModel;
		this.noseColor = new Color(87, 45, 3);
		this.noseShadingModel = new FlatShadingModelImpl(0.9, 5.0);
		this.eyeColor = new Color(22, 23, 28);
		this.eyeShadingModel = new FlatShadingModelImpl(0.5, 6.0);
		this.mouthColor = new Color(0, 0, 0);
		this.mouthShadingModel = new FlatShadingModelImpl(0, 6.0);
		this.neckRingColor = new Color(26, 145, 163);
		this.neckRingShadingModel = new FlatShadingModelImpl(0.8, 2.0);
		this.tailColor = noseColor;
		this.tailShadingModel = noseShadingModel;
		this.tailCordColor = new Color(240, 240, 240);
		this.tailCordShadingModel = new FlatShadingModelImpl(0.9, 6.0);
		this.earInsideColor = new Color(232, 212, 197);
		this.earOutsideColor = new Color(107, 49, 7);
		this.earShadingModel = new FlatShadingModelImpl(0.4, 1.0);
	}

	public Color getWheelColor() {
		return wheelColor;
	}

	public void setWheelColor(Color wheelColor) {
		this.wheelColor = wheelColor;
	}

	public FlatShadingModel getWheelShadingModel() {
		return wheelShadingModel;
	}

	public void setWheelShadingModel(FlatShadingModel wheelShadingModel) {
		this.wheelShadingModel = wheelShadingModel;
	}

	public Color getWheelRodColor() {
		return wheelRodColor;
	}

	public void setWheelRodColor(Color wheelRodColor) {
		this.wheelRodColor = wheelRodColor;
	}

	public FlatShadingModel getWheelRodShadingModel() {
		return wheelRodShadingModel;
	}

	public void setWheelRodShadingModel(FlatShadingModel wheelRodShadingModel) {
		this.wheelRodShadingModel = wheelRodShadingModel;
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

	public Color getBodyPartConnectorStripColor() {
		return bodyPartConnectorStripColor;
	}

	public void setBodyPartConnectorStripColor(Color bodyPartConnectorStripColor) {
		this.bodyPartConnectorStripColor = bodyPartConnectorStripColor;
	}

	public FlatShadingModel getBodyPartConnectorStripShadingModel() {
		return bodyPartConnectorStripShadingModel;
	}

	public void setBodyPartConnectorStripShadingModel(FlatShadingModel bodyPartConnectorStripShadingModel) {
		this.bodyPartConnectorStripShadingModel = bodyPartConnectorStripShadingModel;
	}

	public Color getNoseColor() {
		return noseColor;
	}

	public void setNoseColor(Color noseColor) {
		this.noseColor = noseColor;
	}

	public FlatShadingModel getNoseShadingModel() {
		return noseShadingModel;
	}

	public void setNoseShadingModel(FlatShadingModel noseShadingModel) {
		this.noseShadingModel = noseShadingModel;
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

	public Color getMouthColor() {
		return mouthColor;
	}

	public void setMouthColor(Color mouthColor) {
		this.mouthColor = mouthColor;
	}

	public FlatShadingModel getMouthShadingModel() {
		return mouthShadingModel;
	}

	public void setMouthShadingModel(FlatShadingModel mouthShadingModel) {
		this.mouthShadingModel = mouthShadingModel;
	}

	public Color getNeckRingColor() {
		return neckRingColor;
	}

	public void setNeckRingColor(Color neckRingColor) {
		this.neckRingColor = neckRingColor;
	}

	public FlatShadingModel getNeckRingShadingModel() {
		return neckRingShadingModel;
	}

	public void setNeckRingShadingModel(FlatShadingModel neckRingShadingModel) {
		this.neckRingShadingModel = neckRingShadingModel;
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

	public Color getTailCordColor() {
		return tailCordColor;
	}

	public void setTailCordColor(Color tailCordColor) {
		this.tailCordColor = tailCordColor;
	}

	public FlatShadingModel getTailCordShadingModel() {
		return tailCordShadingModel;
	}

	public void setTailCordShadingModel(FlatShadingModel tailCordShadingModel) {
		this.tailCordShadingModel = tailCordShadingModel;
	}

	public Color getEarOutsideColor() {
		return earOutsideColor;
	}

	public void setEarOutsideColor(Color earOutsideColor) {
		this.earOutsideColor = earOutsideColor;
	}

	public Color getEarInsideColor() {
		return earInsideColor;
	}

	public void setEarInsideColor(Color earInsideColor) {
		this.earInsideColor = earInsideColor;
	}

	public FlatShadingModel getEarShadingModel() {
		return earShadingModel;
	}

	public void setEarShadingModel(FlatShadingModel earShadingModel) {
		this.earShadingModel = earShadingModel;
	}

}