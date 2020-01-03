package org.maia.cgi.demo.d3.butterfly.model;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.MovableCamera;
import org.maia.cgi.model.d3.light.DirectionalLightSource;
import org.maia.cgi.model.d3.light.PositionalLightSource;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneUtils;

public abstract class ButterflyScene extends Scene {

	private File wingPicturesDirectory;

	protected ButterflyScene(String name, Camera camera, int numberOfButterflies, File wingPicturesDirectory) {
		super(name, camera);
		setWingPicturesDirectory(wingPicturesDirectory);
		make(numberOfButterflies);
	}

	public void addButterfly(Butterfly butterfly) {
		addTopLevelObject(butterfly);
	}

	public void addButterfly(ButterflyDescriptor descriptor) {
		addButterfly(ButterflyFactory.getInstance().createButterfly(descriptor));
	}

	protected abstract void make(int numberOfButterflies);

	protected boolean overlapsWithButterflyInScene(Butterfly butterfly) {
		for (Butterfly other : getButterflies()) {
			if (butterfly.overlaps(other))
				return true;
		}
		return false;
	}

	public Collection<Butterfly> getButterflies() {
		Collection<Butterfly> butterflies = new Vector<Butterfly>(getTopLevelObjects().size());
		for (Object3D object : getTopLevelObjects()) {
			if (object instanceof Butterfly) {
				butterflies.add((Butterfly) object);
			}
		}
		return butterflies;
	}

	public int getButterflyCount() {
		return getButterflies().size();
	}

	public double getDistanceToNearestSpotLight(Point3D positionInCamera) {
		double minDistance = Double.MAX_VALUE;
		for (PositionalLightSource spotLight : SceneUtils.getAllPositionalLightSourcesInScene(this)) {
			double distance = computeDistance(positionInCamera, spotLight);
			minDistance = Math.min(minDistance, distance);
		}
		return minDistance;
	}

	private double computeDistance(Point3D positionInCamera, PositionalLightSource spotLight) {
		double vz = getCamera().getViewVolume().getViewPlaneZ();
		double fz = getCamera().getViewVolume().getFarPlaneZ();
		double scaleZ = getCamera().getViewVolume().getViewPlaneRectangle().getWidth() / -fz;
		// Project light onto near plane
		Point3D lp = spotLight.getPositionInWorld();
		double scale = vz / lp.getZ();
		double lx = scale * lp.getX();
		// double ly = scale * lp.getY();
		double lz = scaleZ * lp.getZ();
		// Project position onto near plane
		scale = vz / positionInCamera.getZ();
		double px = scale * positionInCamera.getX();
		// double py = scale * positionInCamera.getY();
		double pz = scaleZ * positionInCamera.getZ();
		// Distance
		return new Point3D(lx, 0, lz).distanceTo(new Point3D(px, 0, pz));
	}

	@Override
	public MovableCamera getCamera() {
		return (MovableCamera) super.getCamera();
	}

	public Vector3D getLightDirection() {
		Collection<DirectionalLightSource> ls = SceneUtils.getAllDirectionalLightSourcesInScene(this);
		return ls.isEmpty() ? null : ls.iterator().next().getDirection();
	}

	public File getWingPicturesDirectory() {
		return wingPicturesDirectory;
	}

	public void setWingPicturesDirectory(File wingPicturesDirectory) {
		this.wingPicturesDirectory = wingPicturesDirectory;
	}

}