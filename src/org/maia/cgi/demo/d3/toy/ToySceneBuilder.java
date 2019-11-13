package org.maia.cgi.demo.d3.toy;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.compose.d3.DepthBlurParameters;
import org.maia.cgi.demo.d3.toy.model.ToyBuilder;
import org.maia.cgi.demo.d3.toy.model.ToyTheme;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.gui.d3.renderer.RenderOptions;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.PerspectiveViewVolume;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.camera.RevolvingCameraImpl;
import org.maia.cgi.model.d3.camera.ViewVolume;
import org.maia.cgi.model.d3.light.InboundLight;
import org.maia.cgi.model.d3.scene.Scene;

public class ToySceneBuilder {

	public ToySceneBuilder() {
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
		ToyTheme toyTheme = new ToyTheme();
		ToyBuilder toyBuilder = new ToyBuilder(toyTheme, 0.5);
		Scene scene = new Scene("Toy", camera);
		scene.addTopLevelObject(toyBuilder.build().translateZ(-4.0));
		scene.addLightSource(new InboundLight(new Vector3D(0, 0, -1.0)));
		scene.addLightSource(new InboundLight(new Vector3D(0, -1.0, 0)));
		scene.setAmbientColor(Compositing.setTransparency(options.getSceneBackgroundColor(), 1.0));
		scene.setDepthBlurParameters(new DepthBlurParameters(0.6, 1.0, 12.0));
		return scene;
	}

	private Camera createCamera(double aspectRatio) {
		Point3D pivotPoint = new Point3D(0, 0, -4.0);
		Point3D position = Point3D.origin();
		double viewAngleInDegrees = 50.0;
		double N = 0.5;
		double F = 10.0;
		ViewVolume viewVolume = PerspectiveViewVolume.createFromParameters(viewAngleInDegrees, aspectRatio, N, F);
		RevolvingCamera camera = new RevolvingCameraImpl(pivotPoint, position, viewVolume);
		return camera.revolveLongitudinal(Geometry.degreesToRadians(10.0)).revolveLatitudinal(
				Geometry.degreesToRadians(15.0));
	}

}