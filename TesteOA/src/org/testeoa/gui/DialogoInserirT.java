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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.testeoa.criterios.Criterio;
import org.testeoa.criterios.TodasArestas;
import org.testeoa.criterios.TodasArestasDepExcecao;
import org.testeoa.criterios.TodasArestasIndepExcecao;
import org.testeoa.criterios.TodasArestasTransversais;
import org.testeoa.criterios.TodasDefinicoes;
import org.testeoa.criterios.TodosUsos;
import org.testeoa.criterios.TodosUsosDepExcecao;
import org.testeoa.criterios.TodosUsosIndepExcecao;
import org.testeoa.criterios.TodosUsosTransversais;
import org.testeoa.criterios.TodosVertices;
import org.testeoa.criterios.TodosVerticesDepExcecao;
import org.testeoa.criterios.TodosVerticesIndepExcecao;
import org.testeoa.criterios.TodosVerticesTransversais;
import org.testeoa.estatica.AnaliseEstatica;
import org.testeoa.excecoes.ExAnaliseEstatica;
import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;
import org.testeoa.modelo.Projeto;
import org.testeoa.teste.CasoTeste;
import org.testeoa.teste.TesteAdendoAdendo;
import org.testeoa.teste.TesteAdendoMetodo;
import org.testeoa.teste.TesteEstrutural;
import org.testeoa.teste.TesteInterMetodo;
import org.testeoa.teste.TesteInterMetodoAdendo;
import org.testeoa.teste.TesteIntraAdendo;
import org.testeoa.teste.TesteIntraMetodo;
import org.testeoa.teste.TesteMetodoAdendo;


/**
 * @author    Rafael
 */
public class DialogoInserirT extends JDialog {
	
	Projeto projeto;
	TesteEstrutural teste;
	
	JTextField tfNome;
	JRadioButton rbUni;
	JRadioButton rbMod;
	JComboBox cbTipo;
	JComboBox cbOwner;
	JComboBox cbUni;
	JList jlCrit;
	
	private final String T_MET = "Teste Intra-Método";	
	private final String T_AD = "Teste Intra-Adendo";	
	private final String T_MET_AD = "Teste Método-Adendo";
	private final String T_INT_MET = "Teste Inter-Método";
	private final String T_INT_MET_AD = "Teste Inter-Método-Adendo";
	private final String T_AD_AD = "Teste Adendo-Adendo";
	private final String T_AD_MET = "Teste Adendo-Método";
	
	private String[] testesUni = {
			T_MET, T_AD
	};	
	private String[] testesMod = {
			T_MET_AD, T_INT_MET, T_INT_MET_AD, T_AD_AD, T_AD_MET
	};
	
