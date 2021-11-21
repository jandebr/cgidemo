package org.maia.cgi.demo.d3.butterfly;

import java.awt.Color;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import org.maia.cgi.compose.d3.DepthBlurParameters;
import org.maia.cgi.demo.d3.butterfly.model.HandsButterflyScene;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.geometry.d3.Vector3D;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.InboundLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.render.d3.RenderOptions;

public class ButterflySceneBuilder extends SceneBuilder {

	public ButterflySceneBuilder() {
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(1280);
		options.setRenderHeight(720);
		options.setSceneBackgroundColor(Color.BLACK);
		options.setWireframeColorNear(Color.WHITE);
		options.setWireframeColorFar(Color.DARK_GRAY);
		return options;
	}

	@Override
	protected Scene createEmptyScene(RenderOptions options) {
		return new HandsButterflyScene(getSceneName(), createCamera(options), 500, new File(
				"resources/butterfly/wingcovers/art"));
	}

	@Override
	protected String getSceneName() {
		return "Butterflies";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, -1.0, -24.0), new Point3D(0, -1.0, 11.0), 60.0,
				options.getAspectRatio(), 0.5, 100.0);
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		// objects added via ButterflyScene constructor
		return Collections.emptyList();
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.2));
		lights.add(new InboundLight(new Vector3D(0, -1.0, -0.2).getUnitVector()));
		return lights;
	}

	@Override
	protected DepthBlurParameters createDepthBlurParameters(Scene scene, RenderOptions options) {
		return new DepthBlurParameters(0.6, 0.3, 4.0);
	}

}