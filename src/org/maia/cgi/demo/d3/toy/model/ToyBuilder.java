package org.maia.cgi.demo.d3.toy.model;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.maia.cgi.demo.d3.elephant.model.ElephantBuilder;
import org.maia.cgi.demo.d3.elephant.model.ElephantTheme;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Box3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.CoordinateFrame;
import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.PolygonalObject3D;
import org.maia.cgi.model.d3.object.SimpleFace3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D.PictureRegion;
import org.maia.cgi.render.d2.ImageTextureMapFileHandle;
import org.maia.cgi.render.d2.TextureMapHandle;
import org.maia.cgi.render.d3.shading.FlatShadingModel;

public class ToyBuilder {

	private ToyTheme theme;

	private double precision;

	public ToyBuilder(ToyTheme theme, double precision) {
		this.theme = theme;
		this.precision = precision;
	}

	public BaseObject3D build() {
		BaseObject3D toy = buildToy();
		BaseObject3D floor = buildFloor(toy);
		double yFloor = floor.getBoundingBox(CoordinateFrame.WORLD, null).getY1();
		BaseObject3D cube = buildCube(yFloor);
		BaseObject3D elephant = buildElephant(cube);
		MultipartObject3D<BaseObject3D> model = new MultipartObject3D<BaseObject3D>();
		model.addPart(toy);
		model.addPart(floor);
		model.addPart(cube);
		model.addPart(elephant);
		return model;
	}

	public BaseObject3D buildToy() {
		MultipartObject3D<BaseObject3D> toy = new MultipartObject3D<BaseObject3D>();
		toy.addPart(buildToyHead());
		toy.addPart(buildToyBodyPart1());
		toy.addPart(buildToyBodyPart2());
		toy.addPart(buildToyBodyPart3());
		toy.addPart(buildToyBodyPart4());
		toy.addPart(buildToyBodyPartConnectionStrip());
		toy.addPart(buildToyWheelFrontRight());
		toy.addPart(buildToyWheelFrontLeft());
		toy.addPart(buildToyWheelBackRight());
		toy.addPart(buildToyWheelBackLeft());
		toy.addPart(buildToyWheelRodFront());
		toy.addPart(buildToyWheelRodBack());
		toy.addPart(buildToyNeckRing());
		toy.addPart(buildToyTail());
		toy.addPart(buildToyTailCord());
		toy.addPart(buildToyLeftEar());
		toy.addPart(buildToyRightEar());
		toy.translate(1.1, -0.9, -0.5);
		return toy;
	}

	protected BaseObject3D buildToyWheelFrontRight() {
		BaseObject3D wheel = buildToyWheel();
		wheel.rotateY(Geometry.degreesToRadians(64.0));
		wheel.translate(-1.578, 0, 0.099);
		return wheel;
	}

	protected BaseObject3D buildToyWheelFrontLeft() {
		BaseObject3D wheel = buildToyWheel();
		wheel.rotateY(Geometry.degreesToRadians(64.0));
		wheel.translate(-0.697, 0, 0.529);
		return wheel;
	}

	protected BaseObject3D buildToyWheelBackRight() {
		BaseObject3D wheel = buildToyWheel();
		wheel.translate(1.011 - 0.1, 0, -1.303);
		return wheel;
	}

	protected BaseObject3D buildToyWheelBackLeft() {
		MultipartObject3D<BaseObject3D> wheel = new MultipartObject3D<BaseObject3D>();
		wheel.addPart(buildToyWheel());
		wheel.addPart(new SimpleTexturedFace3D(getTheme().getWheelShadingModel(), new ImageTextureMapFileHandle(
				"resources/toy/trademark-450x450.png"), new PictureRegion(450, 450)).scale(0.2, 1.0, 0.2)
				.rotateX(Geometry.degreesToRadians(90.0)).translateZ(0.38 / 2 + 0.0001));
		wheel.translate(1.011 + 0.1, 0, -0.323);
		return wheel;
	}

