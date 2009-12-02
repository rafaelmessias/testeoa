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

import org.testeoa.gui.GUI;
import org.testeoa.gui.ViewUnidade;
import org.testeoa.modelo.Adendo;

/**
 * @author    Rafael
 */
public class NodeAdendo extends NodeAP {
	
	ViewUnidade view;

	public NodeAdendo(ArvoreProjetos ap, Adendo a) {
		super(ap);
		setUserObject(a);
		view = new ViewUnidade(a);
		registrarPopup();
		icone = new ImageIcon("fig/ad.png");
	}
	
	@Override
	public Adendo getUserObject() {
		return (Adendo) super.getUserObject();
	}
	
	void registrarPopup() {
		popup = new JPopupMenu();
		
		JMenuItem visualizar = new JMenuItem("Visualizar");
		visualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizar();
			}
		});
		popup.add(visualizar);
		
		JMenuItem remover = new JMenuItem("Remover");
		remover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remover();
			}				
		});
		popup.add(remover);
	}
	
	public void visualizar() {
		if (GUI.conteudo.getPosition(view) == -1) {
			GUI.conteudo.add(view);
		}				
		view.setVisible(true);
		GUI.conteudo.setPosition(view, 0);
	}
	
	public void remover() {
		String title = "Confirmação de Remoção";
		String message = "Confirma remoção do adendo [" + 
			getUserObject() + "] ?";
		int oType = JOptionPane.OK_CANCEL_OPTION;
		int mType = JOptionPane.WARNING_MESSAGE;
		int conf = JOptionPane.showConfirmDialog(
				GUI.instancia, message, title, oType, mType);
		if (conf == JOptionPane.OK_OPTION) {
			getUserObject().getClasse().remover(getUserObject());
			((NodeProjeto)getParent().getParent()).checarTestes();
			removerGUI();
		}		
	}
	
	@Override
	void removerGUI() {
		super.removerGUI();
		view.setVisible(false);
		GUI.conteudo.remove(view);
	}
	
}
