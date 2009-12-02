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




/**
 * @author    Rafael
 */
public class PosProcDUG {
	
	private DUG grafo;
	
	public PosProcDUG(DUG grafo) {
		this.grafo = grafo;
	}
	
	public void otimizar() {
		for (Vertice v1 : grafo.getVertices()) {
			if (isInacancavel(v1)) {
				grafo.remover(v1);
			}
			else if (isCandidato(v1)) {
				Vertice v2 = getProximo(v1);
				if (compativeis(v1, v2)) {
					fundir(v1, v2);
				}
			}			
		}
	}
	
	boolean isInacancavel(Vertice v) {
		if (!grafo.isEntrada(v)) {
			if (grafo.getEntradas(v).length == 0) {
				return true;
			}
		}
		return false;
	}
	
	boolean isCandidato(Vertice v) {
		return (grafo.getSaidas(v).length == 1);
	}
	
	Vertice getProximo(Vertice v) {
		return grafo.getSaidas(v)[0].getDestino();
	}
	
	boolean compativeis(Vertice v1, Vertice v2) {
		return (grafo.getEntradas(v2).length == 1);
	}
	
	void fundir(Vertice v1, Vertice v2) {
		v2.instrucoes.addAll(0, v1.instrucoes);
		v2.setLabel(v1.getLabel());
		for (Aresta a : grafo.getEntradas(v1)) {
			a.setDestino(v2);
		}
		if (grafo.isEntrada(v1)) {
			grafo.setEntrada(v2);
		}
		v2.definicoes.addAll(v1.definicoes);
		v2.usos.addAll(v1.usos);
		grafo.remover(v1);
	}

}
