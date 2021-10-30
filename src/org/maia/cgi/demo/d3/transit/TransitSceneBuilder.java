package org.maia.cgi.demo.d3.transit;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.compose.d3.DepthFunction;
import org.maia.cgi.compose.d3.SigmoidDepthFunction;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Box3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.gui.d3.renderer.RenderOptions;
import org.maia.cgi.model.d3.CoordinateFrame;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D.PictureRegion;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.shading.d2.ImageTextureMapFileHandle;
import org.maia.cgi.shading.d2.TextureMapHandle;
import org.maia.cgi.shading.d3.FlatShadingModel;
import org.maia.cgi.shading.d3.FlatShadingModelImpl;

public class TransitSceneBuilder extends SceneBuilder {

	public TransitSceneBuilder() {
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(1200);
		options.setRenderHeight(600);
		options.setSceneBackgroundColor(Color.BLACK);
		return options;
	}

	@Override
	protected String getSceneName() {
		return "Transit";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, -4.0), new Point3D(0, 2.0, 0), 60.0,
				options.getAspectRatio(), 0.5, 10.0);
		// camera.revolveLongitudinal(Geometry.degreesToRadians(30.0)).revolveLatitudinal(Geometry.degreesToRadians(20.0));
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		objects.add(buildTile("resources/transit/Thunderbird2.jpg", 1024, 768).translateZ(-4.0));
		return objects;
	}

	private BaseObject3D buildTile(String imagePath, int imageWidth, int imageHeight) {
		FlatShadingModel shadingModel = new FlatShadingModelImpl(1.0, 1.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(imagePath);
		BaseObject3D layer = new SimpleTexturedFace3D(shadingModel, pictureMapHandle,
				new PictureRegion(imageWidth, imageHeight));
		double imageRatio = imageWidth * 1.0 / imageHeight;
		layer.scale(imageRatio, 1.0, 1.0).rotateX(Geometry.degreesToRadians(90));
		return layer;
	}

	@Override
	protected DepthFunction createDarknessDepthFunction(Scene scene, RenderOptions options) {
		Box3D bbox = scene.getBoundingBox(CoordinateFrame.CAMERA);
		double nearDepth = -bbox.getZ2();
		double farDepth = -bbox.getZ1();
		return SigmoidDepthFunction.createFilter(nearDepth, farDepth, 0.6, 0.4);
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