	protected BaseObject3D buildToyWheel() {
		int vertexCount = 10 + (int) (170 * getPrecision());
		BaseObject3D wheel = ModelBuilderUtils.buildExtrusionWithRoundedSides(ModelBuilderUtils.buildCircularShapeXY(
				1.0, vertexCount), 0.2, getPrecision(), getTheme().getWheelColor(), getTheme().getWheelShadingModel());
		wheel.scale(0.90, 0.90, 0.38);
		return wheel;
	}

	protected BaseObject3D buildToyWheelRodFront() {
		List<Point3D> path = new Vector<Point3D>(2);
		path.add(new Point3D(-0.697, 0, 0.529));
		path.add(new Point3D(-1.578, 0, 0.099));
		return ModelBuilderUtils.buildExtrusionAlongPath(ModelBuilderUtils.buildCircularShapeXY(0.1, 24), path,
				getTheme().getWheelRodColor(), getTheme().getWheelRodShadingModel());
	}

	protected BaseObject3D buildToyWheelRodBack() {
		List<Point3D> path = new Vector<Point3D>(2);
		path.add(new Point3D(1.1, 0, -0.323));
		path.add(new Point3D(1.1, 0, -1.303));
		return ModelBuilderUtils.buildExtrusionAlongPath(ModelBuilderUtils.buildCircularShapeXY(0.1, 24), path,
				getTheme().getWheelRodColor(), getTheme().getWheelRodShadingModel());
	}

	protected BaseObject3D buildToyBodyPart1() {
		BaseObject3D bodyPart = buildToyBodyPart("resources/toy/bodypart1.csv");
		bodyPart.scale(0.97, 1.27, 0.48);
		bodyPart.rotateY(Geometry.degreesToRadians(64.0));
		bodyPart.translate(-1.137, 0.440, 0.314);
		return bodyPart;
	}

	protected BaseObject3D buildToyBodyPart2() {
		BaseObject3D bodyPart = buildToyBodyPart("resources/toy/bodypart2.csv");
		bodyPart.scale(0.69, 0.98, 0.48);
		bodyPart.rotateY(Geometry.degreesToRadians(32.0));
		bodyPart.translate(-0.552, 0.400, -0.394);
		return bodyPart;
	}

	protected BaseObject3D buildToyBodyPart3() {
		BaseObject3D bodyPart = buildToyBodyPart("resources/toy/bodypart3.csv");
		bodyPart.scale(0.70, 0.90, 0.48);
		bodyPart.rotateY(Geometry.degreesToRadians(16.0));
		bodyPart.translate(0.150, 0.520, -0.706);
		return bodyPart;
	}

	protected BaseObject3D buildToyBodyPart4() {
		BaseObject3D bodyPart = buildToyBodyPart("resources/toy/bodypart4.csv");
		bodyPart.scale(0.89, 1.18, 0.48);
		bodyPart.translate(1.011, 0.380, -0.813);
		return bodyPart;
	}

	protected BaseObject3D buildToyBodyPart(String shapeFilePath) {
		return ModelBuilderUtils.buildExtrusionWithRoundedSides(ModelBuilderUtils.loadShapeXY(shapeFilePath), 0.2,
				getPrecision(), getTheme().getBodyPartColor(), getTheme().getBodyPartShadingModel());
	}

