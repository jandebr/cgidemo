package org.maia.cgi.demo.d3.elephant.model;

import java.util.List;
import java.util.Vector;

import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.PolygonalObject3D;
import org.maia.cgi.model.d3.object.SimpleFace3D;

public class ElephantBuilder {

	private ElephantTheme theme;

	private double precision;

	public ElephantBuilder(ElephantTheme theme, double precision) {
		this.theme = theme;
		this.precision = precision;
	}

	public BaseObject3D build() {
		MultipartObject3D<BaseObject3D> model = new MultipartObject3D<BaseObject3D>();
		model.addPart(buildHead().translateY(1.1));
		model.addPart(buildBody());
		model.addPart(buildTail().translate(-0.3, 0.1, 0));
		return model;
	}

	protected BaseObject3D buildHead() {
		int nv = (int) Math.round(40 + 60 * getPrecision());
		int vertexCount = (int) Math.round(10 + 170 * getPrecision());
		List<PolygonalObject3D> layers = new Vector<PolygonalObject3D>(nv);
		PolygonalObject3D section = null;
		for (Point3D coord : ModelBuilderUtils.smoothenPlanarPolyline(
				ModelBuilderUtils.loadVerticesXY("resources/elephant/head.csv"), nv)) {
			double radius = Math.abs(coord.getY());
			double z = coord.getX();
			double r = z / 13.5;
			double t = Math.max((r - 0.8) / 0.2, 0);
			double squeezeX = 0.5 + 0.5 * t;
			double scaleX = 0.7 + 0.1 * t;
			section = ModelBuilderUtils.buildSqueezedCircularShapeXY(radius, squeezeX, 1.0, vertexCount);
			section.scaleX(scaleX).translateZ(z);
			layers.add(section);
		}
		BaseObject3D headPart = ModelBuilderUtils.buildLayeredObject(layers, true, false,
				getTheme().getBodyPartColor(), getTheme().getBodyPartShadingModel());
		MultipartObject3D<BaseObject3D> head = new MultipartObject3D<BaseObject3D>();
		head.addPart(headPart);
		head.addPart(new SimpleFace3D(getTheme().getEyeColor(), getTheme().getEyeShadingModel(), section
				.getVerticesInWorldCoordinates()).scale(0.7, 0.7, 1.0).translateZ(0.0001));
		head.addPart(buildEye().translate(0.7 * 1.75, 0, 4.75));
		head.addPart(buildEye().translate(-0.7 * 1.75, 0, 4.75));
		head.addPart(buildEar().rotateY(Geometry.degreesToRadians(-70.0)).translate(-3.0, -0.5, 1.0));
		head.addPart(buildEar().rotateY(Geometry.degreesToRadians(-110.0)).translate(3.0, -0.5, 1.0));
		head.translateZ(-1.5).scale(0.1).rotateX(Geometry.degreesToRadians(40.0))
				.rotateY(Geometry.degreesToRadians(90.0));
		return head;
	}

	protected BaseObject3D buildEye() {
		return ModelBuilderUtils.buildSphere(0.3, 24, getTheme().getEyeColor(), getTheme().getEyeShadingModel());
	}

	protected BaseObject3D buildEar() {
		double thickness = 0.24;
		double angleFrom = Geometry.degreesToRadians(35.0);
		double angleTo = 2 * Math.PI - angleFrom;
		int vertexCount = 10 + (int) (170 * getPrecision());
		PolygonalObject3D section = ModelBuilderUtils.buildCircularSegmentXY(1.0, angleFrom, angleTo, vertexCount);
		BaseObject3D outerEar = ModelBuilderUtils.buildExtrusion(section, thickness, getTheme().getOuterEarColor(),
				getTheme().getEarShadingModel());
		BaseObject3D innerEar1 = (BaseObject3D) new SimpleFace3D(getTheme().getInnerEarColor(), getTheme()
				.getEarShadingModel(), section.getVerticesInWorldCoordinates()).scale(0.7).translateX(0.15)
				.translateZ(thickness + 0.0001);
		BaseObject3D innerEar2 = (BaseObject3D) new SimpleFace3D(getTheme().getInnerEarColor(), getTheme()
				.getEarShadingModel(), section.getVerticesInWorldCoordinates()).scale(0.7).translateX(0.15)
				.translateZ(-0.0001);
		MultipartObject3D<BaseObject3D> ear = new MultipartObject3D<BaseObject3D>();
		ear.addPart(outerEar);
		ear.addPart(innerEar1);
		ear.addPart(innerEar2);
		ear.translateZ(-thickness / 2);
		ear.scale(3.5);
		return ear;
	}

