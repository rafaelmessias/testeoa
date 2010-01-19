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

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.testeoa.modelo.Metodo;


/**
 * @author    Rafael
 */
public class ViewUnidade extends JInternalFrame {
	
	Metodo unidade;
	
	JTabbedPane tabbedPane;
	
	JGraphAODU jgraph;
	
	PainelCodigo codigo;
	
	public ViewUnidade(Metodo unidade) {
		super("", true, true, true, true);
		this.unidade = unidade;
		
		setTitle(unidade.toString());
		
		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
		// TODO verificar por que tive que comentar essa linha aqui.
		
		jgraph = new JGraphAODU(null, unidade.getGrafo());
		codigo = new PainelCodigo(unidade);
		
		tabbedPane.addTab("Grafo", new JScrollPane(jgraph));
		tabbedPane.addTab("Bytecode", new JScrollPane(codigo));
		getContentPane().add(tabbedPane);
		setSize(500, 500);
	}
	
	/**
	 * @return  the unidade
	 * @uml.property  name="unidade"
	 */
	public Metodo getUnidade() {
		return unidade;
	}

}
