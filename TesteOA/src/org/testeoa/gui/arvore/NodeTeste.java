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
import org.testeoa.gui.ViewTeste;
import org.testeoa.teste.TesteEstrutural;
import org.testeoa.teste.TesteIntraMetodo;

/**
 * @author    Rafael
 */
public class NodeTeste extends NodeAP {
	
	ViewTeste vt;
	
	JMenuItem visualizar;
	JMenuItem executar;

	public NodeTeste(ArvoreProjetos a, TesteEstrutural t) {
		super(a);
		setUserObject(t);
		registrarPopup();
		vt = new ViewTeste((TesteIntraMetodo)t);
		checarAtivo();
	}
	
	@Override
	public TesteEstrutural getUserObject() {
		return (TesteEstrutural) super.getUserObject();
	}
	
	void registrarPopup() {
		popup = new JPopupMenu();
		
		visualizar = new JMenuItem("Visualizar");
		visualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizar();
			}				
		});
		popup.add(visualizar);
		
		executar = new JMenuItem("Executar");
		executar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executar();
				
			}				
		});
		popup.add(executar);
		
		JMenuItem remover = new JMenuItem("Remover");
		remover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remover();
			}				
		});
		popup.add(remover);
	}
	
	public void visualizar() {
		if (GUI.conteudo.getPosition(vt) == -1) {
			GUI.conteudo.add(vt);			
		}		
		vt.setVisible(true);
		GUI.conteudo.setPosition(vt, 0);
	}
	
	public void executar() {
		getUserObject().executarTeste();
		vt.atualizar();
		checarAtivo();
		arvore.repaint();
		visualizar();
		vt.tabResultado();
	}
	
	@Override
	public void remover() {
		String title = "Confirmação de Remoção";
		String message = "Confirma remoção do teste [" + 
			getUserObject() + "] ?";
		int oType = JOptionPane.OK_CANCEL_OPTION;
		int mType = JOptionPane.WARNING_MESSAGE;
		int conf = JOptionPane.showConfirmDialog(
				GUI.instancia, message, title, oType, mType);
		if (conf == JOptionPane.OK_OPTION) {
			// Modelo
			getUserObject().getProjeto().remover(getUserObject());
			removerGUI();
			vt.setVisible(false);
			GUI.conteudo.remove(vt);
		}	
	}
	
	void checarAtivo() {
		if (!getUserObject().isAtivo()) {
			icone = new ImageIcon("fig/te.png");
			executar.setEnabled(false);
			vt.atualizar();
		}
		else {
			if (!getUserObject().isExecutado()) {
				icone = new ImageIcon("fig/tw.png");
				vt.redesenharGrafo();
				vt.atualizar();
			}
			else {
				icone = new ImageIcon("fig/t.png");
			}
			executar.setEnabled(true);
		}
		arvore.revalidate();
	}
	
	@Override
	void removerGUI() {
		super.removerGUI();
		vt.setVisible(false);
		GUI.conteudo.remove(vt);
	}

}
