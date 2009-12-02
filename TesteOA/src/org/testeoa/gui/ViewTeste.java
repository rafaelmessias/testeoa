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
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.testeoa.criterios.Criterio;
import org.testeoa.grafo.Vertice;
import org.testeoa.modelo.Metodo;
import org.testeoa.teste.TesteInterMetodoAdendo;
import org.testeoa.teste.TesteIntraMetodo;

/**
 * @author    Rafael
 */
public class ViewTeste extends JInternalFrame {
	
	TesteIntraMetodo teste;
	
	JGraphAODU jgraph;
	
	PainelCodigo codigo;	
	
	JPanel resultado;
	
	JTabbedPane tabs;
	
	EscalaCores escala = new EscalaCores();

	public ViewTeste(TesteIntraMetodo t) {
		super(t.toString(), true, true, true, true);
		teste = t;
		
		tabs = 
			new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
		jgraph = new JGraphAODU(this, teste.getGrafo());
		
		if (t instanceof TesteInterMetodoAdendo) {
			TesteInterMetodoAdendo tm = (TesteInterMetodoAdendo)t;
			Vector<Metodo> v = new Vector<Metodo>();
			v.add(tm.getUnidade());
			v.addAll(Arrays.asList(tm.getUnidadesSecundarias()));
			codigo = new PainelCodigo(v.toArray(new Metodo[0]));
		}
		else {
			codigo = new PainelCodigo(teste.getUnidade());
		}
		
		JPanel pg = new JPanel(new BorderLayout());
		pg.add(new JScrollPane(jgraph));
		pg.add(escala, BorderLayout.SOUTH);
		tabs.addTab("Grafo", pg);
		
		jgraph.setMouseHandler();
		
		
		
		JPanel pc = new JPanel(new BorderLayout());
		pc.add(new JScrollPane(codigo));
		tabs.addTab("Bytecode", pc);
		getContentPane().add(tabs);
		
		resultado = new JPanel(new BorderLayout());
		tabs.addTab("Resultado", resultado);
		
		setSize(500, 500);		
		atualizar();		
	}
	
	public void removerMarcador() {
		if (teste.isExecutado()) {
			escala.removerMarcador();
			escala.repaint();
		}
	}
	
	public void definirMarcador(Vertice v) {
		if (teste.isExecutado()) {
			int freq = getFrequencia(v);					
			escala.setMarcador(freq);
			escala.repaint();
		}		
	}
	
	public void atualizar() {
		atualizarEscala();
		atualizarGrafo();
		atualizarCodigo();
		atualizarResultado();		
	}
	
	private void atualizarEscala() {
		int max = 0;
		if (teste.isExecutado()) {
			max = teste.getResultado().getCaminho().getMaxFreq();
		}
		escala.setMax(max);
		escala.repaint();
	}
	
