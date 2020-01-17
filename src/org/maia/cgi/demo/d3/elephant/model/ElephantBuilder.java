package org.maia.cgi.demo.d3.elephant.model;

import org.maia.cgi.model.d3.ModelBuilderUtils;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;

public class ElephantBuilder {

	private ElephantTheme theme;

	private double precision;

	public ElephantBuilder(ElephantTheme theme, double precision) {
		this.theme = theme;
		this.precision = precision;
	}

	public BaseObject3D build() {
		MultipartObject3D<BaseObject3D> model = new MultipartObject3D<BaseObject3D>();
		model.addPart(ModelBuilderUtils.buildSphere(1.0, 60, getTheme().getBodyPartColor(), getTheme()
				.getBodyPartShadingModel()));
		return model;
	}

	public ElephantTheme getTheme() {
		return theme;
	}

	public double getPrecision() {
		return precision;
	}

}