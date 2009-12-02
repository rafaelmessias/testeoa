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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicTreeUI.MouseHandler;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.plaf.basic.BasicGraphUI;
import org.testeoa.grafo.AODU;
import org.testeoa.grafo.Aresta;
import org.testeoa.grafo.ArestaExcecao;
import org.testeoa.grafo.Vertice;

/**
 * @author    Rafael
 */
public class JGraphAODU extends JGraph {
	
	AODU grafo;
	
	Vector<DefaultGraphCell> cells = new Vector<DefaultGraphCell>();
	
	/**
	 * @uml.property  name="nivel"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Integer"
	 */
	Map<DefaultGraphCell, Integer> nivel = 
			new HashMap<DefaultGraphCell, Integer>();
	
	/**
	 * @uml.property  name="filhos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.List<DefaultGraphCell>"
	 */
	Map<DefaultGraphCell, List<DefaultGraphCell>> filhos =
			new HashMap<DefaultGraphCell, List<DefaultGraphCell>>();
	
	/**
	 * @uml.property  name="pos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Long"
	 */
	Map<Integer, Long> pos = new HashMap<Integer, Long>();
	
	public JGraphAODU(final ViewTeste vt, AODU g) {
		grafo = g;
		
		ToolTipManager.sharedInstance().registerComponent(this);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		
		setEditable(false);
		
		GraphModel model = new DefaultGraphModel();
		
		GraphLayoutCache layout = 
			new GraphLayoutCache(model, new DefaultCellViewFactory());
		setGraphLayoutCache(layout);
		
		DefaultGraphCell entrada = null;
		
		for (Vertice v : grafo.getVertices()) {
			DefaultGraphCell cell = new DefaultGraphCell(v);
			
			if (grafo.isEntrada(v)) {
				entrada = cell;
			}
			
			GraphConstants.setGradientColor(
						cell.getAttributes(),
						Color.LIGHT_GRAY);
			
			Border b = null;
			if (grafo.isTransversal(v)) {
				b = new DashedBorder();
			}
			else {
				b = BorderFactory.createLineBorder(Color.black, 2);
			}
			
			if (grafo.isSaida(v)) {
				b = BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(Color.BLACK, 2),
						BorderFactory.createCompoundBorder(
								BorderFactory.createEmptyBorder(2, 2, 2, 2), b));
			}
			
			GraphConstants.setBorder(cell.getAttributes(), b);
			
			GraphConstants.setOpaque(
					cell.getAttributes(),
					true);
			
			cell.add(new DefaultPort());
			cells.add(cell);
		}
		
		Vector<DefaultEdge> edges = new Vector<DefaultEdge>();
		Vector<DefaultEdge> edgesex = new Vector<DefaultEdge>();
		for (Aresta a : grafo.getArestas()) {
			DefaultEdge edge = new DefaultEdge();
			for (DefaultGraphCell cell : cells) {
				if (cell.getUserObject().equals(a.getOrigem())) {
					edge.setSource(cell.getChildAt(0));
				}
				if (cell.getUserObject().equals(a.getDestino())) {
					edge.setTarget(cell.getChildAt(0));
				}
			}
			GraphConstants.setLineEnd(
					edge.getAttributes(), 
					GraphConstants.ARROW_CLASSIC);
			GraphConstants.setEndFill(
					edge.getAttributes(),
					true);
			GraphConstants.setLineWidth(edge.getAttributes(), 2);
			if (a instanceof ArestaExcecao) {
				GraphConstants.setDashPattern(
						edge.getAttributes(),
						new float[] {5, 7});				
			}
			edgesex.add(edge);
		}
		layout.insert(edgesex.toArray(new DefaultEdge[0]));
		layout.insert(edges.toArray(new DefaultEdge[0]));
		
		new TreeLayoutSimples(this, entrada).aplicar();
		
		layout.insert(cells.toArray(new DefaultGraphCell[0]));
		
		setUI(new BasicGraphUI() {
			@Override
			protected MouseListener createMouseListener() {				
				return new MouseHandler() {
					public void mousePressed(MouseEvent e) {
						if (vt != null) {
							DefaultGraphCell c = (DefaultGraphCell)
									getFirstCellForLocation(e.getX(), e.getY());
							if (c == null) {								
								vt.removerMarcador();
							}
							else if (c.getUserObject() instanceof Vertice){								
								vt.definirMarcador((Vertice)c.getUserObject());
							}
						}
						super.mousePressed(e);
					}
				};
			}
		});
	}
	
	@Override
	public String getToolTipText(MouseEvent e) {
		DefaultGraphCell c = 
			(DefaultGraphCell) getFirstCellForLocation(e.getX(), e.getY());
		String t = null;
		if (c != null) {
			if (c.getUserObject() instanceof Vertice) {
				Vertice v = (Vertice) c.getUserObject();
				if (v.getDefinicoes().length > 0) {
					t = "<html><b>Definições:<br>";
					for (String def : v.getDefinicoes()) {
						t += "- " + def + "<br>";
					}
				}
				if (v.getUsos().length > 0) {
					if (t == null) {
						t = "<html><b>";
					}
					t += "Usos:<br>";
					for (String uso : v.getUsos()) {
						t += "- " + uso + "<br>";
					}
				}
				if (t != null) {
					t += "</b></html>";
				}
			}
		}
		return t;
	}
	
	public void setMouseHandler() {
		
	}
	
}
