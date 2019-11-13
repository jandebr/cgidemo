package org.maia.cgi.demo.d3.butterfly.model;

import java.util.Map;

import org.maia.cgi.demo.d3.butterfly.shading.ButterflyWingShadingModel;
import org.maia.cgi.geometry.d3.Point3D;

public class PointInTimeButterflyDescriptor implements ButterflyDescriptor {

	private Map<WingType, Double> wingPitchAngleInDegrees;

	private double wingOverlapFactor;

	private double scalingFactor;

	private double pitchAngleInDegrees;

	private double rollAngleInDegrees;

	private double yawAngleInDegrees;

	private Point3D position;

	private String pictureFilePath;

	private ButterflyWingShadingModel wingShadingModel;

	PointInTimeButterflyDescriptor(Map<WingType, Double> wingPitchAngleInDegrees, double wingOverlapFactor,
			double scalingFactor, double pitchAngleInDegrees, double rollAngleInDegrees, double yawAngleInDegrees,
			Point3D position, String pictureFilePath, ButterflyWingShadingModel wingShadingModel) {
		this.wingPitchAngleInDegrees = wingPitchAngleInDegrees;
		this.wingOverlapFactor = wingOverlapFactor;
		this.scalingFactor = scalingFactor;
		this.pitchAngleInDegrees = pitchAngleInDegrees;
		this.rollAngleInDegrees = rollAngleInDegrees;
		this.yawAngleInDegrees = yawAngleInDegrees;
		this.position = position;
		this.pictureFilePath = pictureFilePath;
		this.wingShadingModel = wingShadingModel;
	}

	@Override
	public double getWingPitchAngleInDegrees(WingType wingType) {
		return wingPitchAngleInDegrees.get(wingType);
	}

	@Override
	public double getWingOverlapFactor() {
		return wingOverlapFactor;
	}

	@Override
	public double getScalingFactor() {
		return scalingFactor;
	}

	@Override
	public double getPitchAngleInDegrees() {
		return pitchAngleInDegrees;
	}

	@Override
	public double getRollAngleInDegrees() {
		return rollAngleInDegrees;
	}

	@Override
	public double getYawAngleInDegrees() {
		return yawAngleInDegrees;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}

	@Override
	public String getPictureFilePath() {
		return pictureFilePath;
	}

	@Override
	public ButterflyWingShadingModel getWingShadingModel() {
		return wingShadingModel;
	}

}
