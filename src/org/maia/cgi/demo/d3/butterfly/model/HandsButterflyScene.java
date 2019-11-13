package org.maia.cgi.demo.d3.butterfly.model;

import java.io.File;
import java.util.List;

import org.maia.cgi.demo.d3.butterfly.shading.ButterflyWingShadingModel;
import org.maia.cgi.demo.d3.butterfly.shading.HandsButterflyWingShadingModel;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.camera.Camera;

public class HandsButterflyScene extends ButterflyScene {

	private static ButterflyWingShadingModel wingShadingModel = new HandsButterflyWingShadingModel();

	public HandsButterflyScene(Camera camera, int numberOfButterflies, File wingPicturesDirectory) {
		super("Hands butterfly scene", camera, numberOfButterflies, wingPicturesDirectory);
	}

	@Override
	protected void make(int numberOfButterflies) {
		addEyeCatchingButterflies();
		numberOfButterflies -= getButterflyCount();
		ButterflyDescriptorBuilder builder = new ButterflyDescriptorBuilder();
		List<String> pictureFilePaths = ButterflyFactory.getInstance().getButterflyPictureFilePaths(
				getWingPicturesDirectory());
		int windles = 8;
		double height = 10.0;
		double minRadius = 1.0;
		double maxRadius = 24.0;
		double depthRadiusFactor = 1.0;
		double minPitch = -20.0;
		double maxPitch = 20.0;
		double minRoll = -40.0;
		double maxRoll = 40.0;
		double minYawDeviation = -10;
		double maxYawDeviation = 40;
		double minScale = 2.0;
		double maxScale = 3.0;
		double minFrontWingAngle = -20;
		double maxFrontWingAngle = 60;
		double minBackWingAngleDiff = 1;
		double maxBackWingAngleDiff = 10;
		double minWingOverlapFactor = -0.5;
		double maxWingOverlapFactor = 0;
		int count = 0;
		while (count < numberOfButterflies) {
			double y = height * Math.random();
			double radius = minRadius + y / height * (maxRadius - minRadius);
			radius += 0.1 * radius * (2.0 * Math.random() - 1.0);
			double angleRad = Geometry.degreesToRadians(windles * 360.0 * y / height);
			double x0 = radius * Math.cos(angleRad);
			double z0 = -1.0 * depthRadiusFactor * radius * (1.0 + Math.sin(angleRad)); // keep this on negative side
			double y0 = y + (0.1 * height / windles * (2.0 * Math.random() - 1.0)) - height / 2;
			double pitch = minPitch + Math.random() * (maxPitch - minPitch);
			double roll = minRoll + Math.random() * (maxRoll - minRoll);
			double yaw = Geometry.radiansToDegrees(angleRad) + 90.0 + minYawDeviation + Math.random()
					* (maxYawDeviation - minYawDeviation);
			double scale = minScale + Math.random() * (maxScale - minScale);
			scale *= 0.5 + 0.5 * y / height;
			double frontWingAngle = minFrontWingAngle + Math.random() * (maxFrontWingAngle - minFrontWingAngle);
			double backWingAngle = frontWingAngle
					- (minBackWingAngleDiff + Math.random() * (maxBackWingAngleDiff - minBackWingAngleDiff));
			double wingOverlapFactor = minWingOverlapFactor + Math.random()
					* (maxWingOverlapFactor - minWingOverlapFactor);
			String pictureFilePath = pictureFilePaths.get(count % pictureFilePaths.size());
			ButterflyDescriptor descriptor = builder.withScalingFactor(scale).withPitchAngleInDegrees(pitch)
					.withRollAngleInDegrees(roll).withYawAngleInDegrees(yaw).withPosition(new Point3D(x0, y0, z0))
					.withFrontWingPitchAngleInDegrees(frontWingAngle).withBackWingPitchAngleInDegrees(backWingAngle)
					.withWingOverlapFactor(wingOverlapFactor).withPictureFilePath(pictureFilePath)
					.withWingShadingModel(wingShadingModel).build();
			Butterfly butterfly = ButterflyFactory.getInstance().createButterfly(descriptor);
			if (!overlapsWithButterflyInScene(butterfly)) {
				addButterfly(butterfly);
				count++;
			}
		}
	}

	private void addEyeCatchingButterflies() {
		addButterfly(createFirstEyeCatchingButterflyDescriptor());
		addButterfly(createSecondEyeCatchingButterflyDescriptor());
	}

	private ButterflyDescriptor createFirstEyeCatchingButterflyDescriptor() {
		ButterflyDescriptorBuilder builder = new ButterflyDescriptorBuilder();
		builder.withScalingFactor(2.5);
		builder.withPitchAngleInDegrees(-40.0).withRollAngleInDegrees(20).withYawAngleInDegrees(-60);
		builder.withFrontWingPitchAngleInDegrees(25).withBackWingPitchAngleInDegrees(10);
		builder.withWingOverlapFactor(-0.2);
		builder.withPosition(new Point3D(-1.6, -1.0, 4.0));
		builder.withPictureFilePath(new File(getWingPicturesDirectory(), "special1.jpg").getPath());
		builder.withWingShadingModel(wingShadingModel);
		return builder.build();
	}

	private ButterflyDescriptor createSecondEyeCatchingButterflyDescriptor() {
		ButterflyDescriptorBuilder builder = new ButterflyDescriptorBuilder();
		builder.withScalingFactor(4.0);
		builder.withWingOverlapFactor(0);
		builder.withPitchAngleInDegrees(10.0).withRollAngleInDegrees(-20.0).withYawAngleInDegrees(100.0);
		builder.withFrontWingPitchAngleInDegrees(40).withBackWingPitchAngleInDegrees(25);
		builder.withPosition(new Point3D(4.0, -3.5, 0.0));
		builder.withPictureFilePath(new File(getWingPicturesDirectory(), "special2.jpg").getPath());
		builder.withWingShadingModel(wingShadingModel);
		return builder.build();
	}

}