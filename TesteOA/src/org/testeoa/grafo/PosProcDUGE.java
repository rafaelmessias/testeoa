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

package org.testeoa.grafo;

import java.util.Vector;


/**
 * @author    Rafael
 */
public class PosProcDUGE extends PosProcDUG {
	
	private DUGE grafo;
	
	public PosProcDUGE(DUGE grafo) {
		super(grafo);
		this.grafo = grafo;
	}
	
	@Override
	boolean isCandidato(Vertice v) {		
		return (grafo.getSaidasRegulares(v).length == 1);
	}
	
	@Override
	Vertice getProximo(Vertice v) {
		return grafo.getSaidasRegulares(v)[0].getDestino();
	}
	
	@Override
	boolean compativeis(Vertice v1, Vertice v2) {
		if (grafo.getEntradasRegulares(v2).length == 1) {
			Vector<String> e1 = new Vector<String>();
			for (ArestaExcecao a : grafo.getSaidasExcecao(v1)) {
				e1.add(a.getExcecao());
			}
			Vector<String> e2 = new Vector<String>();
			for (ArestaExcecao a : grafo.getSaidasExcecao(v2)) {
				e2.add(a.getExcecao());
			}
			return e1.equals(e2);
		}
		return false;
	}

}
