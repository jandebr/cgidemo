package org.maia.cgi.demo.d3.butterfly.model;

import java.util.HashMap;
import java.util.Map;

import org.maia.cgi.demo.d3.butterfly.shading.BasicButterflyWingShadingModel;
import org.maia.cgi.demo.d3.butterfly.shading.ButterflyWingShadingModel;
import org.maia.cgi.geometry.d3.Point3D;

public class ButterflyDescriptorBuilder {

	private Map<WingType, Double> wingPitchAngleInDegrees = new HashMap<WingType, Double>();

	private double wingOverlapFactor;

	private double scalingFactor;

	private double pitchAngleInDegrees;

	private double rollAngleInDegrees;

	private double yawAngleInDegrees;

	private Point3D position;

	private String pictureFilePath;

	private ButterflyWingShadingModel wingShadingModel;

	public ButterflyDescriptorBuilder() {
		reset();
	}

	public ButterflyDescriptorBuilder(ButterflyDescriptor descriptor) {
		withWingPitchAngleInDegrees(WingType.FRONT_LEFT, descriptor.getWingPitchAngleInDegrees(WingType.FRONT_LEFT));
		withWingPitchAngleInDegrees(WingType.FRONT_RIGHT, descriptor.getWingPitchAngleInDegrees(WingType.FRONT_RIGHT));
		withWingPitchAngleInDegrees(WingType.BACK_LEFT, descriptor.getWingPitchAngleInDegrees(WingType.BACK_LEFT));
		withWingPitchAngleInDegrees(WingType.BACK_RIGHT, descriptor.getWingPitchAngleInDegrees(WingType.BACK_RIGHT));
		withWingOverlapFactor(descriptor.getWingOverlapFactor());
		withScalingFactor(descriptor.getScalingFactor());
		withPitchAngleInDegrees(descriptor.getPitchAngleInDegrees());
		withRollAngleInDegrees(descriptor.getRollAngleInDegrees());
		withYawAngleInDegrees(descriptor.getYawAngleInDegrees());
		withPosition(descriptor.getPosition());
		withPictureFilePath(descriptor.getPictureFilePath());
		withWingShadingModel(descriptor.getWingShadingModel());
	}

	public ButterflyDescriptor build() {
		return new PointInTimeButterflyDescriptor(new HashMap<WingType, Double>(wingPitchAngleInDegrees),
				wingOverlapFactor, scalingFactor, pitchAngleInDegrees, rollAngleInDegrees, yawAngleInDegrees, position,
				pictureFilePath, wingShadingModel);
	}

	public ButterflyDescriptorBuilder reset() {
		withWingPitchAngleInDegrees(WingType.FRONT_LEFT, 50.0);
		withWingPitchAngleInDegrees(WingType.FRONT_RIGHT, 50.0);
		withWingPitchAngleInDegrees(WingType.BACK_LEFT, 45.0);
		withWingPitchAngleInDegrees(WingType.BACK_RIGHT, 45.0);
		this.wingOverlapFactor = 0;
		this.scalingFactor = 1.0;
		this.pitchAngleInDegrees = 0;
		this.rollAngleInDegrees = 0;
		this.yawAngleInDegrees = 0;
		this.position = Point3D.origin();
		this.pictureFilePath = "resources/images/wings/pictures/dummy16_9.png";
		this.wingShadingModel = new BasicButterflyWingShadingModel();
		return this;
	}

	public ButterflyDescriptorBuilder withFrontWingPitchAngleInDegrees(double angleInDegrees) {
		return withWingPitchAngleInDegrees(WingType.FRONT_LEFT, angleInDegrees).withWingPitchAngleInDegrees(
				WingType.FRONT_RIGHT, angleInDegrees);
	}

	public ButterflyDescriptorBuilder withBackWingPitchAngleInDegrees(double angleInDegrees) {
		return withWingPitchAngleInDegrees(WingType.BACK_LEFT, angleInDegrees).withWingPitchAngleInDegrees(
				WingType.BACK_RIGHT, angleInDegrees);
	}

	public ButterflyDescriptorBuilder withWingPitchAngleInDegrees(WingType wingType, double angleInDegrees) {
		this.wingPitchAngleInDegrees.put(wingType, angleInDegrees);
		return this;
	}

	public ButterflyDescriptorBuilder withWingOverlapFactor(double overlapFactor) {
		this.wingOverlapFactor = overlapFactor;
		return this;
	}

	public ButterflyDescriptorBuilder withScalingFactor(double scalingFactor) {
		this.scalingFactor = scalingFactor;
		return this;
	}

	public ButterflyDescriptorBuilder withPitchAngleInDegrees(double angleInDegrees) {
		this.pitchAngleInDegrees = angleInDegrees;
		return this;
	}

	public ButterflyDescriptorBuilder withRollAngleInDegrees(double angleInDegrees) {
		this.rollAngleInDegrees = angleInDegrees;
		return this;
	}

	public ButterflyDescriptorBuilder withYawAngleInDegrees(double angleInDegrees) {
		this.yawAngleInDegrees = angleInDegrees;
		return this;
	}

	public ButterflyDescriptorBuilder withPosition(Point3D position) {
		this.position = position;
		return this;
	}

	public ButterflyDescriptorBuilder withPictureFilePath(String filePath) {
		this.pictureFilePath = filePath;
		return this;
	}

	public ButterflyDescriptorBuilder withWingShadingModel(ButterflyWingShadingModel wingShadingModel) {
		this.wingShadingModel = wingShadingModel;
		return this;
	}

}