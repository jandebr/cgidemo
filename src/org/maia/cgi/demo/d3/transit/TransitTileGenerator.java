package org.maia.cgi.demo.d3.transit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.maia.cgi.compose.Compositing;

public class TransitTileGenerator {

	public static void main(String[] args) throws Exception {
		File baseDir = new File("resources/transit");
		File imageFile = new File(baseDir, "words/aws.png");
		File outputDir = new File(baseDir, "tiles/aws");
		new TransitTileGenerator().sliceImageIntoTiles(imageFile, 48, 27, outputDir);
	}

	private void sliceImageIntoTiles(File imageFile, int tileWidth, int tileHeight, File outputDir) throws IOException {
		outputDir.mkdirs();
		BufferedImage image = Compositing.readImageFromFile(imageFile.getPath());
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int tilesX = (int) Math.ceil((double) imageWidth / tileWidth);
		int tilesY = (int) Math.ceil((double) imageHeight / tileHeight);
		for (int yi = 0; yi < tilesY; yi++) {
			for (int xi = 0; xi < tilesX; xi++) {
				BufferedImage tileImage = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB);
				Compositing.fillImageWithSolidColor(tileImage, Color.WHITE);
				Graphics2D g2 = (Graphics2D) tileImage.getGraphics();
				g2.drawImage(image, -xi * tileWidth, -yi * tileHeight, null);
				g2.dispose();
				String fileName = "tile-y" + yi + "-x" + xi + ".png";
				Compositing.writeImageToFile(tileImage, new File(outputDir, fileName).getPath());
			}
		}
		System.out.println("Finished slicing " + tilesX + "x" + tilesY + " tiles");
	}

}