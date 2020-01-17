package org.maia.cgi.demo.d3.elephant;

import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.demo.d3.elephant.model.ElephantBuilder;
import org.maia.cgi.demo.d3.elephant.model.ElephantTheme;
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

public class ElephantSceneBuilder extends SceneBuilder {

	public ElephantSceneBuilder() {
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
		return "Elephant";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(Point3D.origin(), new Point3D(0, 0, 4.0), 50.0,
				options.getAspectRatio(), 0.5, 10.0);
		camera.revolveLongitudinal(Geometry.degreesToRadians(-30.0)).revolveLatitudinal(Geometry.degreesToRadians(15.0));
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		ElephantTheme theme = new ElephantTheme();
		ElephantBuilder builder = new ElephantBuilder(theme, 1.0);
		Collection<Object3D> objects = new Vector<Object3D>();
		objects.add(builder.build().translateY(-0.5));
		return objects;
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.2));
		lights.add(new SpotLight(new Point3D(-5.0, 5.0, -5.0), 0.6));
		lights.add(new SpotLight(new Point3D(5.0, 5.0, -5.0), 0.6));
		lights.add(new SpotLight(new Point3D(-5.0, 5.0, 5.0), 0.6));
		lights.add(new SpotLight(new Point3D(5.0, 5.0, 5.0), 0.6));
		return lights;
	}

}