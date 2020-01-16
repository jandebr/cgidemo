package org.maia.cgi.demo.d3.toy;

import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.compose.Compositing;
import org.maia.cgi.compose.d3.DepthBlurParameters;
import org.maia.cgi.demo.d3.toy.model.ToyBuilder;
import org.maia.cgi.demo.d3.toy.model.ToyTheme;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.gui.d3.renderer.RenderOptions;
import org.maia.cgi.model.d3.camera.Camera;
import org.maia.cgi.model.d3.camera.RevolvingCamera;
import org.maia.cgi.model.d3.light.AmbientLight;
import org.maia.cgi.model.d3.light.LightSource;
import org.maia.cgi.model.d3.light.SpotLight;
import org.maia.cgi.model.d3.object.Object3D;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.model.d3.scene.SceneBuilder;
import org.maia.cgi.render.d3.view.ColorDepthBuffer;

public class ToySceneBuilder extends SceneBuilder {

	public ToySceneBuilder() {
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(940);
		options.setRenderHeight(700);
		return options;
	}

	@Override
	protected String getSceneName() {
		return "Toy";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, -4.0), Point3D.origin(), 50.0,
				options.getAspectRatio(), 0.5, 10.0);
		camera.revolveLongitudinal(Geometry.degreesToRadians(10.0)).revolveLatitudinal(Geometry.degreesToRadians(15.0));
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		Collection<Object3D> objects = new Vector<Object3D>();
		ToyTheme toyTheme = new ToyTheme();
		ToyBuilder toyBuilder = new ToyBuilder(toyTheme, 0.4);
		objects.add(toyBuilder.build().translate(0.1, -0.1, -3.5));
		return objects;
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.2));
		lights.add(new SpotLight(new Point3D(-3.72, 5.0, -8.0), 0.6));
		lights.add(new SpotLight(new Point3D(4.78, 5.0, -8.0), 0.6));
		lights.add(new SpotLight(new Point3D(-3.72, 5.0, -1.0), 0.6));
		lights.add(new SpotLight(new Point3D(4.78, 5.0, -1.0), 0.6));
		return lights;
	}

	@Override
	protected ColorDepthBuffer createBackdrop(Scene scene, RenderOptions options) {
		return new ColorDepthBuffer(Compositing.readImageFromFile("resources/toy/backdrop-940x700.png"), 10.0);
	}

	@Override
	protected DepthBlurParameters createDepthBlurParameters(Scene scene, RenderOptions options) {
		return new DepthBlurParameters(0.6, 1.0, 12.0);
	}

}