/*
 * Copyright 2007 Rafael Messias Martins
 * 
 * This file is part of TesteOA.
 * 
 * TesteOA is free software: you can redistribue it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TesteOA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TesteOA. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.testeoa.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class DashedBorder extends AbstractBorder {
	
	private final Insets insets = new Insets(2, 2, 2, 2);
	
	private final int length = 5;
	
	private final int space = 5;
	
	@Override
	public Insets getBorderInsets(Component arg0) {
		return insets;
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {		
//		--- draw horizontal ---
		for (int i = 0; i < width; i += length) {
			g.drawLine(i, y, i + length, y);
			g.drawLine(i, y + 1, i + length, y + 1);
			g.drawLine(i, height - 1, i + length, height - 1);
			g.drawLine(i, height - 2, i + length, height - 2);
			i += space;
		}
//		--- draw vertical ---
		for (int i = 0; i < height; i += length) {
			g.drawLine(0, i, 0, i + length);
			g.drawLine(1, i, 1, i + length);
			g.drawLine(width - 1, i, width - 1, i + length);
			g.drawLine(width - 2, i, width - 2, i + length);
			i += space;
		}
	}


}