	protected BaseObject3D buildToyBodyPartConnectionStrip() {
		Color color = getTheme().getBodyPartConnectorStripColor();
		FlatShadingModel shadingModel = getTheme().getBodyPartConnectorStripShadingModel();
		MultipartObject3D<BaseObject3D> strip = new MultipartObject3D<BaseObject3D>();
		strip.addPart(new SimpleFace3D(color, shadingModel, new Point3D(-1.137, -0.1, 0.314), new Point3D(-0.925, -0.1,
				-0.122), new Point3D(-0.925, 0.65, -0.122), new Point3D(-1.137, 0.65, 0.314)));
		strip.addPart(new SimpleFace3D(color, shadingModel, new Point3D(-0.925, -0.1, -0.122), new Point3D(-0.844,
				-0.08, -0.211), new Point3D(-0.844, 0.67, -0.211), new Point3D(-0.925, 0.65, -0.122)));
		strip.addPart(new SimpleFace3D(color, shadingModel, new Point3D(-0.844, -0.08, -0.211), new Point3D(-0.259,
				0.08, -0.577), new Point3D(-0.259, 0.69, -0.577), new Point3D(-0.844, 0.67, -0.211)));
		strip.addPart(new SimpleFace3D(color, shadingModel, new Point3D(-0.259, 0.08, -0.577), new Point3D(-0.186, 0.1,
				-0.609), new Point3D(-0.186, 0.71, -0.609), new Point3D(-0.259, 0.69, -0.577)));
		strip.addPart(new SimpleFace3D(color, shadingModel, new Point3D(-0.186, 0.1, -0.609), new Point3D(0.487, 0.3,
				-0.802), new Point3D(0.487, 0.8, -0.802), new Point3D(-0.186, 0.71, -0.609)));
		strip.addPart(new SimpleFace3D(color, shadingModel, new Point3D(0.487, 0.3, -0.802), new Point3D(0.566, 0.25,
				-0.813), new Point3D(0.566, 0.8, -0.813), new Point3D(0.487, 0.8, -0.802)));
		strip.addPart(new SimpleFace3D(color, shadingModel, new Point3D(0.566, 0.25, -0.813), new Point3D(1.011, 0,
				-0.813), new Point3D(1.011, 0.7, -0.813), new Point3D(0.566, 0.8, -0.813)));
		return strip;
	}

