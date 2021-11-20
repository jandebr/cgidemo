package org.maia.cgi.demo.d3.transit;

import java.awt.Color;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.compose.d3.DepthFunction;
import org.maia.cgi.compose.d3.LinearScalingDepthFunction;
import org.maia.cgi.compose.d3.SigmoidDepthFunction;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Box3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.model.d3.CoordinateFrame;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.object.ObjectSurfacePoint3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D.PictureRegion;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.render.d3.view.ColorDepthBuffer;
import org.maia.cgi.shading.d2.ImageMaskFileHandle;
import org.maia.cgi.shading.d2.ImageTextureMapFileHandle;
import org.maia.cgi.shading.d2.Mask;
import org.maia.cgi.shading.d2.TextureMap;
import org.maia.cgi.shading.d2.TextureMapHandle;
import org.maia.cgi.shading.d2.TextureMapRegistry;
import org.maia.cgi.shading.d3.FlatShadingModel;
import org.maia.cgi.shading.d3.FlatShadingModelImpl;
import org.maia.cgi.transform.d3.TransformMatrix;
import org.maia.cgi.transform.d3.Transformation;

public class TransitSceneBuilder extends SceneBuilder {

	private String wordAlfa = "alfa";

	private String wordBeta = "beta";

	private double zAlfa = -28.0;

	private double zBeta = -15.0;

	private double wordWidth = 19.2;

	private double wordHeight = 7.29;

	private int tilesX = 40;

	private int tilesY = 27;

	private double tileWidth = wordWidth / tilesX;

	private double tileHeight = wordHeight / tilesY;

	private int tileImageWidth = 48;

	private int tileImageHeight = 27;

	private int frameImageWidth = 160;

	private int frameImageHeight = 90;

	public TransitSceneBuilder() {
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(960);
		options.setRenderHeight(540);
		options.setSceneBackgroundColor(Color.BLACK);
		return options;
	}

