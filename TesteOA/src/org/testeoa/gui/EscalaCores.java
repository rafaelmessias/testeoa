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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.LineBorder;

/**
 * @author  Rafael
 */
public class EscalaCores extends JPanel {
	
	/**
	 * @uml.property  name="max"
	 */
	private int max = 0;
	
	private int legendas = 0;
	
	/**
	 * @uml.property  name="marcador"
	 */
	private int marcador = -1;
	
	private Font fonte = new Font("monospaced", Font.BOLD, 14);	
	
	BufferedImage imagem;
	
	public EscalaCores() {
		super(new BorderLayout());
		setPreferredSize(new Dimension(0, 30));
		setBorder(new LineBorder(Color.BLACK));
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	/**
	 * @param max  the max to set
	 * @uml.property  name="max"
	 */
	public void setMax(int m) {
		max = m;
	}
	
	/**
	 * @param marcador  the marcador to set
	 * @uml.property  name="marcador"
	 */
	public void setMarcador(int m) {
		marcador = m;
	}
	
	public void removerMarcador() {
		marcador = -1;
	}
	
	private int getValor(float x) {
		return Math.round((float)max / (float)getWidth() * x);
	}
	
	private float getX(int valor) {
		return (float)getWidth() / (float)max * valor;
	}
	
	public Color getCor(int valor) {
		imagem = (BufferedImage) createImage(getWidth(), getHeight());
		paint(imagem.createGraphics());
		int x = (int)getX(valor);
		// desviar das bordas
		if (x == 0) {
			x = 1;
		}
		if (x == imagem.getWidth()) {
			x -= 2;
		}
		return new Color(imagem.getRGB(x, 1));
	}
	
	@Override
	protected void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		// Desenho da Barra de Cores
		Graphics2D g2d = (Graphics2D) arg0;
		GradientPaint grad = new GradientPaint(
				0, 0, Color.RED, getWidth(), 0, Color.GREEN, false);
		g2d.setPaint(grad);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		// Desenho da Escala
		g2d.setColor(Color.BLACK);
		g2d.setFont(fonte);
		float x = 2;
		float y = getHeight() - 2;
		// 0
		g2d.drawString("0", x, y);
		if (max > 0) {
			// max
			String s = String.valueOf(max);
			int w = s.length() * 8 + 2;
			x = getWidth() - w;
			g2d.drawString(s, x, y);
			if (max < 7) {
				legendas = max + 1;
			}
			else {
				legendas = 7;
			}
			// o resto
			int n = 0;
			for (int i = 1; i <= (legendas - 2); i++) {			
				x = (float)getWidth() / (float)(legendas - 1) * i;
				n = getValor(x);
				s = String.valueOf(n);
				w = s.length() * 8 + 1;
				g2d.drawString(s, x, y);
			}
		}
		// marcador
		if (marcador > -1) {
			x = getX(marcador);
			g2d.drawLine((int)x, 2, (int)x, getHeight() - 12);			
			y = getHeight() / 3.0f;
			g2d.drawLine((int)x - 10, (int)y, (int)x + 10, (int)y);
			g2d.drawOval((int)x - 4, (int)y - 4, 8, 8);
//			int[] xp = {(int)x - 7, (int) x - 7, (int)x};
//			int[] yp = {(int)y - 7, (int) y + 7, (int)y};
//			g2d.fillPolygon(xp, yp, 3);
//			xp = new int[]{(int)x + 7, (int) x + 7, (int)x};
//			yp = new int[]{(int)y - 7, (int) y + 7, (int)y};
//			g2d.fillPolygon(xp, yp, 3);
		}
	}
	
	@Override
	public String getToolTipText(MouseEvent e) {
		return String.valueOf(getValor(e.getX()));
	}

}