	protected BaseObject3D buildToyHead() {
		int nlayers = 10 + (int) (50 * getPrecision());
		int vertexCount = 10 + (int) (170 * getPrecision());
		double e = 0.005;
		double hb = 0.9;
		double hz0 = -0.3;
		double hz1 = 0.8;
		double ha = hb / ((hz1 - hz0) * (hz1 - hz0));
		Color color = getTheme().getBodyPartColor();
		FlatShadingModel shadingModel = getTheme().getBodyPartShadingModel();
		MultipartObject3D<BaseObject3D> carveOutShape = new MultipartObject3D<BaseObject3D>();
		List<PolygonalObject3D> layers = new Vector<PolygonalObject3D>(nlayers);
		boolean previousLayerCarvedOut = false;
		PolygonalObject3D previousPolygon = null;
		for (int li = 0; li < nlayers; li++) {
			PolygonalObject3D polygon = null;
			double z = li / (double) (nlayers - 1) * (2.0 - 2 * e) - 1.0 + e;
			double radius = Math.cos(Math.asin(z));
			double xc = 1.0 - (hb - ha * (z - hz1) * (z - hz1));
			if (xc < radius) {
				// carve-out
				double yc = Math.sqrt(radius * radius - xc * xc);
				double anglec0 = Math.atan(yc / xc);
				double anglec1 = 2 * Math.PI - anglec0;
				polygon = (PolygonalObject3D) ModelBuilderUtils.buildCircularSegmentXY(radius, anglec0, anglec1,
						vertexCount).translateZ(z);
				Point3D p0 = polygon.getVerticesInWorldCoordinates().get(0);
				Point3D p1 = polygon.getVerticesInWorldCoordinates().get(polygon.getVertexCount() - 1);
				if (!previousLayerCarvedOut && previousPolygon != null) {
					Point3D q0 = previousPolygon.getVerticesInWorldCoordinates().get(0);
					carveOutShape.addPart(new SimpleFace3D(color, shadingModel, p0, p1, q0));
				} else if (previousLayerCarvedOut) {
					Point3D q0 = previousPolygon.getVerticesInWorldCoordinates().get(0);
					Point3D q1 = previousPolygon.getVerticesInWorldCoordinates().get(
							previousPolygon.getVertexCount() - 1);
					carveOutShape.addPart(new SimpleFace3D(color, shadingModel, p0, p1, q1, q0));
				}
				previousLayerCarvedOut = true;
			} else {
				polygon = (PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(radius, vertexCount).translateZ(z);
				if (previousLayerCarvedOut) {
					Point3D p0 = polygon.getVerticesInWorldCoordinates().get(0);
					Point3D q0 = previousPolygon.getVerticesInWorldCoordinates().get(0);
					Point3D q1 = previousPolygon.getVerticesInWorldCoordinates().get(
							previousPolygon.getVertexCount() - 1);
					carveOutShape.addPart(new SimpleFace3D(color, shadingModel, p0, q0, q1));
				}
				previousLayerCarvedOut = false;
			}
			layers.add(polygon);
			previousPolygon = polygon;
		}
		BaseObject3D hull = ModelBuilderUtils.buildLayeredObject(layers, true, true, color, shadingModel);
		return assembleToyHead(hull, carveOutShape);
	}

	protected BaseObject3D assembleToyHead(BaseObject3D hull, MultipartObject3D<BaseObject3D> carveOutShape) {
		MultipartObject3D<BaseObject3D> head = new MultipartObject3D<BaseObject3D>();
		head.addPart(hull);
		head.addPart(carveOutShape);
		head.addPart(buildToyNose());
		head.addPart(buildToyEyes(carveOutShape));
		head.addPart(buildToyMouth());
		head.scale(0.4, 0.4, 0.6);
		head.rotateZ(Geometry.degreesToRadians(110)).rotateX(Geometry.degreesToRadians(-10));
		head.translate(-1.456, 1.5, 0.968);
		return head;
	}

	protected BaseObject3D buildToyNose() {
		BaseObject3D nose = ModelBuilderUtils.buildSphere(0.12, 24, getTheme().getNoseColor(), getTheme()
				.getNoseShadingModel());
		nose.scale(1.0, 1.0, 0.2).translate(-0.05, 0, 1.0);
		return nose;
	}

	protected BaseObject3D buildToyEyes(MultipartObject3D<BaseObject3D> carveOutShape) {
		Color color = getTheme().getEyeColor();
		FlatShadingModel shadingModel = getTheme().getEyeShadingModel();
		SimpleFace3D baseFace = selectEyesBaseFace(carveOutShape);
		List<Point3D> vertices = baseFace.getVerticesInWorldCoordinates();
		Point3D p0 = Point3D.centerOfTwo(vertices.get(0), vertices.get(3));
		Point3D p1 = Point3D.centerOfTwo(vertices.get(1), vertices.get(2));
		Point3D c0 = Point3D.interpolateBetween(p0, p1, 0.2);
		Point3D c1 = Point3D.interpolateBetween(p0, p1, 0.8);
		double angleY = 2 * Math.PI - vertices.get(3).minus(vertices.get(0)).getLongitudeInRadians();
		MultipartObject3D<BaseObject3D> eyes = new MultipartObject3D<BaseObject3D>();
		eyes.addPart(ModelBuilderUtils.buildSphere(0.1, 24, color, shadingModel).scale(2.0, 1.0, 0.2).rotateY(angleY)
				.translate(c0.getX(), c0.getY(), c0.getZ()));
		eyes.addPart(ModelBuilderUtils.buildSphere(0.1, 24, color, shadingModel).scale(2.0, 1.0, 0.2).rotateY(angleY)
				.translate(c1.getX(), c1.getY(), c1.getZ()));
		return eyes;
	}

	protected SimpleFace3D selectEyesBaseFace(MultipartObject3D<BaseObject3D> carveOutShape) {
		SimpleFace3D baseFace = null;
		double zBaseFace = 0;
		double zTarget = 0.2; // select the face which is closest to this depth
		for (BaseObject3D object : carveOutShape.getParts()) {
			SimpleFace3D face = (SimpleFace3D) object;
			if (face.getVertexCount() == 4) {
				List<Point3D> vertices = face.getVerticesInWorldCoordinates();
				double zFace = (vertices.get(0).getZ() + vertices.get(3).getZ()) / 2;
				if (baseFace == null || Math.abs(zFace - zTarget) < Math.abs(zBaseFace - zTarget)) {
					baseFace = face;
					zBaseFace = zFace;
				}
			}
		}
		return baseFace;
	}

	protected BaseObject3D buildToyMouth() {
		Color color = getTheme().getMouthColor();
		FlatShadingModel shadingModel = getTheme().getMouthShadingModel();
		PolygonalObject3D shapeXY = ModelBuilderUtils.buildCircularShapeXY(0.02, 6);
		PolygonalObject3D pathObjectLeft = ModelBuilderUtils.loadShapeXY("resources/toy/mouth.csv");
		MultipartObject3D<BaseObject3D> mouth = new MultipartObject3D<BaseObject3D>();
		for (int i = 0; i < 2; i++) {
			PolygonalObject3D pathObject = ModelBuilderUtils.cloneShape(pathObjectLeft);
			ModelBuilderUtils.stretchToUnityBoundingBox(pathObject);
			if (i == 0) {
				// left
				pathObject.translateX(-0.5).scaleX(0.23).scaleY(0.25).rotateZ(-Math.PI / 2).translateX(-0.4);
			} else {
				// right
				pathObject.scaleX(-1.0).translateX(0.5).scaleX(0.23).scaleY(0.25).rotateZ(-Math.PI / 2)
						.translateX(-0.4);
			}
			List<Point3D> path = pathObject.getVerticesInWorldCoordinates();
			for (Point3D p : path) {
				p.setZ(Math.sqrt(1.0 - p.getX() * p.getX() - p.getY() * p.getY()));
			}
			mouth.addPart(ModelBuilderUtils.buildExtrusionAlongPath(shapeXY, path, color, shadingModel));
		}
		return mouth;
	}

	protected BaseObject3D buildToyNeckRing() {
		int vertexCount = 10 + (int) (170 * getPrecision());
		BaseObject3D ring = ModelBuilderUtils.buildExtrusionWithRoundedSides(ModelBuilderUtils.buildCircularShapeXY(
				1.0, vertexCount), 0.65, getPrecision(), getTheme().getNeckRingColor(), getTheme()
				.getNeckRingShadingModel());
		ring.scale(0.7, 0.7, 0.22);
		ring.rotateX(Math.PI / 2).rotateZ(Geometry.degreesToRadians(26)).rotateY(Geometry.degreesToRadians(64.0));
		ring.translate(-1.264, 1.11, 0.5745);
		return ring;
	}

	protected BaseObject3D buildToyTail() {
		int vertexCount = 10 + (int) (170 * getPrecision());
		List<PolygonalObject3D> layers = new Vector<PolygonalObject3D>();
		layers.add(ModelBuilderUtils.buildCircularShapeXY(0.85, vertexCount));
		layers.add((PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(1.35, vertexCount).translateZ(0.28));
		layers.add((PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(1.70, vertexCount).translateZ(0.8));
		layers.add((PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(1.88, vertexCount).translateZ(1.5));
		layers.add((PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(1.95, vertexCount).translateZ(2));
		layers.add((PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(0.58, vertexCount).translateZ(6.7));
		layers.add((PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(0.45, vertexCount).translateZ(6.95));
		layers.add((PolygonalObject3D) ModelBuilderUtils.buildCircularShapeXY(0.25, vertexCount).translateZ(7.05));
		BaseObject3D tail = ModelBuilderUtils.buildLayeredObject(layers, true, false, getTheme().getTailColor(),
				getTheme().getTailShadingModel());
		tail.scale(0.15 / 1.95, 0.15 / 1.95, 0.76 / 7.05);
		tail.rotateY(Geometry.degreesToRadians(90));
		tail.rotateZ(Geometry.degreesToRadians(12));
		tail.translate(0.98, 0.97, -0.813);
		return tail;
	}

	protected BaseObject3D buildToyTailCord() {
		List<Point3D> path = new Vector<Point3D>(2);
		path.add(new Point3D(0.91, 0.89, -0.813));
		path.add(new Point3D(1.06, 0.98, -0.813));
		return ModelBuilderUtils.buildExtrusionAlongPath(ModelBuilderUtils.buildCircularShapeXY(0.02, 16), path,
				getTheme().getTailCordColor(), getTheme().getTailCordShadingModel());
	}

	protected BaseObject3D buildToyLeftEar() {
		BaseObject3D ear = buildToyEar();
		ear.rotateX(Geometry.degreesToRadians(-85)).rotateZ(Geometry.degreesToRadians(20))
				.rotateX(Geometry.degreesToRadians(-10));
		ear.translate(-1.4, 1.9, 0.7);
		return ear;
	}

	protected BaseObject3D buildToyRightEar() {
		BaseObject3D ear = buildToyEar();
		ear.rotateX(Geometry.degreesToRadians(-98)).scaleX(-1.0).rotateZ(Geometry.degreesToRadians(20))
				.rotateX(Geometry.degreesToRadians(-10));
		ear.translate(-1.8, 1.7, 0.8);
		return ear;
	}

	protected BaseObject3D buildToyEar() {
		MultipartObject3D<BaseObject3D> ear = new MultipartObject3D<BaseObject3D>();
		ear.addPart(buildToyEarSide(getTheme().getEarInsideColor()));
		ear.addPart(buildToyEarSide(getTheme().getEarOutsideColor()).translateZ(0.001));
		return ear;
	}

	protected BaseObject3D buildToyEarSide(Color color) {
		FlatShadingModel shadingModel = getTheme().getEarShadingModel();
		List<Point3D> innerVertices = ModelBuilderUtils.loadVerticesXY("resources/toy/ear-inner.csv");
		List<Point3D> midVertices = ModelBuilderUtils.loadVerticesXY("resources/toy/ear-mid.csv");
		List<Point3D> outerVertices = ModelBuilderUtils.loadVerticesXY("resources/toy/ear-outer.csv");
		List<Point3D> innerVerticesCC = new Vector<Point3D>(innerVertices);
		Collections.reverse(innerVerticesCC);
		MultipartObject3D<BaseObject3D> ear = new MultipartObject3D<BaseObject3D>();
		ear.addPart(new SimpleFace3D(color, shadingModel, innerVerticesCC));
		int n = midVertices.size();
		for (int k = 0; k <= 1; k++) {
			List<Point3D> v1 = (k == 0 ? innerVertices : midVertices);
			List<Point3D> v2 = (k == 0 ? midVertices : outerVertices);
			double zFactor1 = (k == 0 ? 0 : 0.3);
			double zFactor2 = (k == 0 ? 0.3 : 1.0);
			for (int i = 0; i < n; i++) {
				int j = (i + 1) % n;
				ear.addPart(new SimpleFace3D(color, shadingModel, bendEarVertexInDepth(v1.get(i), zFactor1),
						bendEarVertexInDepth(v1.get(j), zFactor1), bendEarVertexInDepth(v2.get(i), zFactor2)));
				ear.addPart(new SimpleFace3D(color, shadingModel, bendEarVertexInDepth(v1.get(j), zFactor1),
						bendEarVertexInDepth(v2.get(j), zFactor2), bendEarVertexInDepth(v2.get(i), zFactor2)));
			}
		}
		ear.translate(-30.0, 198.0, 0);
		ear.scale(1.08 / 787, 0.84 / 550, 0.28);
		return ear;
	}

	protected Point3D bendEarVertexInDepth(Point3D vertex, double factor) {
		double e1 = 3.0;
		double e2 = 1.2;
		double zmax1 = -factor;
		double zmax2 = zmax1 / 5.0;
		double xn0 = 0.3;
		double xn = (vertex.getX() - 427.5) / 787 + 0.5;
		double t = (xn <= xn0 ? xn / xn0 * 0.5 : 0.5 + (xn - xn0) / (1.0 - xn0) * 0.5);
		double z1 = zmax1 * Math.pow(1.0 - xn, e1);
		double z2 = zmax2 * Math.pow(xn, e2);
		double z = (1.0 - t) * z1 + t * z2;
		return new Point3D(vertex.getX(), vertex.getY(), z);
	}

	protected BaseObject3D buildFloor(BaseObject3D toy) {
		MultipartObject3D<BaseObject3D> floor = new MultipartObject3D<BaseObject3D>();
		Box3D bbox = toy.getBoundingBox(CoordinateFrame.WORLD, null);
		double y = bbox.getY1();
		double x1 = bbox.getX1() - 4.0;
		double x2 = bbox.getX2() + 3.0;
		double z1 = bbox.getZ1() - 2.0;
		double z2 = bbox.getZ2() + 2.0;
		double tileSize = 2.5;
		FlatShadingModel shadingModel = getTheme().getFloorShadingModel();
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle("resources/toy/tile02-320x320.png");
		for (double x = x1; x <= x2 - tileSize; x += tileSize) {
			for (double z = z1; z <= z2 - tileSize; z += tileSize) {
				BaseObject3D tile = new SimpleTexturedFace3D(shadingModel, pictureMapHandle,
						new PictureRegion(320, 320));
				tile.scaleX(tileSize / 2).scaleZ(tileSize / 2);
				tile.translate(x + tileSize / 2, y, z + tileSize / 2);
				floor.addPart(tile);
			}
		}
		return floor;
	}

	protected BaseObject3D buildCube(double yFloor) {
		double size = 1.0;
		Point3D position = new Point3D(-1.8, yFloor + size / 2, 0.2);
		MultipartObject3D<BaseObject3D> cube = new MultipartObject3D<BaseObject3D>();
		cube.addPart(buildBaseCube(position, size));
		cube.addPart(buildCubeSides(position, size));
		return cube;
	}

	protected BaseObject3D buildBaseCube(Point3D position, double size) {
		double roundness = 0.05;
		PolygonalObject3D face = ModelBuilderUtils.buildRoundedRectangleXY(size, size, size * roundness, size
				* roundness, getPrecision());
		BaseObject3D cube = ModelBuilderUtils.buildExtrusionWithRoundedSides(face, roundness, getPrecision(),
				getTheme().getCubeColor(), getTheme().getCubeShadingModel());
		// position
		cube.rotateY(Geometry.degreesToRadians(-20.0));
		cube.translate(position.minus(Point3D.origin()));
		return cube;
	}

	protected BaseObject3D buildCubeSides(Point3D position, double size) {
		FlatShadingModel shadingModel = getTheme().getFloorShadingModel();
		MultipartObject3D<BaseObject3D> sides = new MultipartObject3D<BaseObject3D>();
		// top side
		sides.addPart((BaseObject3D) new SimpleTexturedFace3D(shadingModel, new ImageTextureMapFileHandle(
				"resources/toy/cube-side02-80x80.png"), new PictureRegion(80, 80)).scale(size * 0.45)
				.translateY(size / 2 + 0.0001).rotateY(Geometry.degreesToRadians(-20.0))
				.translate(position.minus(Point3D.origin())));
		// right side
		sides.addPart((BaseObject3D) new SimpleTexturedFace3D(shadingModel, new ImageTextureMapFileHandle(
				"resources/toy/cube-side01-80x80.png"), new PictureRegion(80, 80)).scale(size * 0.45)
				.rotateX(Geometry.degreesToRadians(90.0)).rotateY(Geometry.degreesToRadians(90.0))
				.translateX(size / 2 + 0.0001).rotateY(Geometry.degreesToRadians(-20.0))
				.translate(position.minus(Point3D.origin())));
		// front side
		sides.addPart((BaseObject3D) new SimpleTexturedFace3D(shadingModel, new ImageTextureMapFileHandle(
				"resources/toy/cube-side03-80x80.png"), new PictureRegion(80, 80)).scale(size * 0.45)
				.rotateX(Geometry.degreesToRadians(90.0)).translateZ(size / 2 + 0.0001)
				.rotateY(Geometry.degreesToRadians(-20.0)).translate(position.minus(Point3D.origin())));
		return sides;
	}

	protected BaseObject3D buildElephant(BaseObject3D cube) {
		Box3D bbox = cube.getBoundingBox(CoordinateFrame.WORLD, null);
		double xc = 0.5 * bbox.getX1() + 0.5 * bbox.getX2();
		double yc = bbox.getY2();
		double zc = 0.3 * bbox.getZ1() + 0.7 * bbox.getZ2();
		ElephantTheme theme = new ElephantTheme();
		ElephantBuilder builder = new ElephantBuilder(theme, getPrecision());
		BaseObject3D elephant = builder.build();
		elephant.scale(0.45).rotateY(Geometry.degreesToRadians(-20.0)).translate(xc, yc, zc);
		return elephant;
	}

	public ToyTheme getTheme() {
		return theme;
	}

	public double getPrecision() {
		return precision;
	}

}