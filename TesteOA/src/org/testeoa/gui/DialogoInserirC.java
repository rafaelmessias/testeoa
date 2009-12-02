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
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;

/**
 * @author    Rafael
 */
public class DialogoInserirC extends JDialog {
	
	Classe classe;
	
	Classe tmpclasse;
	
	JTextField tfNome;
	JList mList;
	JButton bconf;

	public DialogoInserirC() {
		super(GUI.instancia, "Nova Classe", true);
		
		JPanel pn = new JPanel(new BorderLayout());
		add(pn, BorderLayout.NORTH);
		pn.setBorder(BorderFactory.createTitledBorder("Nome"));
		
		tfNome = new JTextField(18);
		tfNome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					carregar();
				}
			}
		});
		pn.add(tfNome, BorderLayout.CENTER);
		
		JPanel pns = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pn.add(pns, BorderLayout.SOUTH);
		JButton bcarr = new JButton("Carregar");
		bcarr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregar();
			}
		});
		pns.add(bcarr, BorderLayout.SOUTH);
		
		JPanel pc = new JPanel(new BorderLayout());
		pc.setBorder(BorderFactory.createTitledBorder("Métodos"));
		add(pc, BorderLayout.CENTER);
		
		mList = new JList();
		pc.add(new JScrollPane(mList), BorderLayout.CENTER);
		
		JPanel ps = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(ps, BorderLayout.SOUTH);
		bconf = new JButton("Confirmar");
		bconf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmar();
			}
		});
		bconf.setEnabled(false);
		ps.add(bconf);
		JButton bcanc = new JButton("Cancelar");
		bcanc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});
		ps.add(bcanc);
		
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
			tmpclasse = AnaliseEstatica.lerClasse(nome);
			mList.setListData(tmpclasse.getMetodos());
			bconf.setEnabled(true);
		}
		catch (ExAnaliseEstatica e) {
			GUI.instancia.exibirMensagemErro(e);
		}
	}
	
	private void confirmar() {
		List lm = Arrays.asList(mList.getSelectedValues());
		for (Metodo m : tmpclasse.getMetodos()) {
			if (!lm.contains(m)) {
				tmpclasse.remover(m);
			}
		}
		classe = tmpclasse;
		dispose();
	}
	
	private void cancelar() {
		dispose();
	}
	
	/**
	 * @return  the classe
	 * @uml.property  name="classe"
	 */
	public Classe getClasse() {
		return classe;
	}
	
}
