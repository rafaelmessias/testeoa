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

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;



/**
 * @author  Rafael
 */
public class AODU extends DUGE {	
	
	// vértices transversais
	Set<VerticeTransversal> verticesTransversais;
	
	// arestas transversais
	Set<Aresta> arestasTransversais;
	
	public AODU() {
		verticesTransversais = new HashSet<VerticeTransversal>();		
		arestasTransversais = new HashSet<Aresta>();
	}
	
	// operações com vértices
	
	// inserir um vértice transversal
	public void inserir(VerticeTransversal vt) {
		verticesTransversais.add(vt);
		super.inserir(vt);
	}
	
	// retorna os vértices transversais
	/**
	 * @return  the verticesTransversais
	 * @uml.property  name="verticesTransversais"
	 */
	public VerticeTransversal[] getVerticesTransversais() {
		return verticesTransversais.toArray(new VerticeTransversal[0]);
	}
	
	// remove um vértice transversal
	public void remover(VerticeTransversal vt) {
		verticesTransversais.remove(vt);
		remover((Vertice)vt);
	}
	
	// remove um vértice comum
	@Override
	public void remover(Vertice v) {
		if (v instanceof VerticeTransversal) {
			remover((VerticeTransversal)v);
			return;
		}
		Vector<Aresta> remover = new Vector<Aresta>();
		for (Aresta a : arestasTransversais) {
			if (a.getOrigem().equals(v) || a.getDestino().equals(v)) {
				remover.add(a);
			}
		}
		for (Aresta a : remover) {
			remover(a);
		}
		super.remover(v);
	}
	
	// operações com arestas
	
	// insere uma aresta (possivelmente transversal)
	public void inserir(Aresta aresta) {
		if (isTransversal(aresta)) {
			arestasTransversais.add(aresta);
		}
		super.inserir(aresta);
	}
	
	// insere uma aresta de exceção (possivelmente transversal)
	public void inserir(ArestaExcecao aresta) {
		if (isTransversal(aresta)) {
			arestasTransversais.add(aresta);
		}
		super.inserir(aresta);
	}
	
	// retorna arestas transversais
	/**
	 * @return  the arestasTransversais
	 * @uml.property  name="arestasTransversais"
	 */
	public Aresta[] getArestasTransversais() {
		return arestasTransversais.toArray(new Aresta[0]);
	}
	
	// retorna as saídas transversais de um determinado vértice
	public Aresta[] getSaidasTransversais(Vertice vertice) {
		Vector<Aresta> saidas = new Vector<Aresta>();
		for (Aresta a : arestasTransversais) {
			if (a.getOrigem().equals(vertice)) {
				saidas.add(a);
			}
		}
		return saidas.toArray(new Aresta[0]);
	}
	
	// retorna as saídas transversais de um determinado vértice
	public Aresta[] getEntradasTransversais(Vertice vertice) {
		Vector<Aresta> entradas = new Vector<Aresta>();
		for (Aresta a : arestasTransversais) {
			if (a.getDestino().equals(vertice)) {
				entradas.add(a);
			}
		}
		return entradas.toArray(new Aresta[0]);
	}
	
	// remove uma aresta (possivelmente transversal)
	public void remover(Aresta aresta) {
		if (isTransversal(aresta)) {
			arestasTransversais.remove(aresta);
		}
		super.remover(aresta);
	}
	
	// remove uma aresta de exceção (possivelmente transversal)
	public void remover(ArestaExcecao aresta) {
		if (isTransversal(aresta)) {
			arestasTransversais.remove(aresta);
		}
		super.remover(aresta);
	}
	
	// verifica se uma aresta é transversal
	public boolean isTransversal(Aresta aresta) {
		return (aresta.getDestino() instanceof VerticeTransversal);
	}
	
	public boolean isTransversal(Vertice vertice) {
		return (vertice instanceof VerticeTransversal);
	}
	
}
