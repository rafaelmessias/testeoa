package org.testeoa.dutra;

import java.awt.Color;

public class TMSizeColorer {
	
	private final static float cMax = 180.0f;
		
	public static Color getColor(float max, float size) {
		float localMax = max == 0.0? 1.0f : max;
		
		float distRed, distGreen;
		float number = (size / localMax) * cMax;
		
		distRed = /*cMax*/ 255.0f - number;
		distGreen = number;
		return new Color((int)distRed, (int)distGreen, 0);
	}

}
