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

package org.testeoa.gui.arvore;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import org.testeoa.excecoes.ExProjeto;
import org.testeoa.excecoes.ExcecaoExportarProjeto;
import org.testeoa.gui.DialogoInserirA;
import org.testeoa.gui.DialogoInserirC;
import org.testeoa.gui.DialogoInserirT;
import org.testeoa.gui.GUI;
import org.testeoa.modelo.Ambiente;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Projeto;
import org.testeoa.teste.TesteEstrutural;
import org.testeoa.teste.TesteInterMetodoAdendo;
import org.testeoa.teste.TesteIntraMetodo;

public class NodeProjeto extends NodeAP {
	
	JMenuItem abrir;
	JMenuItem fechar;
	JMenuItem novaClasse;
	JMenuItem novoAspecto;
	JMenuItem novoTeste;
	
	public NodeProjeto(ArvoreProjetos a, Projeto p) {
		super(a);
		setUserObject(p);
		setAllowsChildren(true);
		registrarPopup();
		icone = new ImageIcon("fig/pf.png");
	}
	
	@Override
	public Projeto getUserObject() {
		return (Projeto) super.getUserObject();
	}
	
	void registrarPopup() {
		popup = new JPopupMenu();
		
		abrir = new JMenuItem("Abrir");
		abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrir();
			}
		});
		popup.add(abrir);
		
		fechar = new JMenuItem("Fechar");
		fechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fechar();				
			}
		});
		popup.add(fechar);
		
		popup.add(new JSeparator());		
		
		novaClasse = new JMenuItem("Nova Classe");
		novaClasse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				novaClasse();
			}
		});
		popup.add(novaClasse);
		
		novoAspecto = new JMenuItem("Novo Aspecto");
		novoAspecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				novoAspecto();				
			}
		});
		popup.add(novoAspecto);
		
		novoTeste = new JMenuItem("Novo Teste");
		novoTeste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				novoTeste();
			}
		});
		popup.add(novoTeste);
		
		popup.add(new JSeparator());
		
		JMenuItem exportar = new JMenuItem("Exportar");
		exportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportar();
			}
		});
		popup.add(exportar);
		
		JMenuItem remover = new JMenuItem("Remover");
		remover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remover();
			}				
		});
		popup.add(remover);		
	}
	
	@Override
	public void exibirPopup(Point p) {
		if (getUserObject().isAberto()) {
			abrir.setEnabled(false);
			fechar.setEnabled(true);
			novaClasse.setEnabled(true);
			novoAspecto.setEnabled(true);
			novoTeste.setEnabled(true);
		}
		else {
			abrir.setEnabled(true);
			fechar.setEnabled(false);
			novaClasse.setEnabled(false);
			novoAspecto.setEnabled(false);
			novoTeste.setEnabled(false);
		}
		super.exibirPopup(p);
	}
	
	public void abrir() {
		icone = new ImageIcon("fig/pa.png");
		// Modelo
		getUserObject().abrir();
		// GUI
		for (Classe c : getUserObject().getClasses()) {
			inserirNodeClasse(c);
		}
		for (Aspecto a : getUserObject().getAspectos()) {
			inserirNodeAspecto(a);
		}
		for (TesteEstrutural t : getUserObject().getTestes()) {
			inserirNodeTeste(t);
		}		
		expandir();
		GUI.instancia.atualizarBarra(getUserObject());
		arvore.repaint();
	}
	
	public void fechar() {
		// Modelo
		getUserObject().fechar();
		// GUI
		fecharGUI();
		GUI.instancia.atualizarBarra(getUserObject());
	}
	
	@Override
	public void remover() {
		String title = "Confirmação de Remoção";
		String message = "Confirma remoção do projeto [" + 
			getUserObject().getNome() + "] ?";
		int oType = JOptionPane.OK_CANCEL_OPTION;
		int mType = JOptionPane.WARNING_MESSAGE;
		int conf = JOptionPane.showConfirmDialog(
				GUI.instancia, message, title, oType, mType);
		if (conf == JOptionPane.OK_OPTION) {
			Ambiente.removerProjeto(getUserObject());
			removerGUI();
		}		
	}
	
	public void novaClasse() {
		// não deixar inserir duas vezes a mesma classe!!!
		DialogoInserirC d = new DialogoInserirC();
		d.executar();
		if (d.getClasse() != null) {
			Classe c = d.getClasse();			
			try {
				// Modelo
				getUserObject().inserir(c);
				// GUI
				inserirNodeClasse(c);
			}
			catch(ExProjeto e) {
				GUI.instancia.exibirMensagemErro(e);
			}
		}
		checarTestes();
		expandir();
	}
	
	public void novoAspecto() {
		// não deixar inserir duas vezes o mesmo aspecto!
		DialogoInserirA d = new DialogoInserirA();
		d.executar();
		if (d.getAspecto() != null) {
			try {
				Aspecto a = d.getAspecto();
				// Modelo
				getUserObject().inserir(a);
				// GUI
				inserirNodeAspecto(a);
			}
			catch (ExProjeto e) {
				GUI.instancia.exibirMensagemErro(e);
			}
		}
		checarTestes();
		expandir();
	}
	
	public void novoTeste() {
		DialogoInserirT d = new DialogoInserirT(getUserObject());
		d.executar();
		if (d.getTeste() != null) {
			TesteEstrutural t = d.getTeste();
			// Modelo
			if (t instanceof TesteInterMetodoAdendo) {
				getUserObject().inserir((TesteInterMetodoAdendo)t);
			}
			else {
				getUserObject().inserir((TesteIntraMetodo)t);
			}
			// GUI
			inserirNodeTeste(t);
		}
		expandir();
	}
	
	public void exportar() {
		JFileChooser jfc = new JFileChooser(".");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle("Selecione o diretório destino");
		int estado = jfc.showDialog(GUI.instancia, "Selecionar");
		if (estado == JFileChooser.APPROVE_OPTION) {
			try {
				Ambiente.docXML.exportarProjeto(
						getUserObject(),
						jfc.getSelectedFile().getAbsolutePath(),
						true);
			}
			catch (ExcecaoExportarProjeto e) {
				GUI.instancia.exibirMensagemErro(e);
			}			
		}
	}
	
	public void inserirNodeClasse(Classe c) {
		int i = 0;
		for(; i < getChildCount(); i++) {
			if (getChildAt(i) instanceof NodeAspecto) {				
				break;
			}			
			if (getChildAt(i) instanceof NodeTeste) {
				break;
			}
		}
		arvore.getModel().insertNodeInto(
				new NodeClasse(arvore, c),
				this,
				i);
	}
	
	public void inserirNodeAspecto(Aspecto a) {
		int i = 0;
		for(; i < getChildCount(); i++) {
			if (getChildAt(i) instanceof NodeTeste) {				
				break;
			}			
		}
		arvore.getModel().insertNodeInto(
				new NodeAspecto(arvore, a),
				this,
				i);
	}
	
	private void inserirNodeTeste(TesteEstrutural t) {
		arvore.getModel().insertNodeInto(
				new NodeTeste(arvore, t),
				this,
				getChildCount());
	}
	
	private void fecharGUI() {
		while(getChildCount() > 0) {
			((NodeAP)getChildAt(0)).removerGUI();
		}
		icone = new ImageIcon("fig/pf.png");
	}
	
	void checarTestes() {
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i) instanceof NodeTeste) {
				((NodeTeste)getChildAt(i)).checarAtivo();
			}
		}
	}
	
}
