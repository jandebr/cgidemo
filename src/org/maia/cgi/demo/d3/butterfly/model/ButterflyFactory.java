package org.maia.cgi.demo.d3.butterfly.model;

import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Vector;

import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Box3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.shading.d2.ImageMaskFileHandle;
import org.maia.cgi.shading.d2.ImageTextureMapFileHandle;
import org.maia.cgi.shading.d2.Mask;
import org.maia.cgi.shading.d2.TextureMap;
import org.maia.cgi.shading.d2.TextureMapHandle;
import org.maia.cgi.shading.d2.TextureMapRegistry;
import org.maia.cgi.transform.d3.TransformMatrix;
import org.maia.cgi.transform.d3.Transformation;
import org.maia.cgi.transform.d3.TwoWayCompositeTransform;

public class ButterflyFactory {

	private static ButterflyFactory instance;

	private File maskAndTexturesDirectory;

	private ButterflyFactory() {
		setMaskAndTexturesDirectory(new File("resources/butterfly"));
	}

	public static ButterflyFactory getInstance() {
		if (instance == null) {
			setInstance(new ButterflyFactory());
		}
		return instance;
	}

	private static synchronized void setInstance(ButterflyFactory factory) {
		if (instance == null) {
			instance = factory;
		}
	}

	public List<String> getButterflyPictureFilePaths(File dir) {
		String[] filenames = dir.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return true;
			}
		});
		List<String> filePaths = new Vector<String>(filenames.length);
		for (String filename : filenames) {
			filePaths.add(new File(dir, filename).getPath());
		}
		return filePaths;
	}

	public Butterfly createButterfly(ButterflyDescriptor descriptor) {
		List<ButterflyWing> wings = createButterflyWings(descriptor);
		Butterfly butterfly = new Butterfly(wings);
		butterfly.scale(descriptor.getScalingFactor());
		butterfly.rotateZ(Geometry.degreesToRadians(descriptor.getPitchAngleInDegrees()));
		butterfly.rotateX(Geometry.degreesToRadians(descriptor.getRollAngleInDegrees()));
		butterfly.rotateY(Geometry.degreesToRadians(descriptor.getYawAngleInDegrees()));
		Point3D pos = descriptor.getPosition();
		butterfly.translate(pos.getX(), pos.getY(), pos.getZ());
		return butterfly;
	}

	private List<ButterflyWing> createButterflyWings(ButterflyDescriptor descriptor) {
		List<ButterflyWing> wings = new Vector<ButterflyWing>(4);
		wings.add(createButterflyWing(WingType.FRONT_LEFT, descriptor));
		wings.add(createButterflyWing(WingType.FRONT_RIGHT, descriptor));
		wings.add(createButterflyWing(WingType.BACK_LEFT, descriptor));
		wings.add(createButterflyWing(WingType.BACK_RIGHT, descriptor));
		return wings;
	}

	private ButterflyWing createButterflyWing(WingType wingType, ButterflyDescriptor descriptor) {
		List<Point3D> vertices = createArchetypeWingVertices();
		TwoWayCompositeTransform ct = new TwoWayCompositeTransform();
		// Translate to wing's pivot point
		ct.then(Transformation.getTranslationMatrix(-442.5, 0, -787.0));
		// Rotate in XZ plane to front_left position
		double wingAngle = -35.0 * (1.0 - descriptor.getWingOverlapFactor());
		ct.then(Transformation.getRotationYrollMatrix(Geometry.degreesToRadians(wingAngle)));
		// Flip and scale in XZ plane to fit the wing's quadrant
		double fx = 1.0, fz = 1.0;
		double xmax = 480.0, zmax = 270.0;
		if (wingType.equals(WingType.FRONT_LEFT)) {
		} else if (wingType.equals(WingType.FRONT_RIGHT)) {
			fz = -1.0;
		} else if (wingType.equals(WingType.BACK_LEFT)) {
			fx = -1.0;
		} else if (wingType.equals(WingType.BACK_RIGHT)) {
			fx = -1.0;
			fz = -1.0;
		}
		ct.then(getScalingMatrixToFit(ct.forwardTransform(vertices), xmax, zmax));
		ct.then(Transformation.getScalingMatrix(fx, 1.0, fz));
		// Translate to picture coordinates (for texture sampling)
		ct.then(Transformation.getTranslationMatrix(480.0, 0, 270.0));
		// Keep reverse transform (picture to mask)
		TransformMatrix pictureToMaskTransformMatrix = ct.getReverseCompositeMatrix();
		vertices = ct.forwardTransform(vertices);
		ct.reset();
		// Translate back to wing's pivot point
		ct.then(Transformation.getTranslationMatrix(-480.0, 0, -270.0));
		// Scale to object coordinates
		double sx = 1.0 / 960; // wing of unit length
		double sz = 1.0 / 540; // wing of unit width (= stretched)
		ct.then(Transformation.getScalingMatrix(sx, 1.0, sz));
		// Turn wing
		double wingAngleUp = descriptor.getWingPitchAngleInDegrees(wingType);
		if (wingType.equals(WingType.FRONT_RIGHT) || wingType.equals(WingType.BACK_RIGHT)) {
			wingAngleUp *= -1.0;
		}
		ct.then(Transformation.getRotationXrollMatrix(Geometry.degreesToRadians(wingAngleUp)));
		// Keep reverse transform (object to picture)
		TransformMatrix objectToPictureTransformMatrix = ct.getReverseCompositeMatrix();
		vertices = ct.forwardTransform(vertices);
		// Adjust vertices order such that normal vector on the wing plane points
		// upwards
		if (fx * fz < 0) {
			vertices.set(2, vertices.set(1, vertices.get(2)));
		}
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(descriptor.getPictureFilePath());
		return new ButterflyWing(vertices.get(0), vertices.get(1), vertices.get(2), objectToPictureTransformMatrix,
				pictureToMaskTransformMatrix, pictureMapHandle, descriptor.getWingShadingModel());
	}

	private List<Point3D> createArchetypeWingVertices() {
		List<Point3D> vertices = new Vector<Point3D>(3);
		vertices.add(new Point3D(442.5, 0, 860.7));
		vertices.add(new Point3D(941.8, 0, 0));
		vertices.add(new Point3D(-56.8, 0, 0));
		return vertices;
	}

	private TransformMatrix getScalingMatrixToFit(List<Point3D> points, double xmax, double zmax) {
		Box3D bbox = getBoundingBox(points);
		double sx = xmax / Math.max(Math.abs(bbox.getX1()), Math.abs(bbox.getX2()));
		double sz = zmax / Math.max(Math.abs(bbox.getZ1()), Math.abs(bbox.getZ2()));
		return Transformation.getScalingMatrix(sx, 1.0, sz);
	}

	private Box3D getBoundingBox(List<Point3D> points) {
		Point3D p = points.get(0);
		Box3D bbox = new Box3D(p.getX(), p.getX(), p.getY(), p.getY(), p.getZ(), p.getZ());
		for (int i = 1; i < points.size(); i++) {
			bbox.expandToContain(points.get(i));
		}
		return bbox;
	}

	Mask getWingMask() {
		ImageMaskFileHandle handle = new ImageMaskFileHandle(
				new File(getMaskAndTexturesDirectory(), "wing-mask.png").getPath(), Color.BLACK);
		return (Mask) TextureMapRegistry.getInstance().getTextureMap(handle);
	}

	Mask getWingContourMask() {
		ImageMaskFileHandle handle = new ImageMaskFileHandle(
				new File(getMaskAndTexturesDirectory(), "wing-contour.png").getPath(), Color.BLACK);
		return (Mask) TextureMapRegistry.getInstance().getTextureMap(handle);
	}

	TextureMap getWingLuminanceMap() {
		ImageTextureMapFileHandle handle = new ImageTextureMapFileHandle(new File(getMaskAndTexturesDirectory(),
				"wing-luminance.png").getPath());
		return TextureMapRegistry.getInstance().getTextureMap(handle);
	}

	public Screen createCanonicalScreen(String pictureFilePath, int pictureWidth, int pictureHeight) {
		List<Point3D> vertices = new Vector<Point3D>(4);
		vertices.add(new Point3D(0, 0, 0));
		vertices.add(new Point3D(0, 0, pictureHeight));
		vertices.add(new Point3D(pictureWidth, 0, pictureHeight));
		vertices.add(new Point3D(pictureWidth, 0, 0));
		TwoWayCompositeTransform ct = new TwoWayCompositeTransform();
		// Scale in XZ plane to size 2x2
		ct.then(Transformation.getScalingMatrix(2.0 / pictureWidth, 1.0, 2.0 / pictureHeight));
		// Translate to center
		ct.then(Transformation.getTranslationMatrix(-1.0, 0, -1.0));
		// Keep reverse transform (object to picture)
		TransformMatrix objectToPictureTransformMatrix = ct.getReverseCompositeMatrix();
		vertices = ct.forwardTransform(vertices);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(pictureFilePath);
		return new Screen(vertices.get(0), vertices.get(1), vertices.get(2), vertices.get(3),
				objectToPictureTransformMatrix, pictureMapHandle);
	}

	public File getMaskAndTexturesDirectory() {
		return maskAndTexturesDirectory;
	}

	public void setMaskAndTexturesDirectory(File maskAndTexturesDirectory) {
		this.maskAndTexturesDirectory = maskAndTexturesDirectory;
	}

}