	/**
	 * @uml.property  name="criterios"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Criterio[] criterios = {
			new TodosVertices(),
			new TodosVerticesTransversais(),
			new TodosVerticesIndepExcecao(),
			new TodosVerticesDepExcecao(),
			new TodasArestas(),
			new TodasArestasTransversais(),
			new TodasArestasIndepExcecao(),
			new TodasArestasDepExcecao(),
			new TodasDefinicoes(),
			new TodosUsos(),
			new TodosUsosTransversais(),
			new TodosUsosIndepExcecao(),
			new TodosUsosDepExcecao()
			
	};
	
	public DialogoInserirT(Projeto p) {
		super(GUI.instancia, "Novo Teste", true);
		
		projeto = p;
		
		JPanel pn = new JPanel(new BorderLayout());
		add(pn, BorderLayout.NORTH);
		pn.setBorder(BorderFactory.createTitledBorder("Caso de Teste"));
		
		tfNome = new JTextField(25);
		pn.add(tfNome, BorderLayout.CENTER);
		
		JPanel pc = new JPanel(new BorderLayout());
		add(pc, BorderLayout.CENTER);
		
		JPanel pcn = new JPanel(new BorderLayout());
		pc.add(pcn, BorderLayout.NORTH);
		pcn.setBorder(BorderFactory.createTitledBorder("Tipo"));
		
		JPanel pcnn = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pcn.add(pcnn, BorderLayout.NORTH);
		ButtonGroup bg = new ButtonGroup();
		rbUni = new JRadioButton("Unidade");
		rbUni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selecionarUnidade();
			}
		});
		rbUni.setSelected(true);
		pcnn.add(rbUni);
		bg.add(rbUni);
		rbMod = new JRadioButton("Módulo");
		rbMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selecionarModulo();
			}
		});
		pcnn.add(rbMod);
		bg.add(rbMod);
		
		cbTipo = new JComboBox();
		cbTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mudarTipo();
			}
		});
		pcn.add(cbTipo, BorderLayout.SOUTH);
		
		JPanel pcc = new JPanel(new BorderLayout());
		pcc.setBorder(BorderFactory.createTitledBorder("Unidade Principal"));
		pc.add(pcc, BorderLayout.CENTER);
		cbOwner = new JComboBox();
		cbOwner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mudarOwner();
			}
		});
		pcc.add(cbOwner, BorderLayout.NORTH);		
		cbUni = new JComboBox();
		pcc.add(cbUni, BorderLayout.SOUTH);
		
		JPanel pcs = new JPanel(new BorderLayout());		
		add(pcs, BorderLayout.EAST);
		pcs.setBorder(
				BorderFactory.createTitledBorder("Critérios de Abrangência"));
		
		jlCrit = new JList(criterios);
		pcs.add(new JScrollPane(jlCrit));
		
		JPanel ps = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(ps, BorderLayout.SOUTH);
		JButton bConf = new JButton("Confirmar");
		bConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmar();				
			}
		});
		ps.add(bConf);
		JButton bCanc = new JButton("Cancelar");
		bCanc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelar();				
			}
		});
		ps.add(bCanc);
		
		//pack();
		setSize(new Dimension(500, 290));
		selecionarUnidade();
		mudarTipo();
		setLocationRelativeTo(getOwner());
	}
	
	public void executar() {
		setVisible(true);
	}
	
	private void selecionarUnidade() {
		cbTipo.setModel(new DefaultComboBoxModel(testesUni));
		mudarTipo();
	}
	
	private void selecionarModulo() {
		cbTipo.setModel(new DefaultComboBoxModel(testesMod));
		mudarTipo();
	}
	
	private void mudarTipo() {
		Object t = cbTipo.getSelectedItem();
		if (t.equals(T_MET) || t.equals(T_MET_AD) || t.equals(T_INT_MET) ||
				t.equals(T_INT_MET_AD)) {
			cbOwner.setModel(new DefaultComboBoxModel(projeto.getGeral()));
		}
		else {
			cbOwner.setModel(new DefaultComboBoxModel(projeto.getAspectos()));
		}
		mudarOwner();
	}
	
	private void mudarOwner() {
		Object o = cbOwner.getSelectedItem();
		if (o != null) {
			if (o instanceof Aspecto) {
				Aspecto a = (Aspecto) o;
				String t = (String) cbTipo.getSelectedItem();
				if (t.equals(T_MET) || t.equals(T_MET_AD) || t.equals(T_INT_MET)) {
					cbUni.setModel(new DefaultComboBoxModel(a.getMetodos()));
				}
				else {
					cbUni.setModel(new DefaultComboBoxModel(a.getAdendos()));
				}
			}
			else {
				Classe c = (Classe) o;
				cbUni.setModel(new DefaultComboBoxModel(c.getMetodos()));
			}
		}
		else {
			cbUni.setModel(new DefaultComboBoxModel());
		}
	}
	
	private void confirmar() {
		try {
			CasoTeste ct = AnaliseEstatica.lerCasoTeste(tfNome.getText());
			String t = (String) cbTipo.getSelectedItem();
			Object uni = cbUni.getSelectedItem();
			if (uni == null) {
				throw new ExAnaliseEstatica("Selecione uma unidade.", null);
			}
			if (t.equals(T_MET)) {
				Metodo m = (Metodo) uni;
				teste = new TesteIntraMetodo(ct, m);
			}
			if (t.equals(T_AD)) {
				Adendo a = (Adendo) uni;
				teste = new TesteIntraAdendo(ct, a);
			}
			if (t.equals(T_MET_AD)) {
				Metodo m = (Metodo) uni;
				teste = new TesteMetodoAdendo(ct, m);
			}
			if (t.equals(T_INT_MET)) {
				Metodo m = (Metodo) uni;
				teste = new TesteInterMetodo(ct, m);
			}
			if (t.equals(T_INT_MET_AD)) {
				Metodo m = (Metodo) uni;
				teste = new TesteInterMetodoAdendo(ct, m);
			}
			if (t.equals(T_AD_AD)) {
				teste = new TesteAdendoAdendo(ct, (Adendo)uni);
			}
			if (t.equals(T_AD_MET)) {
				teste = new TesteAdendoMetodo(ct, (Adendo)uni);
			}
			for (Object o : jlCrit.getSelectedValues()) {
				((TesteIntraMetodo)teste).inserir((Criterio)o);
			}
			dispose();
		}
		catch (ExAnaliseEstatica e) {
			GUI.instancia.exibirMensagemErro(e);
		}
	}
	
	private void cancelar() {
		dispose();
	}
	
	/**
	 * @return  the teste
	 * @uml.property  name="teste"
	 */
	public TesteEstrutural getTeste() {
		return teste;
	}

}
