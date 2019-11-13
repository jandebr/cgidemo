package org.maia.cgi.demo.d3.cube;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.compose.d3.DepthBlurParameters;
import org.maia.cgi.demo.d3.cube.model.CubeBuilder;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.gui.d3.renderer.RenderOptions;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.PerspectiveViewVolume;
import org.maia.cgi.model.d3.camera.RevolvingCameraImpl;
import org.maia.cgi.model.d3.camera.ViewVolume;
import org.maia.cgi.model.d3.light.InboundLight;
import org.maia.cgi.model.d3.scene.Scene;

public class CubeSceneBuilder {

	public CubeSceneBuilder() {
	}

	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = RenderOptions.createDefaultOptions();
		options.setRenderWidth(940);
		options.setRenderHeight(700);
		return options;
	}

	public Scene build(RenderOptions options) {
		return createScene(options);
	}

	private Scene createScene(RenderOptions options) {
		Camera camera = createCamera(options.getAspectRatio());
		CubeBuilder cb = new CubeBuilder();
		Scene scene = new Scene("Cubes", camera);
		scene.addTopLevelObject(cb.build().scale(0.4).translate(-0.5, 0.5, -4.0));
		scene.addTopLevelObject(cb.build().scale(0.4).translate(0.5, 0.5, -4.0));
		scene.addTopLevelObject(cb.build().scale(0.4).translate(-0.5, -0.5, -4.0));
		scene.addTopLevelObject(cb.build().scale(0.4).translate(0.5, -0.5, -4.0));
		scene.addLightSource(new InboundLight(new Vector3D(0, 1.0, -1.0)));
		scene.setAmbientColor(Compositing.setTransparency(options.getSceneBackgroundColor(), 1.0));
		scene.setDepthBlurParameters(new DepthBlurParameters(0.6, 1.0, 12.0));
		return scene;
	}

	private Camera createCamera(double aspectRatio) {
		Point3D pivotPoint = new Point3D(0, 0, -4.0);
		Point3D position = Point3D.origin();
		double viewAngleInDegrees = 60.0;
		double N = 0.5;
		double F = 10.0;
		ViewVolume viewVolume = PerspectiveViewVolume.createFromParameters(viewAngleInDegrees, aspectRatio, N, F);
		return new RevolvingCameraImpl(pivotPoint, position, viewVolume);
	}

}