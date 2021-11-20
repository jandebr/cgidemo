package org.maia.cgi.demo.d3.axis;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D.PictureRegion;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.shading.d2.ImageTextureMapFileHandle;
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
		options.setSceneBackgroundColor(Compositing.setTransparency(Color.BLACK, 1.0));
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
		camera.revolveLongitudinal(Geometry.degreesToRadians(-30.0))
				.revolveLatitudinal(Geometry.degreesToRadians(20.0));
		camera.slide(0, 0.2, -2.0);
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		Color gray = Color.GRAY;
		Color black = Color.BLACK;
		objects.add(buildAxes(black, black, 0.2).scale(0.7, 0.7, 0.7).translateZ(-4.0));
		objects.add(buildCamera(gray, gray, gray, 0.8, false).rotateY(Geometry.degreesToRadians(180.0))
				.translateZ(-4.0));
		objects.add(buildCamera(new Color(95, 179, 252), new Color(244, 49, 65), new Color(252, 183, 89), 0, false)
				.rotateY(Geometry.degreesToRadians(135.0)).translateZ(-4.0));
		return objects;
	}

	private BaseObject3D buildCamera(Color baseColor, Color lensColor, Color buttonColor, double transparency,
			boolean showRotation) {
		MultipartObject3D<BaseObject3D> camera = new MultipartObject3D<BaseObject3D>();
		FlatShadingModel shadingModel = new FlatShadingModelImpl();
		double d = 0.25;
		camera.addPart(ModelBuilderUtils.buildBox(1.0, 1.0, d, Compositing.setTransparency(baseColor, transparency),
				shadingModel));
		camera.addPart(ModelBuilderUtils
				.buildBox(0.14, 0.04, 0.1, Compositing.setTransparency(buttonColor, transparency), shadingModel)
				.translateX(-0.38).translateY(0.5 + 0.04 / 2));
		camera.addPart(ModelBuilderUtils.buildRing(0.4, 0.45, d * 0.8, 500,
				Compositing.setTransparency(lensColor, transparency), shadingModel).translateZ(d / 2));
		if (showRotation) {
			camera.addPart((BaseObject3D) new SimpleTexturedFace3D(shadingModel, new ImageTextureMapFileHandle(
					"resources/axis/arrow-ccw.png"), new PictureRegion(512, 512)).scale(0.4)
					.rotateX(Geometry.degreesToRadians(90.0)).translateZ(-d / 2 - 0.0001));
		}
		return camera;
	}

	private BaseObject3D buildAxes(Color axisColor, Color arrowColor, double transparency) {
		MultipartObject3D<BaseObject3D> axes = new MultipartObject3D<BaseObject3D>();
		axes.addPart(buildXaxis(axisColor, arrowColor, transparency));
		axes.addPart(buildXaxis(axisColor, arrowColor, transparency).rotateZ(Geometry.degreesToRadians(90.0)));
		axes.addPart(buildXaxis(axisColor, arrowColor, transparency).rotateY(Geometry.degreesToRadians(-90.0)));
		return axes;
	}

	private BaseObject3D buildXaxis(Color axisColor, Color arrowColor, double transparency) {
		MultipartObject3D<BaseObject3D> axis = new MultipartObject3D<BaseObject3D>();
		FlatShadingModel shadingModel = new FlatShadingModelImpl(0.5, 3.0);
		axis.addPart(ModelBuilderUtils
				.buildCylinder(0.04, 2.0, 20, Compositing.setTransparency(axisColor, transparency), shadingModel)
				.translateZ(-1.0).rotateY(Geometry.degreesToRadians(90.0)));
		axis.addPart(ModelBuilderUtils
				.buildPyramid(0.08, 0.12, 50, Compositing.setTransparency(arrowColor, transparency), shadingModel, true)
				.rotateY(Geometry.degreesToRadians(90.0)).translateX(1.0));
		return axis;
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.6));
		lights.add(new SpotLight(new Point3D(-2.0, 1.0, -2.0)));
		lights.add(new SpotLight(new Point3D(+4.0, 1.0, -4.0)));
		return lights;
	}

}