package org.maia.cgi.demo.d3.axis;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.compose.d3.DepthFunction;
import org.maia.cgi.compose.d3.LinearScalingDepthFunction;
import org.maia.cgi.compose.d3.SigmoidDepthFunction;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.gui.d3.renderer.RenderOptions;
import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.shading.d3.FlatShadingModel;
import org.maia.cgi.shading.d3.FlatShadingModelImpl;

public class AxisSceneBuilder extends SceneBuilder {

	public AxisSceneBuilder() {
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
		return "Axis";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, -4.0), Point3D.origin(), 60.0,
				options.getAspectRatio(), 0.5, 10.0);
		// camera.revolveLongitudinal(Geometry.degreesToRadians(30.0)).revolveLatitudinal(Geometry.degreesToRadians(20.0));
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		// objects.add(buildRing().scale(0.5).translateZ(-4.0));
		objects.add(buildAxes().translateZ(-4.0));
		// objects.add(buildRing().scale(1.0).translateZ(-4.0));
		return objects;
	}

	private BaseObject3D buildRing() {
		FlatShadingModel shadingModel = new FlatShadingModelImpl(1.0, 1.0);
		double depth = 1.0;
		return ModelBuilderUtils.buildRing(0.7, 1.0, depth, 50, Color.GREEN, shadingModel).translateZ(-depth / 2);
	}

	private BaseObject3D buildAxes() {
		MultipartObject3D<BaseObject3D> axes = new MultipartObject3D<BaseObject3D>();
		axes.addPart(buildXaxis());
		axes.addPart(buildXaxis().rotateZ(Geometry.degreesToRadians(90.0)));
		axes.addPart(buildXaxis().rotateY(Geometry.degreesToRadians(-90.0)));
		return axes;
	}

	private BaseObject3D buildXaxis() {
		MultipartObject3D<BaseObject3D> axis = new MultipartObject3D<BaseObject3D>();
		FlatShadingModel shadingModel = new FlatShadingModelImpl(1.0, 1.0);
		axis.addPart(ModelBuilderUtils.buildCylinder(0.02, 2.0, 20, Color.BLUE, shadingModel).translateZ(-1.0)
				.rotateY(Geometry.degreesToRadians(90.0)));
		axis.addPart(ModelBuilderUtils.buildPyramid(0.05, 0.1, 20, Color.RED, shadingModel, true)
				.rotateY(Geometry.degreesToRadians(90.0)).translateX(1.0));
		return axis;
	}

	@Override
	protected DepthFunction createDarknessDepthFunction(Scene scene, RenderOptions options) {
		return new LinearScalingDepthFunction(SigmoidDepthFunction.createFilter(3.0, 5.0, 0.7, 0.4), 0.9);
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.4));
		lights.add(new SpotLight(new Point3D(3.0, 1.0, 1.0), 0.6));
		lights.add(new SpotLight(new Point3D(0.0, 3.0, -2.0), 0.6));
		return lights;
	}

}