package org.maia.cgi.demo.d3.butterfly.model;

import org.maia.cgi.demo.d3.butterfly.shading.ButterflyWingShadingModel;
import org.maia.cgi.geometry.d3.Point3D;

public class InterpolatingButterflyDescriptor implements ButterflyDescriptor {

	private ButterflyDescriptor initialState;

	private ButterflyDescriptor finalState;

	private double interpolationFactor; // between 0 (initial) and 1 (final)

	public InterpolatingButterflyDescriptor(ButterflyDescriptor initialState, ButterflyDescriptor finalState) {
		this.initialState = initialState;
		this.finalState = finalState;
	}

	@Override
	public double getWingPitchAngleInDegrees(WingType wingType) {
		return interpolateValue(getInitialState().getWingPitchAngleInDegrees(wingType), getFinalState()
				.getWingPitchAngleInDegrees(wingType));
	}

	@Override
	public double getWingOverlapFactor() {
		return interpolateValue(getInitialState().getWingOverlapFactor(), getFinalState().getWingOverlapFactor());
	}

	@Override
	public double getScalingFactor() {
		return interpolateValue(getInitialState().getScalingFactor(), getFinalState().getScalingFactor());
	}

	@Override
	public double getPitchAngleInDegrees() {
		return interpolateValue(getInitialState().getPitchAngleInDegrees(), getFinalState().getPitchAngleInDegrees());
	}

	@Override
	public double getRollAngleInDegrees() {
		return interpolateValue(getInitialState().getRollAngleInDegrees(), getFinalState().getRollAngleInDegrees());
	}

	@Override
	public double getYawAngleInDegrees() {
		return interpolateValue(getInitialState().getYawAngleInDegrees(), getFinalState().getYawAngleInDegrees());
	}

	@Override
	public Point3D getPosition() {
		return interpolatePosition(getInitialState().getPosition(), getFinalState().getPosition());
	}

	@Override
	public String getPictureFilePath() {
		return getInitialState().getPictureFilePath();
	}

	@Override
	public ButterflyWingShadingModel getWingShadingModel() {
		return getInitialState().getWingShadingModel();
	}

	private double interpolateValue(double v1, double v2) {
		double f = getInterpolationFactor();
		return (1.0 - f) * v1 + f * v2;
	}

	private Point3D interpolatePosition(Point3D p1, Point3D p2) {
		return new Point3D(interpolateValue(p1.getX(), p2.getX()), interpolateValue(p1.getY(), p2.getY()),
				interpolateValue(p1.getZ(), p2.getZ()));
	}

	public double getInterpolationFactor() {
		return interpolationFactor;
	}

	public void setInterpolationFactor(double interpolationFactor) {
		this.interpolationFactor = interpolationFactor;
	}

	public ButterflyDescriptor getInitialState() {
		return initialState;
	}

	public ButterflyDescriptor getFinalState() {
		return finalState;
	}

}
