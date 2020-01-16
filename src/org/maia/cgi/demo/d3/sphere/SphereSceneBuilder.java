package org.maia.cgi.demo.d3.sphere;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.demo.d3.sphere.model.SphereBuilder;
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

public class SphereSceneBuilder extends SceneBuilder {

	public SphereSceneBuilder() {
	}

	@Override
	public RenderOptions getDefaultRenderOptions() {
		RenderOptions options = super.getDefaultRenderOptions();
		options.setRenderWidth(940);
		options.setRenderHeight(700);
		options.setSceneBackgroundColor(Color.BLACK);
		return options;
	}

	@Override
	protected String getSceneName() {
		return "Spheres";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(Point3D.origin(), new Point3D(0, 0, 3.0), 50.0,
				options.getAspectRatio(), 0.5, 10.0);
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		SphereBuilder sb = new SphereBuilder();
		Collection<Object3D> objects = new Vector<Object3D>();
		objects.add(sb.buildCentralSphere());
		objects.add(sb.buildSolidMoon());
		objects.add(sb.buildSmallerTransparentMoon());
		objects.add(sb.buildLargerTransparentMoon());
		return objects;
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.1));
		lights.add(new SpotLight(new Point3D(-5.0, 3.0, 10.0), 1.0));
		lights.add(new SpotLight(new Point3D(5.0, -3.0, 10.0), 0.7));
		return lights;
	}

}