	private void atualizarResultado() {
		resultado.removeAll();
		// Teste
		JPanel pn = new JPanel(new BorderLayout()); 
		pn.setBorder(
				BorderFactory.createTitledBorder("Teste: " + teste.getTipo()));
		resultado.add(pn, BorderLayout.NORTH);
		
		GridLayout gridl = new GridLayout(1, 2);
		JPanel pn1 = new JPanel(gridl);
		pn1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		pn.add(pn1);
		
		pn1.add(new JLabel("Unidade Principal:"));
		pn1.add(new JLabel(teste.getUnidade().getNomeCompleto()));
		
		if (teste instanceof TesteInterMetodoAdendo) {
			TesteInterMetodoAdendo tm = (TesteInterMetodoAdendo) teste;
			gridl.setRows(2);
			pn1.add(new JLabel("Unidades Secundárias:"));
			pn1.add(new JComboBox(tm.getUnidadesSecundarias()));
		}
		
		// Critérios
		JPanel pc = new JPanel(new BorderLayout());
		pc.setBorder(
				BorderFactory.createTitledBorder("Critérios de Abrangência"));
		resultado.add(pc);
		
		JPanel pc2 = new JPanel(new BorderLayout());
		pc2.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		JScrollPane jsp2 = new JScrollPane(pc2);
		jsp2.setBorder(null);
		pc.add(jsp2);
		
		JPanel pc3 = new JPanel();
		GridLayout layout = new GridLayout(teste.getCriterios().length, 2);
		layout.setVgap(5);
		pc3.setLayout(layout);			
		pc2.add(pc3, BorderLayout.NORTH);
		
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		
		for (Criterio c : teste.getCriterios()) {
			pc3.add(new JLabel(c.toString()));
			JProgressBar bar = 
					new JProgressBar(JProgressBar.HORIZONTAL, 0, 10000);			
			float f = teste.getResultado().getCriteriosMap().get(c);
			bar.setValue((int)(f * 10000));
			bar.setString(nf.format(f));
			bar.setStringPainted(true);
			pc3.add(bar);
		}				
		
		// JUnit
		JPanel ps = new JPanel(new BorderLayout());
		Border b3 = BorderFactory.createTitledBorder("Resultado JUnit");
		Border b4 =	BorderFactory.createEmptyBorder(2, 2, 2, 2);
		ps.setBorder(BorderFactory.createCompoundBorder(b3, b4));				
		resultado.add(ps, BorderLayout.SOUTH);
		
		GridLayout gridl2 = new GridLayout(3, 2);
		gridl2.setVgap(5);
		JPanel ps1 = new JPanel(gridl2);
		ps.add(ps1);
		
		JLabel msg = new JLabel();
		if (teste.isExecutado()) {
			if (teste.getResultado().wasSuccessful()) {
				msg.setText("TESTE BEM-SUCEDIDO!");
				msg.setForeground(Color.GREEN.darker());				
			}
			else {
				msg.setText("TESTE MAL-SUCEDIDO.");
				msg.setForeground(Color.RED);				
			}
		}
		else {
			msg.setText("TESTE NÃO EXECUTADO.");
			msg.setForeground(Color.BLACK);
		}
		ps1.add(msg);
		
		int runCount = teste.getResultado().getRunCount();
		int failureCount = teste.getResultado().getFailureCount();
		
		JProgressBar bar2 = new JProgressBar(JProgressBar.HORIZONTAL, 0, 10000);
		float p = 0.0f;
		if (teste.isExecutado()) {
			p = 1.0f - (float)failureCount / (float)runCount;
		}
		bar2.setValue((int)(p * 10000.0f));
		bar2.setString(nf.format(p));
		bar2.setStringPainted(true);
		ps1.add(bar2);		
		ps1.add(new JLabel(
				"Testes Executados: " + teste.getResultado().getRunCount()));
		ps1.add(new JLabel(
				"Tempo de Execução: " + teste.getResultado().getRunTime() +
				" ms"));
		ps1.add(new JLabel(
				"Testes Mal-Sucedidos: " + 
				teste.getResultado().getFailureCount()));
		ps1.add(new JLabel(
				"Testes Ignorados: " + teste.getResultado().getIgnoreCount()));
		
		resultado.validate();
	}
	
	private void atualizarGrafo() {
		if (isVisible()) {
			for (DefaultGraphCell c : jgraph.cells) {
				if (c.getUserObject() instanceof Vertice) {
					Color cor = Color.LIGHT_GRAY;
					if (teste.isExecutado()) {
						int freq = getFrequencia((Vertice)c.getUserObject());
						cor = escala.getCor(freq); 
					}
					GraphConstants.setGradientColor(c.getAttributes(), cor);
					jgraph.getGraphLayoutCache().editCell(c, c.getAttributes());
				}			
			}
		}		
	}
	
	public void redesenharGrafo() {
		jgraph = new JGraphAODU(this, teste.getGrafo());
		JPanel pg = new JPanel(new BorderLayout());
		pg.add(new JScrollPane(jgraph));
		pg.add(escala, BorderLayout.SOUTH);
		tabs.setComponentAt(0, pg);
		
	}
	
	private void atualizarCodigo() {		
		if (isVisible()) {
			Map<String, Color> cores = new HashMap<String, Color>();
			for (DefaultGraphCell c : jgraph.cells) {
				if (c.getUserObject() instanceof Vertice) {
					Vertice v = (Vertice) c.getUserObject();
					Color cor = Color.LIGHT_GRAY;
					if (teste.isExecutado()) {
						cor = escala.getCor(getFrequencia(v));
					}
					cores.put(v.getLabel().toString(), cor);
				}
			}
			codigo.pintar(cores);
		}		
	}
	
	private int getFrequencia(Vertice v) {
		if (teste.getResultado().getCaminho().contem(v)) {
			return teste.getResultado().getCaminho().getFrequencia(v);
		}
		return 0;
	}
	
	@Override
	public void setVisible(boolean v) {
		super.setVisible(v);
		if (v == true) {
			atualizarGrafo();
			atualizarCodigo();
		}
	}
	
	public void tabResultado() {
		tabs.setSelectedIndex(2);
	}

}
