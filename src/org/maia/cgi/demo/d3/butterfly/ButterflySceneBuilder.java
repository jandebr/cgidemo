package org.maia.cgi.demo.d3.butterfly;

import java.awt.Color;
import java.io.File;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.compose.d3.DepthBlurParameters;
import org.maia.cgi.compose.d3.DepthFunction;
import org.maia.cgi.compose.d3.SigmoidDepthFunction;
import org.maia.cgi.demo.d3.butterfly.model.ButterflyScene;
import org.maia.cgi.demo.d3.butterfly.model.HandsButterflyScene;
import org.maia.cgi.geometry.d3.Box3D;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.gui.d3.renderer.RenderOptions;
import org.maia.cgi.model.d3.CoordinateFrame;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.PerspectiveViewVolume;
import org.maia.cgi.model.d3.camera.RevolvingCameraImpl;
import org.maia.cgi.model.d3.camera.ViewVolume;
import org.maia.cgi.model.d3.light.InboundLight;
import org.maia.cgi.model.d3.scene.Scene;

public class ButterflySceneBuilder {

	public ButterflySceneBuilder() {
	}

	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = RenderOptions.createDefaultOptions();
		options.setRenderWidth(1280);
		options.setRenderHeight(720);
		options.setSceneBackgroundColor(Color.BLACK);
		options.setWireframeColorNear(Color.WHITE);
		options.setWireframeColorFar(Color.DARK_GRAY);
		return options;
	}

	public ButterflyScene build(RenderOptions options) {
		return createScene(options);
	}

	private ButterflyScene createScene(RenderOptions options) {
		Camera camera = createCamera(options.getAspectRatio());
		ButterflyScene scene = new HandsButterflyScene(camera, 500, new File("resources/butterfly/wingcovers/art"));
		scene.addLightSource(new InboundLight(new Vector3D(0, -1.0, -0.2).getUnitVector()));
		scene.getRenderParameters()
				.setAmbientColor(Compositing.setTransparency(options.getSceneBackgroundColor(), 1.0));
		scene.getRenderParameters().setShadowsEnabled(options.isShadowsEnabled());
		scene.getRenderParameters().setDarknessDepthFunction(createDarknessDepthFunction(scene, 0.6, 0.4));
		scene.getRenderParameters().setDepthBlurParameters(new DepthBlurParameters(0.6, 0.3, 4.0));
		scene.setName("Butterflies");
		return scene;
	}

	private Camera createCamera(double aspectRatio) {
		Point3D pivotPoint = new Point3D(0, -1.0, -24.0);
		Point3D position = new Point3D(0, -1.0, 11.0);
		double viewAngleInDegrees = 60.0;
		double N = 0.5;
		double F = 100.0;
		ViewVolume viewVolume = PerspectiveViewVolume.createFromParameters(viewAngleInDegrees, aspectRatio, N, F);
		return new RevolvingCameraImpl(pivotPoint, position, viewVolume);
	}

	private DepthFunction createDarknessDepthFunction(Scene scene, double relativeInflectionDepth, double smoothness) {
		Box3D bbox = scene.getBoundingBox(CoordinateFrame.CAMERA);
		double nearDepth = -bbox.getZ2();
		double farDepth = -bbox.getZ1();
		return SigmoidDepthFunction.createFilter(nearDepth, farDepth, relativeInflectionDepth, smoothness);
	}

}