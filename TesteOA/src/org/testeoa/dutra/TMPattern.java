package org.testeoa.dutra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class TMPattern {
	
	public static Paint getPaint(String metodo, float max, float size) {
		if (metodo.equals("adendo")) {
			return getDotsPattern(TMSizeColorer.getColor(max, size));
		} else if (metodo.equals("metodo")) {
			return getPlusPattern(TMSizeColorer.getColor(max, size));
		} else {
			return Color.white;
		}
	}
	
	private static Paint getDotsPattern(Color color) {
		BufferedImage image =
            new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 5, 5);
        g.setColor(Color.DARK_GRAY);
        g.fillOval(1, 1, 3, 3);
        Rectangle r = new Rectangle(0, 0, 5, 5);
        Paint pattern = new TexturePaint(image, r);
        return pattern;
	}
	
	private static Paint getPlusPattern(Color color) {
		BufferedImage image =
            new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 10, 10);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(3, 5, 8, 5);
        g.drawLine(3, 6, 8, 6);
        g.drawLine(5, 3, 5, 8);
        g.drawLine(6, 3, 6, 8);
        Rectangle r = new Rectangle(0, 0, 10, 10);
        Paint pattern = new TexturePaint(image, r);
        return pattern;
	}

}
