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
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.testeoa.grafo.Vertice;
import org.testeoa.modelo.Metodo;

/**
 * @author  Rafael
 */
public class PainelCodigo extends JPanel {
	
	/**
	 * @uml.property  name="unidades"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	Metodo[] unidades;
	
	List<JLabel> codigo = new ArrayList<JLabel>();
	
	public PainelCodigo(Metodo... unidades) {
		super();		
		this.unidades = unidades;
		setBackground(Color.WHITE);
		Font font = new Font("monospaced", Font.BOLD, 13);
		for (Metodo unidade : unidades) {
			JLabel nome = new JLabel(unidade.getNomeCompleto());
			nome.setOpaque(true);
			nome.setBackground(Color.black);
			nome.setForeground(Color.white);			
			codigo.add(nome);
			for (Vertice v : unidade.getGrafo().getVertices()) {
				JLabel l = new JLabel(v.getLabel().toString());
				l.setFont(font);
				l.setOpaque(true);
				l.setBackground(Color.LIGHT_GRAY);
				codigo.add(l);
				for (String ins : v.getInstrucoes()) {
					JLabel i = new JLabel("  " + ins);
					i.setFont(font);				
					codigo.add(i);
				}
			}
		}
		setLayout(new GridLayout(codigo.size(), 1));
		for (JLabel jl : codigo) {
			add(jl);
		}
	}
	
	public void pintar(Map<String, Color> cores) {
		for (JLabel l : codigo) {
			if (cores.get(l.getText()) != null) {
				l.setBackground(cores.get(l.getText()));
			}
		}
	}

}
