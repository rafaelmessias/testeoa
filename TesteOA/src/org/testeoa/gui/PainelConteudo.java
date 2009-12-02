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

import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JDesktopPane;

import org.testeoa.modelo.Metodo;

public class PainelConteudo extends JDesktopPane {
	
	Set<ViewUnidade> grafoViews;

	public PainelConteudo() {
		super();
		grafoViews = new LinkedHashSet<ViewUnidade>();
	}
	
	public void novaFrame(ViewUnidade view) {
		grafoViews.add(view);
	}
	
	public void exibirView(Metodo un) {
		for (ViewUnidade v : grafoViews) {
			if (v.getUnidade().equals(un)) {
				v.setVisible(true);
				add(v);
			}
		}
	}
	
	public void removerView(Metodo un) {
		ViewUnidade vu = null;
		for (ViewUnidade v : grafoViews) {
			if (v.getUnidade().equals(un)) {
				v.setVisible(false);
				remove(v);
				vu = v;
			}
		}
		// evitar concurrent modification
		grafoViews.remove(vu);
	}
	
}
