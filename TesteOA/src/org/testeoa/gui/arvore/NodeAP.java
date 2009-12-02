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

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author    Rafael
 */
public abstract class NodeAP extends DefaultMutableTreeNode {
	
	ArvoreProjetos arvore;
	
	JPopupMenu popup = new JPopupMenu();
	
	/**
	 * @uml.property  name="icone"
	 */
	Icon icone;
	
	public NodeAP(ArvoreProjetos a) {
		arvore = a;
	}
	
	public void exibirPopup(Point p) {
		popup.show(arvore, p.x, p.y);
	}
	
	/**
	 * @return  the icone
	 * @uml.property  name="icone"
	 */
	public Icon getIcone() {
		return icone;
	}
	
	public void expandir() {
		arvore.expandPath(new TreePath(getPath()));
	}
	
	public abstract void remover();
	
	void removerGUI() {
		while(getChildCount() > 0) {
			((NodeAP)getChildAt(0)).removerGUI();
		}
		arvore.getModel().removeNodeFromParent(this);
	}

}
