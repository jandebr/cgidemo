package org.maia.cgi.demo.d3.polygon;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.ConvexPolygonalObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.object.SimpleFace3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.render.d3.shading.FlatShadingModel;
import org.maia.cgi.render.d3.shading.FlatShadingModelImpl;

public class PolygonSceneBuilder extends SceneBuilder {

	public PolygonSceneBuilder() {
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(900);
		options.setRenderHeight(600);
		options.setSceneBackgroundColor(Color.BLACK);
		return options;
	}

	@Override
	protected String getSceneName() {
		return "Polygon";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, -4.0), Point3D.origin(), 60.0,
				options.getAspectRatio(), 0.5, 10.0);
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		// Yaw
		Color baseColor = new Color(61, 169, 252);
		for (int i = 0; i <= 4; i++) {
			objects.add(buildPolygon(baseColor).rotateY(Geometry.degreesToRadians(i * 16.0)).translate((i - 2), 1.0,
					-4.0));
		}
		// Pitch
		baseColor = new Color(196, 79, 169);
		for (int i = 0; i <= 4; i++) {
			objects.add(buildPolygon(baseColor).rotateX(Geometry.degreesToRadians(i * 19.0)).translate((i - 2), -1.0,
					-4.0));
		}
		return objects;
	}

	private BaseObject3D buildPolygon(Color color) {
		FlatShadingModel shadingModel = new FlatShadingModelImpl();
		List<Point3D> vertices = new Vector<Point3D>();
		vertices.add(new Point3D(2, 8, 0));
		vertices.add(new Point3D(8, 10, 0));
		vertices.add(new Point3D(9, 7, 0));
		vertices.add(new Point3D(8, 2, 0));
		vertices.add(new Point3D(6, 0, 0));
		vertices.add(new Point3D(1, 3, 0));
		vertices.add(new Point3D(1, 5, 0));
		MultipartObject3D<SimpleFace3D> faces = new MultipartObject3D<SimpleFace3D>();
		faces.addParts(ModelBuilderUtils.convertToFaces(new ConvexPolygonalObject3D(vertices), color, shadingModel));
		ModelBuilderUtils.centerAtOrigin(faces);
		return (BaseObject3D) faces.scale(0.1);
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.6));
		lights.add(new SpotLight(Point3D.origin()));
		return lights;
	}

}