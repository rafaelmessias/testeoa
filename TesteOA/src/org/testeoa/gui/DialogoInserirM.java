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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;

/**
 * @author    Rafael
 */
public class DialogoInserirM extends JDialog {
	
	private Classe classe;
	
	private JList mList;
	
	/**
	 * @uml.property  name="metodos"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Metodo[] metodos = new Metodo[0];
	
	public DialogoInserirM(Classe c) {
		super(GUI.instancia, "Nova Unidade", true);
		classe = c;
		
		JPanel p1 = new JPanel(new BorderLayout());
		add(p1);
		p1.setBorder(new TitledBorder("Métodos Não-Utilizados"));		
		mList = new JList(classe.getMetodosN());
		JScrollPane jsp = new JScrollPane(mList);
		jsp.setPreferredSize(new Dimension(259, 131));
		p1.add(jsp);
		
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(p2, BorderLayout.SOUTH);
		JButton bconf = new JButton("Inserir");
		bconf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inserir();
			}
		});
		p2.add(bconf);		
		JButton bcanc = new JButton("Cancelar");
		bcanc.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				cancelar();		
			}		
		});
		p2.add(bcanc);
		
		pack();
		setLocationRelativeTo(GUI.instancia);
	}
	
	public void executar() {
		setVisible(true);
	}
	
	private void inserir() {
		Vector<Metodo> vm = new Vector<Metodo>();
		for (Object o : mList.getSelectedValues()) {
			vm.add((Metodo)o);
		}
		metodos = vm.toArray(new Metodo[0]);
		dispose();
	}
	
	private void cancelar() {
		dispose();
	}
	
	/**
	 * @return  the metodos
	 * @uml.property  name="metodos"
	 */
	public Metodo[] getMetodos() {
		return metodos;
	}

}
