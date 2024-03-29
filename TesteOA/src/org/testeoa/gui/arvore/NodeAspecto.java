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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.testeoa.gui.DialogoInserirMA;
import org.testeoa.gui.GUI;
import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Metodo;

public class NodeAspecto extends NodeAP {

	public NodeAspecto(ArvoreProjetos ap, Aspecto a) {
		super(ap);
		setUserObject(a);
		registrarPopup();
		for (Metodo m : a.getMetodos()) {
			inserirNodeMetodo(m);
		}
		for (Adendo ad : a.getAdendos()) {
			inserirNodeAdendo(ad);
		}
		icone = new ImageIcon("fig/a.png");
	}
	
	@Override
	public Aspecto getUserObject() {
		return (Aspecto) super.getUserObject();
	}
	
	void registrarPopup() {
		popup = new JPopupMenu();
		
		JMenuItem novaUnidade = new JMenuItem("Nova Unidade");
		novaUnidade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				novaUnidade();
			}
		});
		popup.add(novaUnidade);
		
		JMenuItem remover = new JMenuItem("Remover");
		remover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remover();
			}				
		});
		popup.add(remover);
	}
	
	public void novaUnidade() {
		DialogoInserirMA d = new DialogoInserirMA(getUserObject());
		d.executar();
		for (Metodo m : d.getMetodos()) {
			getUserObject().inserir(m);
			inserirNodeMetodo(m);
		}
		for (Adendo a : d.getAdendos()) {
			getUserObject().inserir(a);
			inserirNodeAdendo(a);
		}
		((NodeProjeto)getParent()).checarTestes();
	}
	
	@Override
	public void remover() {
		String title = "Confirma��o de Remo��o";
		String message = "Confirma remo��o do aspecto [" + 
			getUserObject().getNomeCurto() + "] ?";
		int oType = JOptionPane.OK_CANCEL_OPTION;
		int mType = JOptionPane.WARNING_MESSAGE;
		int conf = JOptionPane.showConfirmDialog(
				GUI.instancia, message, title, oType, mType);
		if (conf == JOptionPane.OK_OPTION) {
			// Modelo
			getUserObject().getProjeto().remover(getUserObject());
			((NodeProjeto)getParent()).checarTestes();
			// GUI
			arvore.getModel().removeNodeFromParent(this);
		}
		
	}
	
	private void inserirNodeMetodo(Metodo m) {
		arvore.getModel().insertNodeInto(
				new NodeMetodo(arvore, m),
				this,
				getChildCount());
	}
	
	private void inserirNodeAdendo(Adendo ad) {
		arvore.getModel().insertNodeInto(
				new NodeAdendo(arvore, ad),
				this,
				getChildCount());
	}

}
