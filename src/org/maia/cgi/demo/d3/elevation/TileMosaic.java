package org.maia.cgi.demo.d3.elevation;

import java.awt.Color;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.maia.cgi.geometry.Geometry;
import org.maia.cgi.model.d3.object.BaseObject3D;
import org.maia.cgi.model.d3.object.MultipartObject3D;
import org.maia.cgi.model.d3.object.ObjectSurfacePoint3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D;
import org.maia.cgi.model.d3.object.SimpleTexturedFace3D.PictureRegion;
import org.maia.cgi.model.d3.scene.Scene;
import org.maia.cgi.render.d2.ImageMaskFileHandle;
import org.maia.cgi.render.d2.ImageTextureMapFileHandle;
import org.maia.cgi.render.d2.Mask;
import org.maia.cgi.render.d2.TextureMapHandle;
import org.maia.cgi.render.d2.TextureMapRegistry;
import org.maia.cgi.render.d3.shading.FlatShadingModel;
import org.maia.cgi.render.d3.shading.FlatShadingModelImpl;

public class TileMosaic {

	private File tileMaskFolder;

	private File tilePictureFolder;

	private File tilePictureMask;

	private int tileCountX = 40;

	private int tileCountY = 27;

	private int tileMaskWidth = 48;

	private int tileMaskHeight = 27;

	private int tilePictureWidth = 160;

	private int tilePictureHeight = 90;

	public TileMosaic(File tileMaskFolder, File tilePictureFolder, File tilePictureMask, int tileCountX, int tileCountY,
			int tileMaskWidth, int tileMaskHeight, int tilePictureWidth, int tilePictureHeight) {
		this.tileMaskFolder = tileMaskFolder;
		this.tilePictureFolder = tilePictureFolder;
		this.tilePictureMask = tilePictureMask;
		this.tileCountX = tileCountX;
		this.tileCountY = tileCountY;
		this.tileMaskWidth = tileMaskWidth;
		this.tileMaskHeight = tileMaskHeight;
		this.tilePictureWidth = tilePictureWidth;
		this.tilePictureHeight = tilePictureHeight;
	}

	public BaseObject3D build(double mosaicWidth, double tileBaseTransparency, double lightReflectionFactor) {
		MultipartObject3D<BaseObject3D> mosaic = new MultipartObject3D<BaseObject3D>();
		double aspectRatio = (tileMaskWidth * tileCountX) / (double) (tileMaskHeight * tileCountY);
		double mosaicHeight = mosaicWidth / aspectRatio;
		double tileWidth = mosaicWidth / tileCountX;
		double tileHeight = mosaicHeight / tileCountY;
		String[][] grid = getTilePicturePathsGrid();
		for (int yi = 0; yi < tileCountY; yi++) {
			double yc = (mosaicHeight - tileHeight) / 2 - yi * tileHeight;
			for (int xi = 0; xi < tileCountX; xi++) {
				double xc = -(mosaicWidth - tileWidth) / 2 + xi * tileWidth;
				String picturePath = grid[yi][xi];
				String tilePath = new File(getTileMaskFolder(), "tile-y" + yi + "-x" + xi + ".png").getPath();
				Tile tile = buildTile(picturePath, tilePictureWidth, tilePictureHeight, tileWidth, tileHeight,
						tileBaseTransparency, tilePath, lightReflectionFactor);
				tile.translate(xc, yc, 0);
				mosaic.addPart(tile);
			}
		}
		return mosaic;
	}

	private Tile buildTile(String picturePath, int pictureWidth, int pictureHeight, double tileWidth, double tileHeight,
			double baseTransparency, String transparencyMapPath, double lightReflectionFactor) {
		double pictureToTileRatio = pictureWidth / (double) tileMaskWidth;
		FlatShadingModel shadingModel = new FlatShadingModelImpl(lightReflectionFactor, 3.0);
		TextureMapHandle pictureMapHandle = new ImageTextureMapFileHandle(picturePath);
		TextureMapHandle transparencyMapHandle = new ImageTextureMapFileHandle(transparencyMapPath, pictureToTileRatio);
		Tile tile = new Tile(shadingModel, pictureMapHandle, new PictureRegion(pictureWidth, pictureHeight),
				baseTransparency, transparencyMapHandle, getTileMask());
		tile.scaleX(tileWidth / 2).scaleZ(tileHeight / 2);
		tile.rotateX(Geometry.degreesToRadians(90));
		return tile;
	}

	private Mask getTileMask() {
		ImageMaskFileHandle handle = new ImageMaskFileHandle(getTilePictureMask().getPath(), Color.WHITE);
		return (Mask) TextureMapRegistry.getInstance().getTextureMap(handle);
	}

	private String[][] getTilePicturePathsGrid() {
		List<String> paths = getTilePicturePaths();
		Collections.shuffle(paths);
		int pi = 0;
		String[][] grid = new String[tileCountY][tileCountX];
		for (int yi = 0; yi < tileCountY; yi++) {
			for (int xi = 0; xi < tileCountX; xi++) {
				grid[yi][xi] = paths.get(pi % paths.size());
				pi++;
			}
		}
		return grid;
	}

	private List<String> getTilePicturePaths() {
		List<String> paths = new Vector<String>(1500);
		for (File file : getTilePictureFolder().listFiles()) {
			if (file.getName().endsWith(".png")) {
				paths.add(file.getPath());
			}
		}
		return paths;
	}

	public File getTileMaskFolder() {
		return tileMaskFolder;
	}

	public File getTilePictureFolder() {
		return tilePictureFolder;
	}

	public File getTilePictureMask() {
		return tilePictureMask;
	}

	private static class Tile extends SimpleTexturedFace3D {

		private double baseTransparency;

		public Tile(FlatShadingModel shadingModel, TextureMapHandle pictureMapHandle, PictureRegion pictureRegion,
				double baseTransparency, TextureMapHandle transparencyMapHandle, Mask pictureMask) {
			super(shadingModel, pictureMapHandle, pictureRegion, null, transparencyMapHandle, pictureMask);
			this.baseTransparency = baseTransparency;
		}

		@Override
		protected double sampleTransparency(ObjectSurfacePoint3D surfacePoint, Scene scene) {
			double transparency = super.sampleTransparency(surfacePoint, scene);
			if (Double.isNaN(transparency)) {
				return getBaseTransparency();
			} else {
				return 1.0 - (1.0 - transparency) * (1.0 - getBaseTransparency());
			}
		}

		public double getBaseTransparency() {
			return baseTransparency;
		}

	}

}