	protected BaseObject3D buildBody() {
		int nv = (int) Math.round(40 + 60 * getPrecision());
		int vertexCount = (int) Math.round(10 + 170 * getPrecision());
		List<PolygonalObject3D> layers = new Vector<PolygonalObject3D>(20);
		double radiusCutoff = 2.0;
		for (Point3D coord : ModelBuilderUtils.smoothenPlanarPolyline(
				ModelBuilderUtils.loadVerticesXY("resources/elephant/body.csv"), nv)) {
			double radius = Math.abs(coord.getX());
			double z = Math.abs(coord.getY());
			radius *= 0.9 + 0.1 * Math.sin(z / 9.0 * Math.PI);
			PolygonalObject3D section = null;
			if (radius > radiusCutoff) {
				double angleFrom = Math.acos(radiusCutoff / radius);
				double angleTo = 2 * Math.PI - angleFrom;
				section = ModelBuilderUtils.buildCircularSegmentXY(radius, angleFrom, angleTo, vertexCount);
			} else {
				section = ModelBuilderUtils.buildCircularShapeXY(radius, vertexCount);
			}
			section.scale(1.1).translateZ(z);
			layers.add(section);
		}
		BaseObject3D bodyPart = ModelBuilderUtils.buildLayeredObject(layers, true, true, getTheme().getBodyPartColor(),
				getTheme().getBodyPartShadingModel());
		MultipartObject3D<BaseObject3D> body = new MultipartObject3D<BaseObject3D>();
		body.addPart(bodyPart);
		// front feet
		body.addPart(buildFoot().rotateY(Geometry.degreesToRadians(90.0)).translate(0.8, 1.2, 4.0));
		body.addPart(buildFoot().rotateY(Geometry.degreesToRadians(90.0)).translate(0.8, -1.2, 4.0));
		// back feet
		body.addPart(buildFoot().rotateY(Geometry.degreesToRadians(80.0)).rotateZ(Geometry.degreesToRadians(-24.0))
				.translate(1.0, 2.6, 1.1));
		body.addPart(buildFoot().rotateY(Geometry.degreesToRadians(80.0)).rotateZ(Geometry.degreesToRadians(24.0))
				.translate(1.0, -2.6, 1.1));
		body.scale(0.1).rotateX(Geometry.degreesToRadians(-90.0));
		return body;
	}

	protected BaseObject3D buildFoot() {
		int vertexCount = (int) Math.round(10 + 170 * getPrecision());
		double length = 3.0;
		PolygonalObject3D section = ModelBuilderUtils.buildCircularShapeXY(1.0, vertexCount);
		BaseObject3D outerFoot = ModelBuilderUtils.buildExtrusion(section, length, getTheme().getOuterFootColor(),
				getTheme().getFootShadingModel());
		BaseObject3D innerFoot = (BaseObject3D) new SimpleFace3D(getTheme().getInnerFootColor(), getTheme()
				.getFootShadingModel(), ModelBuilderUtils.buildCircularShapeXY(0.7, vertexCount)
				.getVerticesInWorldCoordinates()).translateZ(length + 0.0001);
		MultipartObject3D<BaseObject3D> foot = new MultipartObject3D<BaseObject3D>();
		foot.addPart(outerFoot);
		foot.addPart(innerFoot);
		foot.addPart(buildToe().translate(-0.7, 0, length + 0.0002));
		foot.addPart(buildToe().translate(-0.55, -0.6, length + 0.0002));
		foot.addPart(buildToe().translate(-0.55, 0.6, length + 0.0002));
		foot.scale(1.0);
		return foot;
	}

	protected BaseObject3D buildToe() {
		BaseObject3D toe = (BaseObject3D) new SimpleFace3D(getTheme().getToeColor(), getTheme().getFootShadingModel(),
				ModelBuilderUtils.buildCircularShapeXY(0.25, 24).getVerticesInWorldCoordinates());
		return toe;
	}

	protected BaseObject3D buildTail() {
		int n = (int) Math.round(10 + 40 * getPrecision());
		double angleFrom = Geometry.degreesToRadians(70);
		double angleTo = Geometry.degreesToRadians(360);
		List<Point3D> path = new Vector<Point3D>(n);
		for (int i = 0; i < n; i++) {
			double angle = angleFrom + (angleTo - angleFrom) * i / (n - 1);
			double x = 0;
			double y = 0.5 * Math.sin(angle);
			double z = -5.0 * (n - 1 - i) / (n - 1);
			path.add(new Point3D(x, y, z));
		}
		PolygonalObject3D section = ModelBuilderUtils.buildRoundedRectangleXY(0.6, 0.15, 0.02, 0.02, getPrecision());
		BaseObject3D tail = ModelBuilderUtils.buildExtrusionAlongPath(section, path, getTheme().getTailColor(),
				getTheme().getTailShadingModel());
		tail.scale(0.1);
		tail.rotateY(Geometry.degreesToRadians(90.0));
		return tail;
	}

	public ElephantTheme getTheme() {
		return theme;
	}

	public double getPrecision() {
		return precision;
	}

}