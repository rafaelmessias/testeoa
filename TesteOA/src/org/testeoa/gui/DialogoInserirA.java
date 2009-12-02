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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.testeoa.estatica.AnaliseEstatica;
import org.testeoa.excecoes.ExAnaliseEstatica;
import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Metodo;

/**
 * @author    Rafael
 */
public class DialogoInserirA extends JDialog {
	
	Aspecto aspecto;
	
	Aspecto tmpasp;
	
	JTextField tfNome;
	JList mList;
	JList aList;
	JButton bconf;

	public DialogoInserirA() {
		super(GUI.instancia, "Novo Aspecto", true);
		
		JPanel p1 = new JPanel(new BorderLayout());
		add(p1, BorderLayout.NORTH);
		p1.setBorder(BorderFactory.createTitledBorder("Nome"));
		
		tfNome = new JTextField(20);
		tfNome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					carregar();
				}
			}
		});
		p1.add(tfNome, BorderLayout.CENTER);
		
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p1.add(p2, BorderLayout.SOUTH);
		JButton bcarr = new JButton("Carregar");
		bcarr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregar();
			}
		});
		p2.add(bcarr);
		
		JPanel p3 = new JPanel(new BorderLayout());
		add(p3, BorderLayout.WEST);
		p3.setBorder(BorderFactory.createTitledBorder("Métodos"));				
		mList = new JList();
		p3.add(new JScrollPane(mList));
		
		JPanel p4 = new JPanel(new BorderLayout());
		p4.setBorder(BorderFactory.createTitledBorder("Adendos"));
		add(p4, BorderLayout.EAST);
		aList = new JList();
		p4.add(new JScrollPane(aList));
		
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(p5, BorderLayout.SOUTH);
		bconf = new JButton("Confirmar");
		bconf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmar();
			}
		});
		bconf.setEnabled(false);
		p5.add(bconf);
		JButton bcanc = new JButton("Cancelar");
		bcanc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});
		p5.add(bcanc);
		
		pack();
		setLocationRelativeTo(GUI.instancia);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public void executar() {
		setVisible(true);
	}
	
	private void carregar() {
		String nome = tfNome.getText();
		try {
			tmpasp = AnaliseEstatica.lerAspecto(nome);
			mList.setListData(tmpasp.getMetodos());
			aList.setListData(tmpasp.getAdendos());
			bconf.setEnabled(true);
		}
		catch (ExAnaliseEstatica e) {
			GUI.instancia.exibirMensagemErro(e);
		}
	}
	
	private void confirmar() {
		List lm = Arrays.asList(mList.getSelectedValues());
		for (Metodo m : tmpasp.getMetodos()) {
			if (!lm.contains(m)) {
				tmpasp.remover(m);
			}
		}
		List la = Arrays.asList(aList.getSelectedValues());
		for (Adendo a : tmpasp.getAdendos()) {
			if (!la.contains(a)) {
				tmpasp.remover(a);
			}
		}
		aspecto = tmpasp;
		dispose();
	}
	
	private void cancelar() {
		dispose();
	}
	
	/**
	 * @return  the aspecto
	 * @uml.property  name="aspecto"
	 */
	public Aspecto getAspecto() {
		return aspecto;
	}
	
}
