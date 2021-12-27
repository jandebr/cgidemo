package org.maia.cgi.demo.d3;

import org.maia.cgi.demo.d3.axis.AxisSceneBuilder;
import org.maia.cgi.demo.d3.butterfly.ButterflySceneBuilder;
import org.maia.cgi.demo.d3.cube.CubeSceneBuilder;
import org.maia.cgi.demo.d3.curve.CurveSceneBuilder;
import org.maia.cgi.demo.d3.elephant.ElephantSceneBuilder;
import org.maia.cgi.demo.d3.layers.LayersSceneBuilder;
import org.maia.cgi.demo.d3.polygon.PolygonSceneBuilder;
import org.maia.cgi.demo.d3.sphere.SphereSceneBuilder;
import org.maia.cgi.demo.d3.toy.ToySceneBuilder;
import org.maia.cgi.demo.d3.transit.TransitSceneBuilder;
import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.gui.d3.renderer.DefaultRenderKit;
import org.maia.cgi.gui.d3.renderer.RenderFrame;
import org.maia.cgi.gui.d3.renderer.RenderKit;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.render.d2.TextureMapRegistry;
import org.maia.cgi.render.d3.RenderOptions;

public class CGIDemo3D {

	public CGIDemo3D() {
	}

	public static void main(String[] args) {
		TextureMapRegistry.getInstance().setCapacity(Integer.MAX_VALUE); // suppress warnings about capacity
		CGIDemo3D demo = new CGIDemo3D();
		demo.show();
	}

	public void show() {
		RenderKit kit = new DefaultRenderKit();
		RenderFrame frame = new RenderFrame(940, 700, kit, 1.0, Geometry.degreesToRadians(30.0));
		addPolygonScene(frame);
		addAxisScene(frame);
		addTransitScene(frame);
		addLayersScene(frame);
		addAndShowElephantScene(frame);
		// addToyScene(frame);
		addCubesScene(frame);
		addSpheresScene(frame);
		addCurveScene(frame);
		addButterfliesScene(frame);
	}

	private void addPolygonScene(RenderFrame frame) {
		PolygonSceneBuilder polygonBuilder = new PolygonSceneBuilder();
		RenderOptions options = polygonBuilder.getDefaultRenderOptions();
		Scene scene = polygonBuilder.build(options);
		addScene(frame, scene, options);
	}

	private void addAxisScene(RenderFrame frame) {
		AxisSceneBuilder axisBuilder = new AxisSceneBuilder();
		RenderOptions options = axisBuilder.getDefaultRenderOptions();
		Scene scene = axisBuilder.build(options);
		addScene(frame, scene, options);
	}

	private void addTransitScene(RenderFrame frame) {
		TransitSceneBuilder transitBuilder = new TransitSceneBuilder();
		RenderOptions options = transitBuilder.getDefaultRenderOptions();
		Scene scene = transitBuilder.build(options);
		addScene(frame, scene, options);
	}

	private void addLayersScene(RenderFrame frame) {
		LayersSceneBuilder layersBuilder = new LayersSceneBuilder();
		RenderOptions options = layersBuilder.getDefaultRenderOptions();
		Scene scene = layersBuilder.build(options);
		addScene(frame, scene, options);
	}

	private void addAndShowElephantScene(RenderFrame frame) {
		ElephantSceneBuilder elephantBuilder = new ElephantSceneBuilder();
		RenderOptions options = elephantBuilder.getDefaultRenderOptions();
		Scene scene = elephantBuilder.build(options);
		addAndShowScene(frame, scene, options);
	}

	private void addToyScene(RenderFrame frame) {
		ToySceneBuilder toyBuilder = new ToySceneBuilder();
		RenderOptions options = toyBuilder.getDefaultRenderOptions();
		Scene scene = toyBuilder.build(options);
		addScene(frame, scene, options);
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

	private void addCurveScene(RenderFrame frame) {
		CurveSceneBuilder curveBuilder = new CurveSceneBuilder();
		RenderOptions options = curveBuilder.getDefaultRenderOptions();
		Scene scene = curveBuilder.build(options);
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