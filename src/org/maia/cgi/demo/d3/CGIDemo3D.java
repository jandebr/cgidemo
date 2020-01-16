package org.maia.cgi.demo.d3;

import org.maia.cgi.demo.d3.butterfly.ButterflySceneBuilder;
import org.maia.cgi.demo.d3.cube.CubeSceneBuilder;
import org.maia.cgi.demo.d3.sphere.SphereSceneBuilder;
import org.maia.cgi.demo.d3.toy.ToySceneBuilder;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.gui.d3.renderer.DefaultRenderKit;
import org.maia.cgi.gui.d3.renderer.RenderFrame;
import org.maia.cgi.gui.d3.renderer.RenderKit;
import org.maia.cgi.gui.d3.renderer.RenderOptions;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.shading.d2.TextureMapRegistry;

public class CGIDemo3D {

	public CGIDemo3D() {
	}

	public static void main(String[] args) {
		TextureMapRegistry.getInstance().setCapacity(Integer.MAX_VALUE); // suppress warnings about capacity
		new CGIDemo3D().show();
	}

	public void show() {
		RenderKit kit = new DefaultRenderKit();
		RenderFrame frame = new RenderFrame(940, 700, kit, 1.0, Geometry.degreesToRadians(30.0));
		addAndShowToyScene(frame);
		addCubesScene(frame);
		addSpheresScene(frame);
		addButterfliesScene(frame);
	}

	private void addAndShowToyScene(RenderFrame frame) {
		ToySceneBuilder toyBuilder = new ToySceneBuilder();
		RenderOptions options = toyBuilder.getDefaultRenderOptions();
		Scene scene = toyBuilder.build(options);
		addAndShowScene(frame, scene, options);
	}

	private void addCubesScene(RenderFrame frame) {
		CubeSceneBuilder cubeBuilder = new CubeSceneBuilder();
		RenderOptions options = cubeBuilder.getDefaultRenderOptions();
		Scene scene = cubeBuilder.build(options);
		addScene(frame, scene, options);
	}

	private void addSpheresScene(RenderFrame frame) {
		SphereSceneBuilder sphereBuilder = new SphereSceneBuilder();
		RenderOptions options = sphereBuilder.getDefaultRenderOptions();
		Scene scene = sphereBuilder.build(options);
		addScene(frame, scene, options);
	}

	private void addButterfliesScene(RenderFrame frame) {
		ButterflySceneBuilder butterflyBuilder = new ButterflySceneBuilder();
		RenderOptions options = butterflyBuilder.getDefaultRenderOptions();
		Scene scene = butterflyBuilder.build(options);
		addScene(frame, scene, options);
	}

	private void addScene(RenderFrame frame, Scene scene, RenderOptions options) {
		frame.addToSceneSelectionMenu(scene, options);
	}

	private void addAndShowScene(RenderFrame frame, Scene scene, RenderOptions options) {
		addScene(frame, scene, options);
		frame.showWithLoadedScene(scene, options);
	}

}