package org.maia.cgi.demo.d3.elevation;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Box3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.InboundLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.object.ObjectSurfacePoint3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D.PictureRegion;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.render.d2.ImageTextureMapFileHandle;
import org.maia.cgi.render.d2.Mask;
import org.maia.cgi.render.d2.TextureMapHandle;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.render.d3.shading.FlatShadingModel;
import org.maia.cgi.render.d3.shading.FlatShadingModelImpl;
import org.maia.cgi.render.d3.view.ColorDepthBuffer;

public class ElevationSceneBuilder extends SceneBuilder {

	private static final double Z_OFFSET = -10.0;

	private static final double BIG_FRAME_TRANSPARENCY = 0.1;

	private static final double PILLAR_TRANSPARENCY = 0.4;

	private static final double MOSAIC_TRANSPARENCY = 0;

	private static final int FRAME_IMAGE_WIDTH = 160;

	private static final int FRAME_IMAGE_HEIGHT = 90;

	private static final int FRAME_COUNT = 300;

	private File rootFolder;

	private File framesDataFile;

	private File frameImagesFolder;

	public ElevationSceneBuilder() {
		this.rootFolder = new File("resources/elevation");
		this.framesDataFile = new File(rootFolder, "frames/frames-data.csv");
		this.frameImagesFolder = new File("resources/transit/frames/vod-scaled");
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(1920);
		options.setRenderHeight(1080);
		options.setSceneBackgroundColor(Color.BLACK);
		return options;
	}

