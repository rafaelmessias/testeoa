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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.testeoa.excecoes.ExImportarProjeto;
import org.testeoa.gui.GUI;
import org.testeoa.modelo.Ambiente;
import org.testeoa.modelo.Projeto;

public class ArvoreProjetos extends JTree {
	
	// Menus popup
	JPopupMenu popup;

	public ArvoreProjetos() {
		super();
		setRootVisible(false);
		iniciarModel();
		registrarPopup();
		setCellRenderer(new RendererAP());
		ToolTipManager.sharedInstance().registerComponent(this);
		
		addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent arg0) {
				if (arg0.getNewLeadSelectionPath() == null) {
					GUI.instancia.atualizarBarra(null);
				}
				else {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
						arg0.getNewLeadSelectionPath().getLastPathComponent();
					GUI.instancia.atualizarBarra(node.getUserObject());					
				}
			}
		});
	}
	
	private void iniciarModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		for (Projeto p : Ambiente.getProjetos()) {
			root.add(new NodeProjeto(this, p));
		}
		setModel(new DefaultTreeModel(root));
	}
	
	private void registrarPopup() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					exibirPopup(e.getPoint());
				}
				if (e.getClickCount() == 2) {
					NodeAP sel = getSelecionado();
					if (sel instanceof NodeTeste) {
						((NodeTeste)sel).visualizar();
					}
					else if (sel instanceof NodeMetodo) {
						((NodeMetodo)sel).visualizar();
					}
					else if (sel instanceof NodeAdendo) {
						((NodeAdendo)sel).visualizar();
					}
				}
			}			
		});
		
		popup = new JPopupMenu();
		
		JMenuItem novoProjeto = new JMenuItem("Novo Projeto");
		novoProjeto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				novoProjeto();
			}
		});
		popup.add(novoProjeto);
		
		JMenuItem importarProjeto = new JMenuItem("Importar Projeto");
		novoProjeto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importarProjeto();
			}
		});
		popup.add(importarProjeto);
	}
	
	public void novoProjeto() {
		String title = "Novo Projeto";
		int mType = JOptionPane.QUESTION_MESSAGE;
		String nome = JOptionPane.showInputDialog(
				GUI.instancia, "Nome do Projeto: ", title, mType);
		if (nome != null) {
			// Modelo
			Projeto p = new Projeto(nome);
			Ambiente.inserirProjeto(p);
			// GUI
			NodeProjeto np = new NodeProjeto(this, p);
			MutableTreeNode root = (MutableTreeNode)getModel().getRoot();
			getModel().insertNodeInto(np, root, root.getChildCount());
			// Bug?! o primeiro projeto não aparece na tela...
			if (root.getChildCount() == 1) {
				getModel().setRoot(root);
			}
		}		
	}
	
	public void importarProjeto() {
		JFileChooser jfc = new JFileChooser(".");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setDialogTitle("Selecione o arquivo XML do projeto");
		jfc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".xml")) {
					return true;
				}
				return false;
			}
			@Override
			public String getDescription() {				
				return "Arquivos XML";
			}
		});
		int estado = jfc.showDialog(GUI.instancia, "Selecionar");
		if (estado == JFileChooser.APPROVE_OPTION) {
			try {
				Ambiente.docXML.importarProjeto(jfc.getSelectedFile());
			}
			catch (ExImportarProjeto e) {
				GUI.instancia.exibirMensagemErro(e);
			}			
		}
	}
	
	private NodeAP selecionar(Point p) {
		TreePath path = getPathForLocation(p.x, p.y);
		if (path != null) {
			setSelectionPath(path);
			return (NodeAP) path.getLastPathComponent();
		}
		else {
			return null;
		}
	}
	
	public NodeAP getSelecionado() {
		if (getSelectionPath() == null) {
			return null;
		}
		return (NodeAP) getSelectionPath().getLastPathComponent();
	}

	@Override
	public DefaultTreeModel getModel() {
		return (DefaultTreeModel) super.getModel();
	} 

	private void exibirPopup(Point p) {
		NodeAP n = selecionar(p);
		if (n == null) {
			popup.show(this, p.x, p.y);
		}
		else {
			n.exibirPopup(p);
		}
	}
	
	private class RendererAP extends DefaultTreeCellRenderer {
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row, 
				boolean hasFocus) {
			
			if (value instanceof NodeAP) {
				NodeAP n = (NodeAP) value;
				setOpenIcon(n.getIcone());
				setClosedIcon(n.getIcone());
				setLeafIcon(n.getIcone());
			}
			
			return super.getTreeCellRendererComponent(tree, value, sel, 
					expanded, leaf,	row, hasFocus);
		}
		
	}

}