	@Override
	protected String getSceneName() {
		return "Transit";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, (zAlfa + zBeta) / 2), new Point3D(0, 0, 0),
				60.0, options.getAspectRatio(), 0.5, -zAlfa + 5.0);
		camera.revolveLongitudinal(Geometry.degreesToRadians(50.0));
		camera.revolveLatitudinal(Geometry.degreesToRadians(5.0));
		camera.slide(-2.5, 0, -4.0);
		camera.revolveLongitudinal(Geometry.degreesToRadians(1.0));
		camera.slide(0, -0.8, 1.0);
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		String[][] grid = getFrameImagePathsGrid();
		TextureMap gravityMap = getGravityMap();
		for (int yi = 0; yi < tilesY; yi++) {
			double yc = (wordHeight - tileHeight) / 2 - yi * tileHeight;
			for (int xi = 0; xi < tilesX; xi++) {
				double xc = -(wordWidth - tileWidth) / 2 + xi * tileWidth;
				double r = 1.0 - gravityMap.sampleDouble(xi, yi);
				double t = 1.0 - Math.abs(2 * r - 1);
				String framePath = grid[yi][xi];
				String tilePath = "tile-y" + yi + "-x" + xi + ".png";
				// Frame
				double delta = 0.2 * (Math.random() * 2.0 - 1.0) * (0.02 + 0.98 * t);
				double zr = Math.max(Math.min(r + delta, 1.0), 0);
				double zc = (1.0 - zr) * zAlfa + zr * zBeta;
				String word = r <= 0.5 ? wordAlfa : wordBeta;
				String transparencyMapPath = getResourcePath("tiles/" + word + "/" + tilePath);
				Tile tile = buildTile(framePath, frameImageWidth, frameImageHeight, tileWidth, tileHeight, 0.1,
						transparencyMapPath);
				tile.rotateY(Geometry.degreesToRadians(30.0 * (Math.random() * 2.0 - 1.0) * t));
				tile.rotateX(Geometry.degreesToRadians(20.0 * (Math.random() * 2.0 - 1.0) * t));
				tile.rotateZ(Geometry.degreesToRadians(10.0 * (Math.random() * 2.0 - 1.0) * t));
				tile.translate(xc, yc, zc);
				objects.add(tile);
				// Mirror at alfa
				if (r >= 0.1) {
					double baseTransparency = 0.7 + 0.27 * ((r - 0.1) / 0.9);
					transparencyMapPath = getResourcePath("tiles/" + wordAlfa + "/" + tilePath);
					objects.add(buildTile(framePath, frameImageWidth, frameImageHeight, tileWidth, tileHeight,
							baseTransparency, transparencyMapPath).translate(xc, yc, zAlfa));
				}
				// Mirror at beta
				if (r <= 0.9) {
					double baseTransparency = 0.7 + 0.27 * ((0.9 - r) / 0.9);
					transparencyMapPath = getResourcePath("tiles/" + wordBeta + "/" + tilePath);
					objects.add(buildTile(framePath, frameImageWidth, frameImageHeight, tileWidth, tileHeight,
							baseTransparency, transparencyMapPath).translate(xc, yc, zBeta));
				}
			}
		}
		return objects;
	}

	private Tile buildTile(String imagePath, int imageWidth, int imageHeight, double tileWidth, double tileHeight,
			double baseTransparency, String transparencyMapPath) {
		double frameToTileRatio = frameImageWidth / (double) tileImageWidth;
		FlatShadingModel shadingModel = new FlatShadingModelImpl(1.0, 3.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(imagePath);
		TextureMapHandle transparencyMapHandle = new ImageTextureMapFileHandle(transparencyMapPath, frameToTileRatio);
		Tile tile = new Tile(shadingModel, pictureMapHandle, new PictureRegion(imageWidth, imageHeight),
				baseTransparency, transparencyMapHandle, getTileMask());
		tile.scaleX(tileWidth / 2).scaleZ(tileHeight / 2);
		tile.rotateX(Geometry.degreesToRadians(90));
		return tile;
	}

	private Mask getTileMask() {
		ImageMaskFileHandle handle = new ImageMaskFileHandle(getResourcePath("borders/border-mask.png"), Color.WHITE);
		return (Mask) TextureMapRegistry.getInstance().getTextureMap(handle);
	}

	private TextureMap getGravityMap() {
		TextureMapHandle handle = new ImageTextureMapFileHandle(getResourcePath("gravity/gravity4-thumb.png"));
		return TextureMapRegistry.getInstance().getTextureMap(handle);
	}

	private String[][] getFrameImagePathsGrid() {
		List<String> paths = getFrameImagePaths();
		Collections.shuffle(paths);
		int pi = 0;
		String[][] grid = new String[tilesY][tilesX];
		for (int yi = 0; yi < tilesY; yi++) {
			for (int xi = 0; xi < tilesX; xi++) {
				grid[yi][xi] = paths.get(pi % paths.size());
				pi++;
			}
		}
		// Specials
		grid[24][3] = getResourcePath("frames/x.png");
		return grid;
	}

	private List<String> getFrameImagePaths() {
		List<String> paths = new Vector<String>(1500);
		File folder = new File(getResourcePath("frames/prototype"));
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".png")) {
				paths.add(file.getPath());
			}
		}
		return paths;
	}

	@Override
	protected DepthFunction createDarknessDepthFunction(Scene scene, RenderOptions options) {
		Box3D bbox = scene.getBoundingBox(CoordinateFrame.CAMERA);
		double nearDepth = -bbox.getZ2();
		double farDepth = -bbox.getZ1();
		double midDepth = 0.4 * nearDepth + 0.6 * farDepth;
		return new LinearScalingDepthFunction(SigmoidDepthFunction.createFilter(midDepth, farDepth, 0.5, 0.6), 0.92);
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		Box3D bbox = scene.getBoundingBox(CoordinateFrame.WORLD);
		Point3D center = bbox.getCenter();
		double radius = Math.max(Math.max(bbox.getWidth(), bbox.getHeight()), bbox.getDepth());
		for (int i = 0; i < 8; i++) {
			TransformMatrix T = Transformation.getRotationXrollMatrix(Geometry.degreesToRadians(360.0 * Math.random()))
					.preMultiply(
							Transformation.getRotationYrollMatrix(Geometry.degreesToRadians(360.0 * Math.random())));
			Vector3D v = T.transform(new Point3D(radius, 0, 0)).minus(Point3D.origin());
			Point3D positionInWorld = center.plus(v);
			lights.add(new SpotLight(positionInWorld, 0.8));
		}
		lights.add(new SpotLight(center, 0.8));
		// lights.add(new AmbientLight(0.2));
		return lights;
	}

	@Override
	protected ColorDepthBuffer createBackdrop(Scene scene, RenderOptions options) {
		Box3D bbox = scene.getBoundingBox(CoordinateFrame.CAMERA);
		double farDepth = -bbox.getZ1();
		return new ColorDepthBuffer(Compositing.readImageFromFile(getResourcePath("backdrops/stars4-1920x1080.png")),
				farDepth + 1.0);
	}

	private String getResourcePath(String relativePath) {
		return new File("resources/transit", relativePath).getPath();
	}

	private static class Tile extends SimpleTexturedFace3D {

		private double baseTransparency;

		public Tile(FlatShadingModel shadingModel, TextureMapHandle pictureMapHandle, PictureRegion pictureRegion,
				double baseTransparency, TextureMapHandle transparencyMapHandle, Mask pictureMask) {
			super(shadingModel, pictureMapHandle, pictureRegion, null, transparencyMapHandle, pictureMask);
			this.baseTransparency = baseTransparency;
		}

		@Override
		protected double sampleTransparency(ObjectSurfacePoint3D surfacePoint, Scene scene) {
			double transparency = super.sampleTransparency(surfacePoint, scene);
			if (Double.isNaN(transparency)) {
				return getBaseTransparency();
			} else {
				return 1.0 - (1.0 - transparency) * (1.0 - getBaseTransparency());
			}
		}

		public double getBaseTransparency() {
			return baseTransparency;
		}

	}

}