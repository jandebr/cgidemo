package org.maia.cgi.demo.d3.butterfly.model;

import java.awt.Color;
import java.util.List;

import org.maia.cgi.demo.d3.butterfly.shading.ButterflyWingShadingModel;
import org.maia.cgi.geometry.d3.LineSegment3D;
import org.maia.cgi.geometry.d3.Plane3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.object.ConvexPolygonalObject3D;
import org.maia.cgi.model.d3.object.ObjectSurfacePoint3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.render.d2.Mask;
import org.maia.cgi.render.d2.TextureMap;
import org.maia.cgi.render.d2.TextureMapHandle;
import org.maia.cgi.render.d2.TextureMapRegistry;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.render.d3.ReusableObjectPack;
import org.maia.cgi.transform.d3.TransformMatrix;

public class ButterflyWing extends ConvexPolygonalObject3D {

	private TransformMatrix objectToPictureTransformMatrix;

	private TransformMatrix pictureToMaskTransformMatrix;

	private TextureMapHandle pictureMapHandle;

	private Mask wingMask;

	private Mask wingContourMask;

	private TextureMap luminanceMap;

	private ButterflyWingShadingModel shadingModel;

	/**
	 * Creates a new butterfly wing.
	 * 
	 * @param p1
	 *            The wing's first vertex, in object coordinates.
	 * @param p2
	 *            The wing's second vertex, in object coordinates.
	 * @param p3
	 *            The wing's third vertex, in object coordinates.
	 */
	ButterflyWing(Point3D p1, Point3D p2, Point3D p3, TransformMatrix objectToPictureTransformMatrix,
			TransformMatrix pictureToMaskTransformMatrix, TextureMapHandle pictureMapHandle,
			ButterflyWingShadingModel shadingModel) {
		super(p1, p2, p3);
		this.objectToPictureTransformMatrix = objectToPictureTransformMatrix;
		this.pictureToMaskTransformMatrix = pictureToMaskTransformMatrix;
		this.pictureMapHandle = pictureMapHandle;
		this.shadingModel = shadingModel;
		this.wingMask = ButterflyFactory.getInstance().getWingMask();
		this.wingContourMask = ButterflyFactory.getInstance().getWingContourMask();
		this.luminanceMap = ButterflyFactory.getInstance().getWingLuminanceMap();
	}

	public boolean overlaps(ButterflyWing other) {
		List<Point3D> otherVertices = other.getVerticesInWorldCoordinates();
		List<Point3D> vertices = getVerticesInWorldCoordinates();
		Plane3D wingPlane = new Plane3D(vertices.get(0), vertices.get(1), vertices.get(2));
		return intersectsInWorld(new LineSegment3D(otherVertices.get(0), otherVertices.get(1)), wingPlane)
				|| intersectsInWorld(new LineSegment3D(otherVertices.get(0), otherVertices.get(2)), wingPlane)
				|| intersectsInWorld(new LineSegment3D(otherVertices.get(1), otherVertices.get(2)), wingPlane);
	}

	private boolean intersectsInWorld(LineSegment3D wingEdge, Plane3D wingPlane) {
		Point3D intersection = wingEdge.intersect(wingPlane);
		if (intersection != null) {
			Point3D objectPosition = fromWorldToObjectCoordinates(intersection);
			Point3D picturePosition = fromObjectToPictureCoordinates(objectPosition);
			Point3D maskPosition = fromPictureToMaskCoordinates(picturePosition);
			if (!getWingMask().isMasked(maskPosition.getX(), maskPosition.getZ())) {
				// real intersection
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean containsPointOnPlane(Point3D positionInCamera, Scene scene) {
		if (!super.containsPointOnPlane(positionInCamera, scene))
			return false;
		Point3D objectPosition = fromCameraToObjectCoordinates(positionInCamera, scene.getCamera());
		Point3D picturePosition = fromObjectToPictureCoordinates(objectPosition);
		Point3D maskPosition = fromPictureToMaskCoordinates(picturePosition);
		return !getWingMask().isMasked(maskPosition.getX(), maskPosition.getZ());
	}

	@Override
	protected Color sampleBaseColor(Point3D positionInCamera, Scene scene) {
		Point3D objectPosition = fromCameraToObjectCoordinates(positionInCamera, scene.getCamera());
		Point3D picturePosition = fromObjectToPictureCoordinates(objectPosition);
		return getWingPicture().sampleColor(picturePosition.getX(), picturePosition.getZ());
	}

	@Override
	protected void applySurfacePointShading(ObjectSurfacePoint3D surfacePoint, Scene scene, RenderOptions options,
			ReusableObjectPack reusableObjects) {
		Point3D positionInCamera = surfacePoint.getPositionInCamera();
		Point3D objectPosition = fromCameraToObjectCoordinates(positionInCamera, scene.getCamera());
		Point3D picturePosition = fromObjectToPictureCoordinates(objectPosition);
		Point3D maskPosition = fromPictureToMaskCoordinates(picturePosition);
		surfacePoint.setColor(getShadingModel().applyShading(surfacePoint.getColor(), positionInCamera, maskPosition,
				this, (ButterflyScene) scene, options));
	}

	private Point3D fromObjectToPictureCoordinates(Point3D point) {
		return getObjectToPictureTransformMatrix().transform(point);
	}

	private Point3D fromPictureToMaskCoordinates(Point3D point) {
		return getPictureToMaskTransformMatrix().transform(point);
	}

	private TransformMatrix getObjectToPictureTransformMatrix() {
		return objectToPictureTransformMatrix;
	}

	private TransformMatrix getPictureToMaskTransformMatrix() {
		return pictureToMaskTransformMatrix;
	}

	private TextureMapHandle getPictureMapHandle() {
		return pictureMapHandle;
	}

	private ButterflyWingShadingModel getShadingModel() {
		return shadingModel;
	}

	private TextureMap getWingPicture() {
		return TextureMapRegistry.getInstance().getTextureMap(getPictureMapHandle());
	}

	private Mask getWingMask() {
		return wingMask;
	}

	public Mask getWingContourMask() {
		return wingContourMask;
	}

	public TextureMap getLuminanceMap() {
		return luminanceMap;
	}

}