package org.maia.cgi.demo.d3.layers;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.compose.d3.DepthFunction;
import org.maia.cgi.compose.d3.SigmoidDepthFunction;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Box3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.CoordinateFrame;
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
import org.maia.cgi.render.d2.ImageTextureMapFileHandle;
import org.maia.cgi.render.d2.TextureMapHandle;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.render.d3.shading.FlatShadingModel;
import org.maia.cgi.render.d3.shading.FlatShadingModelImpl;

public class LayersSceneBuilder extends SceneBuilder {

	public LayersSceneBuilder() {
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
		return "Layers";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, -3.5), new Point3D(0, 2.0, 0), 60.0,
				options.getAspectRatio(), 0.5, 10.0);
		// camera.revolveLongitudinal(Geometry.degreesToRadians(30.0)).revolveLatitudinal(Geometry.degreesToRadians(20.0));
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		// objects.addAll(createSpriteLayers(options)); // Demo 44
		objects.addAll(createStreamingLayers(options)); // Demo 46
		objects.add(buildFloor());
		return objects;
	}

	private Collection<Object3D> createSpriteLayers(RenderOptions options) {
		Collection<Object3D> layers = new Vector<Object3D>();
		layers.add(createSpriteLayer("Sprite1-5x5.jpg", 640, 360).translateZ(-3.0));
		layers.add(createSpriteLayer("Sprite2-5x5.jpg", 640, 360).translateZ(-4.0));
		layers.add(createSpriteLayer("Sprite4-5x5.jpg", 640, 360).translateZ(-5.0));
		layers.add(createSpriteLayer("Sprite3-5x5.jpg", 640, 360).translateZ(-6.0));
		return layers;
	}

	private BaseObject3D createSpriteLayer(String imageFilename, int imageWidth, int imageHeight) {
		String imagePath = "resources/layers/thumbnails/" + imageFilename;
		FlatShadingModel shadingModel = new FlatShadingModelImpl(1.0, 1.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(imagePath);
		BaseObject3D layer = new SimpleTexturedFace3D(shadingModel, pictureMapHandle,
				new PictureRegion(imageWidth, imageHeight));
		double imageRatio = imageWidth * 1.0 / imageHeight;
		layer.scale(imageRatio, 1.0, 1.0).rotateX(Geometry.degreesToRadians(90));
		return layer;
	}

	private Collection<Object3D> createStreamingLayers(RenderOptions options) {
		Collection<Object3D> layers = new Vector<Object3D>();
		layers.add(createStreamingLayer("01_player.png", "transparency.png", 1800, 900).translateZ(-3.0));
		layers.add(createStreamingLayer("02_akamai.png", "02_akamai-transparency.png", 1800, 900).translateZ(-4.0));
		layers.add(createStreamingLayer("03_gcdn.png", "03_gcdn-transparency.png", 1800, 900).translateZ(-5.5));
		layers.add(createStreamingLayer("04_gcs.png", "04_gcs-transparency.png", 1800, 900).translateZ(-8.0));
		return layers;
	}

	private BaseObject3D createStreamingLayer(String imageFilename, String transparencyFilename, int imageWidth,
			int imageHeight) {
		String imagePath = "resources/layers/gCDN/" + imageFilename;
		String transparencyPath = "resources/layers/gCDN/" + transparencyFilename;
		FlatShadingModel shadingModel = new FlatShadingModelImpl(1.0, 1.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(imagePath);
		TextureMapHandle transparencyMapHandle = new ImageTextureMapFileHandle(transparencyPath);
		BaseObject3D layer = new SimpleTexturedFace3D(shadingModel, pictureMapHandle,
				new PictureRegion(imageWidth, imageHeight), null, transparencyMapHandle, null);
		double imageRatio = imageWidth * 1.0 / imageHeight;
		layer.scale(imageRatio, 1.0, 1.0).rotateX(Geometry.degreesToRadians(90));
		return layer;
	}

	private BaseObject3D buildFloor() {
		String imagePath = "resources/layers/thumbnails/wood3.jpg";
		MultipartObject3D<BaseObject3D> floor = new MultipartObject3D<BaseObject3D>();
		double y = -1.0;
		double x1 = -8.0;
		double x2 = 8.0;
		double z1 = -13.0;
		double z2 = -1.0;
		double tileSize = 1.0;
		FlatShadingModel shadingModel = new FlatShadingModelImpl(0.6, 1.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(imagePath);
		for (double x = x1; x <= x2 - tileSize; x += tileSize) {
			for (double z = z1; z <= z2 - tileSize; z += tileSize) {
				BaseObject3D tile = new SimpleTexturedFace3D(shadingModel, pictureMapHandle,
						new PictureRegion(544, 317));
				tile.scaleX(tileSize / 2).scaleZ(tileSize / 2);
				tile.translate(x + tileSize / 2, y, z + tileSize / 2);
				floor.addPart(tile);
			}
		}
		return floor;
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