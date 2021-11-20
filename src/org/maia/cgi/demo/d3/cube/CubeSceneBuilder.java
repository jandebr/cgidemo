package org.maia.cgi.demo.d3.cube;

import java.util.Collection;
import java.util.Vector;

import org.maia.cgi.demo.d3.cube.model.CubeBuilder;
import org.maia.cgi.geometry.Geometry;
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

public class CubeSceneBuilder extends SceneBuilder {

	public CubeSceneBuilder() {
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
		return "Cubes";
	}

	@Override
	protected Camera createCamera(RenderOptions options) {
		RevolvingCamera camera = createRevolvingCamera(new Point3D(0, 0, -4.0), Point3D.origin(), 60.0,
				options.getAspectRatio(), 0.5, 10.0);
		camera.revolveLongitudinal(Geometry.degreesToRadians(30.0)).revolveLatitudinal(Geometry.degreesToRadians(20.0));
		return camera;
	}

	@Override
	protected Collection<Object3D> createTopLevelObjects(RenderOptions options) {
		CubeBuilder cb = new CubeBuilder();
		Collection<Object3D> objects = new Vector<Object3D>();
		objects.add(cb.build().scale(0.4).translate(-0.5, 0.5, -4.0));
		objects.add(cb.build().scale(0.4).translate(0.5, 0.5, -4.0));
		objects.add(cb.build().scale(0.4).translate(-0.5, -0.5, -4.0));
		objects.add(cb.build().scale(0.4).translate(0.5, -0.5, -4.0));
		return objects;
	}

	@Override
	protected Collection<LightSource> createLightSources(Scene scene, RenderOptions options) {
		Collection<LightSource> lights = new Vector<LightSource>();
		lights.add(new AmbientLight(0.2));
		lights.add(new InboundLight(new Vector3D(0, 1.0, -1.0)));
		return lights;
	}

}