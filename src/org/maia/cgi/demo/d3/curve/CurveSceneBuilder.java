package org.maia.cgi.demo.d3.curve;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.ApproximatingCurve3D;
import org.maia.cgi.geometry.d3.Curve3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Trajectory3D;
import org.maia.cgi.geometry.d3.Trajectory3D.Location;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.object.SimpleFace3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.render.d3.shading.FlatShadingModel;
import org.maia.cgi.render.d3.shading.FlatShadingModelImpl;

public class CurveSceneBuilder extends SceneBuilder {

	public CurveSceneBuilder() {
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(940);
		options.setRenderHeight(700);
		return options;
	}

	@Override
	protected String getSceneName() {
		return "Curve";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(Point3D.origin(), new Point3D(0, 0, 4.0), 50.0,
				options.getAspectRatio(), 0.1, 10.0);
		camera.revolveLongitudinal(Geometry.degreesToRadians(50.0));
		return camera;
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.2));
		lights.add(new SpotLight(new Point3D(1.0, 1.0, 1.0), 0.8));
		lights.add(new SpotLight(new Point3D(1.0, 1.0, -1.0), 0.8));
		return lights;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		Trajectory3D trajectory = createTrajectory();
		objects.addAll(buildPathSegments(trajectory, 0.06));
		return objects;
	}

	private Collection<Object3D> buildPathSegments(Trajectory3D trajectory, double thickness) {
		int n = 2000;
		Collection<Object3D> segments = new Vector<Object3D>(n);
		Location loc0 = trajectory.moveToStart();
		double delta = 1.0 / n;
		for (int i = 0; i < n; i++) {
			Location loc1 = trajectory.advance(delta);
			segments.add(buildPathSegment(buildPathIntersectionVertices(loc0, thickness),
					buildPathIntersectionVertices(loc1, thickness)));
			loc0 = loc1;
		}
		return segments;
	}

	private Object3D buildPathSegment(List<Point3D> verticesFrom, List<Point3D> verticesTo) {
		FlatShadingModel shadingModel = new FlatShadingModelImpl();
		MultipartObject3D<SimpleFace3D> faces = new MultipartObject3D<SimpleFace3D>();
		faces.addPart(new SimpleFace3D(Color.RED, shadingModel, verticesFrom.get(3), verticesTo.get(3), verticesTo
				.get(0), verticesFrom.get(0))); // right
		faces.addPart(new SimpleFace3D(Color.YELLOW, shadingModel, verticesFrom.get(0), verticesTo.get(0), verticesTo
				.get(1), verticesFrom.get(1))); // top
		faces.addPart(new SimpleFace3D(Color.GREEN, shadingModel, verticesFrom.get(2), verticesTo.get(2), verticesTo
				.get(1), verticesFrom.get(1))); // left
		faces.addPart(new SimpleFace3D(Color.BLUE, shadingModel, verticesFrom.get(3), verticesTo.get(3), verticesTo
				.get(2), verticesFrom.get(2))); // bottom
		return faces;
	}

	private List<Point3D> buildPathIntersectionVertices(Location location, double thickness) {
		List<Point3D> vertices = new Vector<Point3D>(4);
		Point3D c = location.getPosition();
		Vector3D u = location.getOrientation().getUnitU().clone();
		Vector3D v = location.getOrientation().getUnitV().clone();
		u.scale(thickness / 2);
		v.scale(thickness / 2);
		vertices.add(c.plus(u).plus(v)); // top-right
		vertices.add(c.minus(u).plus(v)); // top-left
		vertices.add(c.minus(u).minus(v)); // bottom-left
		vertices.add(c.plus(u).minus(v)); // bottom-right
		return vertices;
	}

	private Trajectory3D createTrajectory() {
		return new Trajectory3D(createCurve());
	}

	private Curve3D createCurve() {
		return ApproximatingCurve3D.createStandardCurve(createCurveControlPoints());
	}

	private List<Point3D> createCurveControlPoints() {
		int n = 50;
		List<Point3D> controlPoints = new Vector<Point3D>(n);
		for (int i = 0; i < n; i++) {
			double t = i * 4.0 / n;
			double r = t - Math.floor(t);
			double x = 0, y = 0;
			double z = 0.2 - 0.4 * Math.random();
			if (t < 1.0) {
				x = -1.0 + r;
				y = -1.0;
			} else if (t < 2.0) {
				double angle = (r - 0.5) * Math.PI;
				double radius = 0.45 + 1.0 * Math.random();
				x = radius * Math.cos(angle);
				y = radius * Math.sin(angle) - 0.5;
			} else if (t < 3.0) {
				double angle = (1.5 - r) * Math.PI;
				double radius = 0.45 + 1.0 * Math.random();
				x = radius * Math.cos(angle);
				y = radius * Math.sin(angle) + 0.5;
			} else {
				x = r;
				y = 1.0;
			}
			controlPoints.add(new Point3D(x, y, z));
		}
		return controlPoints;
	}

}