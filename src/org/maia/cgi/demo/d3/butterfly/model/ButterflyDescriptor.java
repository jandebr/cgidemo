package org.maia.cgi.demo.d3.butterfly.model;

import org.maia.cgi.demo.d3.butterfly.shading.ButterflyWingShadingModel;
import org.maia.cgi.geometry.d3.Point3D;

public interface ButterflyDescriptor {

	double getWingPitchAngleInDegrees(WingType wingType);

	double getWingOverlapFactor();

	double getScalingFactor();

	double getPitchAngleInDegrees();

	double getRollAngleInDegrees();

	double getYawAngleInDegrees();

	Point3D getPosition();

	String getPictureFilePath();

	ButterflyWingShadingModel getWingShadingModel();

}