package org.maia.cgi.demo.d3.elevation;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import org.maia.cgi.compose.Compositing;

public class KeyFrameGenerator {

	public static void main(String[] args) {
		new KeyFrameGenerator().generateKeyFrames(160, 90, 300, new File("resources/elevation/frames/keys"));
	}

	private void generateKeyFrames(int frameWidth, int frameHeight, int frameCount, File outputDir) {
		outputDir.mkdirs();
		for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
			File outputFile = new File(outputDir, "keyframe-" + frameIndex + ".png");
			Compositing.writeImageToFile(createKeyFrame(frameWidth, frameHeight, frameIndex), outputFile.getPath());
		}
	}

	private BufferedImage createKeyFrame(int frameWidth, int frameHeight, int frameIndex) {
		BufferedImage frame = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
		Compositing.fillImageWithSolidColor(frame, new Color(16, 26, 103));
		Graphics2D g2 = (Graphics2D) frame.getGraphics();
		g2.setColor(new Color(105, 122, 255));
		g2.drawLine(0, 0, frameWidth - 1, frameHeight - 1);
		g2.drawLine(0, frameHeight - 1, frameWidth - 1, 0);
		String label = String.valueOf(frameIndex);
		g2.setFont(g2.getFont().deriveFont(50f).deriveFont(Font.BOLD));
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D bounds = fm.getStringBounds(label, g2);
		int x = (int) Math.round((frameWidth - bounds.getWidth()) / 2.0);
		int yBase = (int) Math.round((frameHeight + fm.getAscent()) / 2.0);
		g2.setColor(Color.WHITE);
		g2.drawString(label, x, yBase - 8);
		g2.fillRect(x, yBase, (int) bounds.getWidth(), 4);
		g2.dispose();
		return frame;
	}

}