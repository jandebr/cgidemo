package org.maia.cgi.demo.d3.sphere.model;

import java.awt.Color;

import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.shading.d3.FlatShadingModelImpl;

public class SphereBuilder {

	public SphereBuilder() {
	}

	public BaseObject3D buildCentralSphere() {
		return ModelBuilderUtils.buildSphere(1.0, 360, new Color(209, 77, 77), new FlatShadingModelImpl(1.0, 3.0))
				.rotateY(Math.PI / 2);
	}

	public BaseObject3D buildSolidMoon() {
		return ModelBuilderUtils.buildSphere(0.2, 120, new Color(44, 81, 153), new FlatShadingModelImpl(1.0, 3.0))
				.rotateY(Math.PI / 2).translate(-0.6, 0.3, 1.5);
	}

	public BaseObject3D buildSmallerTransparentMoon() {
		return ModelBuilderUtils.buildSphere(0.1, 80, new Color(219, 207, 66, 100), new FlatShadingModelImpl(1.0, 3.0))
				.rotateY(Math.PI / 2).translate(0.6, -0.3, 1.0);
	}

	public BaseObject3D buildLargerTransparentMoon() {
		return ModelBuilderUtils.buildSphere(0.2, 100, new Color(91, 171, 85, 80), new FlatShadingModelImpl(1.0, 3.0))
				.rotateY(Math.PI / 2).translate(0.8, -0.55, 1.3);
	}

}