	@Override
	protected String getSceneName() {
		return "Elevation";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, Z_OFFSET), Point3D.origin(), 60.0,
				options.getAspectRatio(), 0.5, 20.0);
		camera.revolveLongitudinal(Geometry.degreesToRadians(-20.0)).revolveLatitudinal(Geometry.degreesToRadians(30.0))
				.revolveLongitudinal(Geometry.degreesToRadians(-40.0));
		camera.slide(+1.0, +1.2, -2.0);
		return camera;
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.1));
		lights.add(new InboundLight(new Vector3D(0, 1.0, 0), 0.1));
		for (int i = 1; i < 5; i++) {
			double y = 2.0 * i;
			lights.add(new SpotLight(new Point3D(2.2, y, Z_OFFSET), 0.4));
			lights.add(new SpotLight(new Point3D(0, y, Z_OFFSET + 2.2), 0.4));
		}
		return lights;
	}

	@Override
	protected ColorDepthBuffer createBackdrop(Scene scene, RenderOptions options) {
		Box3D bbox = scene.getBoundingBoxInCameraCoordinates();
		double farDepth = -bbox.getZ1();
		return new ColorDepthBuffer(Compositing.readImageFromFile(getResourcePath("backdrops/galaxy-1920x1080.png")),
				farDepth + 1.0);
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		try {
			objects.add(createBigFrames().translate(0, 0, Z_OFFSET));
			objects.add(createSmallFrames(false).translate(0, 0, Z_OFFSET));
			objects.add(createPillar().translate(0, 0, Z_OFFSET));
			objects.add(createTitles().translate(0, 0, Z_OFFSET));
			// objects.add(createWordMosaic().translate(0, 0, Z_OFFSET));
		} catch (IOException e) {
			System.out.println(e);
		}
		return objects;
	}

	private BaseObject3D createBigFrames() {
		MultipartObject3D<Frame> frames = new MultipartObject3D<Frame>();
		frames.addPart((Frame) createBigFrame("frames/big/vtmgo-near-square.png", 1903, 2322)
				.rotateY(Geometry.degreesToRadians(90.0)).translate(-1.0, +0.5, +1.0));
		frames.addPart(
				(Frame) createBigFrame("frames/big/streamz-near-square.png", 1901, 2321).translate(+1.0, -0.5, -2.0));
		return frames;
	}

	private Frame createBigFrame(String imagePath, int imageWidth, int imageHeight) {
		FlatShadingModel shader = new FlatShadingModelImpl(1.0, 3.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(getResourcePath(imagePath));
		PictureRegion pictureRegion = new PictureRegion(imageWidth, imageHeight);
		Frame frame = new Frame(shader, pictureMapHandle, pictureRegion, BIG_FRAME_TRANSPARENCY);
		frame.scale(imageWidth / 475.0, 1.0, imageHeight / 475.0);
		return frame;
	}

	private BaseObject3D createSmallFrames(boolean useKeyFrames) throws IOException {
		MultipartObject3D<Frame> frames = new MultipartObject3D<Frame>();
		List<FrameData> framesData = getFramesData();
		for (int i = 0; i < framesData.size(); i++) {
			FrameData data = framesData.get(i);
			if (useKeyFrames) {
				data.setImagePath(getResourcePath("frames/keys/keyframe-" + i + ".png"));
			}
			overrideForHandpickedFrames(data, i);
			frames.addPart(createSmallFrame(data));
		}
		return frames;
	}

	private void overrideForHandpickedFrames(FrameData data, int frameIndex) {
		String imagePath = null;
		if (frameIndex == 200) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame03.png";
		} else if (frameIndex == 145) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame22.png";
		} else if (frameIndex == 284) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame05.png";
		} else if (frameIndex == 212) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame01.png";
		} else if (frameIndex == 105) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame08.png";
		} else if (frameIndex == 255) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame12.png";
		} else if (frameIndex == 24) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame13.png";
		} else if (frameIndex == 241) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame02.png";
		} else if (frameIndex == 68) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame06.png";
		} else if (frameIndex == 146) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame07.png";
		} else if (frameIndex == 65) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame11.png";
		} else if (frameIndex == 12) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame21.png";
		} else if (frameIndex == 298) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame04.png";
		} else if (frameIndex == 246) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame20.png";
		} else if (frameIndex == 17) {
			imagePath = "frames/selected/vtmgo/vtmgo-frame23.png";
		} else if (frameIndex == 178) {
			imagePath = "frames/selected/streamz/streamz-frame02.png";
		} else if (frameIndex == 233) {
			imagePath = "frames/selected/streamz/streamz-frame01.png";
		} else if (frameIndex == 28) {
			imagePath = "frames/selected/streamz/streamz-frame05.png";
		} else if (frameIndex == 122) {
			imagePath = "frames/selected/streamz/streamz-frame03a.png";
		} else if (frameIndex == 281) {
			imagePath = "frames/selected/streamz/streamz-frame07.png";
		} else if (frameIndex == 25) {
			imagePath = "frames/selected/streamz/streamz-frame06a.png";
		} else if (frameIndex == 98) {
			imagePath = "frames/selected/streamz/streamz-frame08.png";
		} else if (frameIndex == 220) {
			imagePath = "frames/selected/streamz/streamz-frame03b.png";
		} else if (frameIndex == 97) {
			imagePath = "frames/selected/streamz/streamz-frame06b.png";
		} else if (frameIndex == 74) {
			imagePath = "frames/selected/streamz/streamz-frame20.png";
		} else if (frameIndex == 70) {
			imagePath = "frames/selected/streamz/streamz-frame21.png";
		} else if (frameIndex == 120) {
			imagePath = "frames/selected/streamz/streamz-frame04.png";
		}
		if (imagePath != null) {
			data.setImagePath(getResourcePath(imagePath));
			data.setImageWidth(500);
			data.setImageHeight(281);
			// data.setTransparency(1.0);
		}
	}

	private Frame createSmallFrame(FrameData data) {
		FlatShadingModel shader = new FlatShadingModelImpl(0.8, 2.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(data.getImagePath());
		PictureRegion pictureRegion = new PictureRegion(data.getImageWidth(), data.getImageHeight());
		TextureMapHandle luminanceMapHandle = new ImageTextureMapFileHandle(getResourcePath(
				"frames/luminance-border-" + data.getImageWidth() + "x" + data.getImageHeight() + ".png"));
		Frame frame = new Frame(shader, pictureMapHandle, pictureRegion, data.getTransparency(), data.getLuminance(),
				luminanceMapHandle);
		frame.scale(data.getScale(), 1.0, data.getScale() / data.getImageWidth() * data.getImageHeight());
		if (data.isFlipOrientation()) {
			frame.rotateY(Geometry.degreesToRadians(90.0));
		}
		frame.translate(data.getCenter().minus(Point3D.origin()));
		return frame;
	}

	private List<FrameData> getFramesData() throws IOException {
		if (getFramesDataFile().exists()) {
			return readFramesData(getFramesDataFile());
		} else {
			List<FrameData> framesData = produceFramesData();
			writeFramesData(getFramesDataFile(), framesData);
			return framesData;
		}
	}

	private List<FrameData> produceFramesData() {
		List<FrameData> framesData = new Vector<FrameData>(FRAME_COUNT);
		List<String> imagePaths = getFrameImagePaths();
		Collections.shuffle(imagePaths);
		for (int i = 0; i < FRAME_COUNT; i++) {
			// String imagePath = "frames/dummy/frame-" + FRAME_IMAGE_WIDTH + "x" + FRAME_IMAGE_HEIGHT + ".png";
			String imagePath = imagePaths.get(i % imagePaths.size());
			framesData.add(produceSingleFrameData(imagePath, FRAME_IMAGE_WIDTH, FRAME_IMAGE_HEIGHT));
		}
		return framesData;
	}

	private FrameData produceSingleFrameData(String imagePath, int imageWidth, int imageHeight) {
		Point3D center = generateRandomFrameCenter();
		double distanceFromYaxis = new Point3D(center.getX(), 0, center.getZ()).distanceTo(Point3D.origin());
		double scale = gaussianFunctionNormalized(distanceFromYaxis, 1.0, 1.8);
		double transparency = 1.0 - 1.0 * gaussianFunctionNormalized(distanceFromYaxis, 1.0, 4.0);
		double luminance = gaussianFunctionNormalized(distanceFromYaxis, 1.0, 2.0) - 0.6;
		boolean flipOrientation = Math.random() >= 0.5;
		return new FrameData(imagePath, imageWidth, imageHeight, center, scale, transparency, luminance,
				flipOrientation);
	}

	private Point3D generateRandomFrameCenter() {
		double radius = 2.0 + 6.8 * Math.random();
		double angle = Math.random() * 2.0 * Math.PI;
		double x = radius * Math.cos(angle);
		double z = radius * Math.sin(angle);
		double y = 0.6 + 3.2 * Math.random();
		return new Point3D(x, y, z);
	}

	private List<String> getFrameImagePaths() {
		List<String> paths = new Vector<String>(1000);
		for (File file : getFrameImagesFolder().listFiles()) {
			if (file.isFile()) {
				paths.add(file.getPath());
			}
		}
		return paths;
	}

	private List<FrameData> readFramesData(File inputFile) throws IOException {
		List<FrameData> framesData = new Vector<FrameData>(FRAME_COUNT);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String line = null;
		while ((line = reader.readLine()) != null) {
			framesData.add(FrameData.fromCsv(line));
		}
		reader.close();
		return framesData;
	}

	private void writeFramesData(File outputFile, List<FrameData> framesData) throws IOException {
		PrintWriter pw = new PrintWriter(outputFile);
		for (FrameData data : framesData) {
			pw.println(data.toCsv());
		}
		pw.close();
	}

	private BaseObject3D createPillar() {
		double width = 2.0;
		double height = 11.0;
		MultipartObject3D<BaseObject3D> pillar = new MultipartObject3D<BaseObject3D>();
		pillar.addPart(createPillarSide(width, height).translateZ(width / 2.0)); // front
		pillar.addPart(
				createPillarSide(width, height).rotateY(Geometry.degreesToRadians(180.0)).translateZ(-width / 2.0)); // back
		pillar.addPart(
				createPillarSide(width, height).rotateY(Geometry.degreesToRadians(-90.0)).translateX(-width / 2.0)); // left
		pillar.addPart(
				createPillarSide(width, height).rotateY(Geometry.degreesToRadians(+90.0)).translateX(+width / 2.0)); // right
		// Beam
		pillar.addPart(createPillarBeam(width, height).rotateY(Geometry.degreesToRadians(-90.0))
				.translateX(-width / 2.0 + 0.1)); // beam left
		pillar.addPart(createPillarBeam(width, height).rotateY(Geometry.degreesToRadians(+90.0))
				.translateX(+width / 2.0 - 0.1)); // beam right
		return pillar;
	}

	private BaseObject3D createPillarSide(double width, double height) {
		Color color = Compositing.setTransparency(new Color(120, 159, 237), PILLAR_TRANSPARENCY);
		FlatShadingModel shader = new FlatShadingModelImpl(1.0, 3.0);
		PictureRegion pictureRegion = new PictureRegion(246, 1303);
		TextureMapHandle luminanceMapHandle = new ImageTextureMapFileHandle(
				getResourcePath("pillar/bright-blue/luminance.png"));
		TextureMapHandle transparencyMapHandle = new ImageTextureMapFileHandle(
				getResourcePath("pillar/bright-blue/transparency-light.png"));
		BaseObject3D side = new SimpleTexturedFace3D(color, shader, pictureRegion, luminanceMapHandle,
				transparencyMapHandle, null);
		side.scale(width / 2, 1.0, height / 2);
		side.rotateX(Geometry.degreesToRadians(90.0));
		return side;
	}

	private BaseObject3D createPillarBeam(double width, double height) {
		Color color = Compositing.setTransparency(new Color(120, 159, 237), PILLAR_TRANSPARENCY);
		FlatShadingModel shader = new FlatShadingModelImpl(1.0, 3.0);
		PictureRegion pictureRegion = new PictureRegion(246, 635);
		TextureMapHandle luminanceMapHandle = new ImageTextureMapFileHandle(
				getResourcePath("pillar/beam/beam-luminance4.png"));
		TextureMapHandle transparencyMapHandle = new ImageTextureMapFileHandle(
				getResourcePath("pillar/beam/beam-transparency4.png"));
		BaseObject3D side = new SimpleTexturedFace3D(color, shader, pictureRegion, luminanceMapHandle,
				transparencyMapHandle, null);
		side.scale(width / 2, 1.0, height / 2);
		side.rotateX(Geometry.degreesToRadians(90.0));
		return side;
	}

	private BaseObject3D createWordMosaic() {
		double yBaseline = -0.5;
		File tileMaskFolder = new File("resources/transit/tiles/aws");
		File tilePictureFolder = new File("resources/transit/frames/vod-scaled");
		File tilePictureMask = new File("resources/transit/borders/border-mask.png");
		TileMosaic mosaic = new TileMosaic(tileMaskFolder, tilePictureFolder, tilePictureMask, 40, 27, 48, 27,
				FRAME_IMAGE_WIDTH, FRAME_IMAGE_HEIGHT);
		BaseObject3D object = mosaic.build(6.0, MOSAIC_TRANSPARENCY, 0.5);
		double objectHeight = object.getBoundingBoxInWorldCoordinates().getHeight();
		object.translate(2.0, yBaseline + objectHeight / 2.0, -6.0);
		return object;
	}

	private BaseObject3D createTitles() {
		MultipartObject3D<BaseObject3D> titles = new MultipartObject3D<BaseObject3D>();
		titles.addPart(createTitle("titles/vod.png", null, "titles/vod-transparency.png", 1128, 342, 1.0, 0.6)
				.translate(2.4, 3.3, -5.6));
		titles.addPart(createTitle("titles/move.png", null, "titles/move-transparency.png", 1397, 342, 1.4, 0.6)
				.translate(2.4, 2.4, -5.8));
		titles.addPart(createTitle("titles/to-solid.png", "titles/to-solid-luminance.png",
				"titles/to-solid-transparency.png", 520, 429, 0.2, 0.1).translate(1.5, 1.65, -6.0));
		titles.addPart(createTitle("titles/aws-solid.png", "titles/aws-solid-luminance.png",
				"titles/aws-solid-transparency.png", 1128, 342, 2.6, 0.6).translate(1.4, 1.0, -6.0));
		return titles;
	}

	private BaseObject3D createTitle(String imagePath, String luminancePath, String transparencyPath, int imageWidth,
			int imageHeight, double scale, double lightReflectionFactor) {
		FlatShadingModel shader = new FlatShadingModelImpl(lightReflectionFactor, 3.0);
		PictureRegion pictureRegion = new PictureRegion(imageWidth, imageHeight);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(getResourcePath(imagePath));
		TextureMapHandle luminanceMapHandle = luminancePath != null
				? new ImageTextureMapFileHandle(getResourcePath(luminancePath))
				: null;
		TextureMapHandle transparencyMapHandle = new ImageTextureMapFileHandle(getResourcePath(transparencyPath));
		BaseObject3D title = new SimpleTexturedFace3D(shader, pictureMapHandle, pictureRegion, luminanceMapHandle,
				transparencyMapHandle, null);
		title.scale(scale, 1.0, scale * imageHeight / imageWidth);
		title.rotateX(Geometry.degreesToRadians(110.0));
		return title;
	}

	private double gaussianFunctionNormalized(double x, double mean, double stdDev) {
		return (stdDev * Math.sqrt(2.0 * Math.PI)) * gaussianFunction(x, mean, stdDev);
	}

	private double gaussianFunction(double x, double mean, double stdDev) {
		return Math.exp(-0.5 * Math.pow((x - mean) / stdDev, 2.0)) / (stdDev * Math.sqrt(2.0 * Math.PI));
	}

	private String getResourcePath(String relativePath) {
		return new File(getRootFolder(), relativePath).getPath();
	}

	private File getRootFolder() {
		return rootFolder;
	}

	private File getFramesDataFile() {
		return framesDataFile;
	}

	private File getFrameImagesFolder() {
		return frameImagesFolder;
	}

	private static class Frame extends SimpleTexturedFace3D {

		private PictureRegion pictureRegion;

		private Mask pictureMask;

		private double baseTransparency;

		private double baseLuminance;

		public Frame(FlatShadingModel shadingModel, TextureMapHandle pictureMapHandle, PictureRegion pictureRegion,
				double baseTransparency) {
			this(shadingModel, pictureMapHandle, pictureRegion, baseTransparency, 0, null);
		}

		public Frame(FlatShadingModel shadingModel, TextureMapHandle pictureMapHandle, PictureRegion pictureRegion,
				double baseTransparency, double baseLuminance, TextureMapHandle luminanceMapHandle) {
			super(shadingModel, pictureMapHandle, pictureRegion, luminanceMapHandle);
			this.pictureRegion = pictureRegion;
			this.baseTransparency = baseTransparency;
			this.baseLuminance = baseLuminance;
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

		@Override
		protected double sampleLuminance(ObjectSurfacePoint3D surfacePoint, Scene scene) {
			double luminance = super.sampleLuminance(surfacePoint, scene);
			if (Double.isNaN(luminance)) {
				return getBaseLuminance();
			} else {
				return Math.min(Math.max(luminance + getBaseLuminance(), -1.0), 1.0);
			}
		}

		public Point3D fromPictureToWorldCoordinates(double pictureX, double pictureY) {
			Point3D pictureCoords = new Point3D(pictureX, 0, pictureY); // in XZ (iso XY)
			Point3D objectCoords = getPictureToObjectTransformMatrix().transform(pictureCoords);
			Point3D worldCoords = getSelfToRootCompositeTransform().forwardTransform(objectCoords);
			return worldCoords;
		}

		@Override
		protected Mask getPictureMask() {
			if (pictureMask == null) {
				pictureMask = new FramePillarMask(this);
			}
			return pictureMask;
		}

		public PictureRegion getPictureRegion() {
			return pictureRegion;
		}

		public double getBaseTransparency() {
			return baseTransparency;
		}

		public double getBaseLuminance() {
			return baseLuminance;
		}

	}

	private static class FramePillarMask implements Mask {

		private Frame frame;

		public FramePillarMask(Frame frame) {
			this.frame = frame;
		}

		@Override
		public boolean isMasked(double x, double y) {
			Point3D worldCoords = getFrame().fromPictureToWorldCoordinates(x, y);
			return Math.abs(worldCoords.getX()) <= 1 && Math.abs(worldCoords.getZ() - Z_OFFSET) <= 1;
		}

		public Frame getFrame() {
			return frame;
		}

	}

	private static class FrameData {

		private String imagePath;

		private int imageWidth;

		private int imageHeight;

		private Point3D center;

		private double scale;

		private double transparency;

		private double luminance;

		private boolean flipOrientation;

		public FrameData(String imagePath, int imageWidth, int imageHeight, Point3D center, double scale,
				double transparency, double luminance, boolean flipOrientation) {
			this.imagePath = imagePath;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.center = center;
			this.scale = scale;
			this.transparency = transparency;
			this.luminance = luminance;
			this.flipOrientation = flipOrientation;
		}

		public static FrameData fromCsv(String line) {
			StringTokenizer st = new StringTokenizer(line, ",");
			String imagePath = st.nextToken();
			int imageWidth = Integer.parseInt(st.nextToken());
			int imageHeight = Integer.parseInt(st.nextToken());
			Point3D center = new Point3D(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),
					Double.parseDouble(st.nextToken()));
			double scale = Double.parseDouble(st.nextToken());
			double transparency = Double.parseDouble(st.nextToken());
			double luminance = Double.parseDouble(st.nextToken());
			boolean flipOrientation = Boolean.parseBoolean(st.nextToken());
			return new FrameData(imagePath, imageWidth, imageHeight, center, scale, transparency, luminance,
					flipOrientation);
		}

		public String toCsv() {
			StringBuilder sb = new StringBuilder();
			sb.append(getImagePath());
			sb.append(',');
			sb.append(getImageWidth());
			sb.append(',');
			sb.append(getImageHeight());
			sb.append(',');
			sb.append(getCenter().getX());
			sb.append(',');
			sb.append(getCenter().getY());
			sb.append(',');
			sb.append(getCenter().getZ());
			sb.append(',');
			sb.append(getScale());
			sb.append(',');
			sb.append(getTransparency());
			sb.append(',');
			sb.append(getLuminance());
			sb.append(',');
			sb.append(isFlipOrientation());
			return sb.toString();
		}

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

		public int getImageWidth() {
			return imageWidth;
		}

		public void setImageWidth(int imageWidth) {
			this.imageWidth = imageWidth;
		}

		public int getImageHeight() {
			return imageHeight;
		}

		public void setImageHeight(int imageHeight) {
			this.imageHeight = imageHeight;
		}

		public Point3D getCenter() {
			return center;
		}

		public double getScale() {
			return scale;
		}

		public double getTransparency() {
			return transparency;
		}

		public void setTransparency(double transparency) {
			this.transparency = transparency;
		}

		public double getLuminance() {
			return luminance;
		}

		public boolean isFlipOrientation() {
			return flipOrientation;
		}

	}

}