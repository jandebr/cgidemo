package org.maia.cgi.demo.d3.butterfly.model;

import java.awt.Color;

import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.object.ObjectSurfacePoint3D;
import org.maia.cgi.model.d3.object.ObjectSurfacePoint3DImpl;
import org.maia.cgi.model.d3.object.PolygonalObject3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.render.d3.RenderOptions;
import org.maia.cgi.shading.d2.TextureMap;
import org.maia.cgi.shading.d2.TextureMapHandle;
import org.maia.cgi.shading.d2.TextureMapRegistry;
import org.maia.cgi.transform.d3.TransformMatrix;

public class Screen extends PolygonalObject3D {

	private TransformMatrix objectToPictureTransformMatrix;

	private TextureMapHandle pictureMapHandle;

	Screen(Point3D p1, Point3D p2, Point3D p3, Point3D p4, TransformMatrix objectToPictureTransformMatrix,
			TextureMapHandle pictureMapHandle) {
		super(p1, p2, p3, p4);
		this.objectToPictureTransformMatrix = objectToPictureTransformMatrix;
		this.pictureMapHandle = pictureMapHandle;
	}

	@Override
	protected ObjectSurfacePoint3D sampleSurfacePoint(Point3D positionInCamera, Scene scene, RenderOptions options,
			boolean applyShading) {
		ObjectSurfacePoint3D surfacePoint = null;
		Point3D objectPosition = fromCameraToObjectCoordinates(positionInCamera, scene.getCamera());
		Point3D picturePosition = fromObjectToPictureCoordinates(objectPosition);
		Color color = getScreenPicture().sampleColor(picturePosition.getX(), picturePosition.getZ());
		if (color != null) {
			surfacePoint = new ObjectSurfacePoint3DImpl(this, positionInCamera, color);
		}
		return surfacePoint;
	}

	private Point3D fromObjectToPictureCoordinates(Point3D point) {
		return getObjectToPictureTransformMatrix().transform(point);
	}

	private TransformMatrix getObjectToPictureTransformMatrix() {
		return objectToPictureTransformMatrix;
	}

	private TextureMapHandle getPictureMapHandle() {
		return pictureMapHandle;
	}

	private TextureMap getScreenPicture() {
		return TextureMapRegistry.getInstance().getTextureMap(getPictureMapHandle());
	}

}