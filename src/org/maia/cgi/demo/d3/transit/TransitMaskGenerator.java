package org.maia.cgi.demo.d3.transit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.maia.cgi.compose.Compositing;

public class TransitMaskGenerator {

	public static void main(String[] args) throws Exception {
		File baseDir = new File("resources/transit");
		File imageFile = new File(baseDir, "Thunderbird2.jpg");
		File outputDir = new File(baseDir, "masks");
		new TransitMaskGenerator().sliceImageIntoTiles(imageFile, 200, 100, outputDir);
	}

	private void sliceImageIntoTiles(File imageFile, int tileWidth, int tileHeight, File outputDir) throws IOException {
		BufferedImage image = Compositing.readImageFromFile(imageFile.getPath());
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int tilesX = (int) Math.ceil((double) imageWidth / tileWidth);
		int tilesY = (int) Math.ceil((double) imageHeight / tileHeight);
		for (int yi = 0; yi < tilesY; yi++) {
			for (int xi = 0; xi < tilesX; xi++) {
				
			}
		}
	}

}