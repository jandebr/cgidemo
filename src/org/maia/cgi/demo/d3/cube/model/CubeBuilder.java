package org.maia.cgi.demo.d3.cube.model;

import java.awt.Color;
import java.util.List;
import java.util.Vector;

import org.maia.cgi.geometry.d3.Point3D;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.SimpleFace3D;
import org.maia.cgi.shading.d3.FlatShadingModel;
import org.maia.cgi.shading.d3.FlatShadingModelImpl;

public class CubeBuilder {

	public CubeBuilder() {
	}

	public BaseObject3D build() {
		MultipartObject3D<SimpleFace3D> cube = new MultipartObject3D<SimpleFace3D>();
		FlatShadingModel shader = createShadingModel();
		List<Point3D> v = createVertices();
		cube.addPart(new SimpleFace3D(Color.RED, shader, v.get(0), v.get(1), v.get(5), v.get(4))); // front
		cube.addPart(new SimpleFace3D(Color.BLUE, shader, v.get(0), v.get(4), v.get(7), v.get(3))); // left
		cube.addPart(new SimpleFace3D(Color.CYAN, shader, v.get(1), v.get(2), v.get(6), v.get(5))); // right
		cube.addPart(new SimpleFace3D(Color.MAGENTA, shader, v.get(2), v.get(3), v.get(7), v.get(6))); // back
		cube.addPart(new SimpleFace3D(Color.GRAY, shader, v.get(0), v.get(3), v.get(2), v.get(1))); // bottom
		cube.addPart(new SimpleFace3D(Color.ORANGE, shader, v.get(4), v.get(5), v.get(6), v.get(7))); // top
		return cube;
	}

	protected FlatShadingModel createShadingModel() {
		return new FlatShadingModelImpl();
	}

	protected List<Point3D> createVertices() {
		List<Point3D> vertices = new Vector<Point3D>(8);
		// bottom face
		vertices.add(new Point3D(-1.0, -1.0, +1.0));
		vertices.add(new Point3D(+1.0, -1.0, +1.0));
		vertices.add(new Point3D(+1.0, -1.0, -1.0));
		vertices.add(new Point3D(-1.0, -1.0, -1.0));
		// top face
		vertices.add(new Point3D(-1.0, +1.0, +1.0));
		vertices.add(new Point3D(+1.0, +1.0, +1.0));
		vertices.add(new Point3D(+1.0, +1.0, -1.0));
		vertices.add(new Point3D(-1.0, +1.0, -1.0));
		return vertices;
	}

}