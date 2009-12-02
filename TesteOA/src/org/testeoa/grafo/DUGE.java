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
public class DUGE extends DUG {
	
	// arestas regulares
	Set<Aresta> arestasRegulares;
	
	// arestas de exceção
	Set<ArestaExcecao> arestasExcecao;
	
	// vértices dependentes de exceção
	Set<Vertice> verticesDependentesExcecao;
	
	Set<Vertice> verticesIndependentesExcecao;
	
	Set<Aresta> arestasDependentesExcecao;
	
	Set<Aresta> arestasIndependentesExcecao;
	
	public DUGE() {
		arestasRegulares = new HashSet<Aresta>();
		arestasExcecao = new HashSet<ArestaExcecao>();
		verticesDependentesExcecao = new HashSet<Vertice>();
		verticesIndependentesExcecao = new HashSet<Vertice>();
		arestasDependentesExcecao = new HashSet<Aresta>();
		arestasIndependentesExcecao = new HashSet<Aresta>();
	}
	
	@Override
	public void setEntrada(Vertice v) {
		super.setEntrada(v);
		setIndependente(v);
	}
	
	// operações com vértices
	@Override
	public void inserir(Vertice vertice) {
		super.inserir(vertice);
		verticesDependentesExcecao.add(vertice);
	}
	
	// remove um vértice e todas as arestas relacionadas
	@Override
	public void remover(Vertice v) {
		Vector<Aresta> remover = new Vector<Aresta>();
		for (Aresta a : arestasRegulares) {
			if (a.getOrigem().equals(v) || a.getDestino().equals(v)) {
				remover.add(a);
			}
		}
		for (Aresta a : arestasExcecao) {
			if (a.getOrigem().equals(v) || a.getDestino().equals(v)) {
				remover.add(a);
			}
		}
		for (Aresta a : remover) {
			remover(a);
		}
		verticesDependentesExcecao.remove(v);
		verticesIndependentesExcecao.remove(v);
		super.remover(v);
	}
	
	// operações com arestas
	
	// insere uma aresta regular
	public void inserir(Aresta aresta) {
		
		arestasRegulares.add(aresta);
		
		super.inserir(aresta);
		if (isIndependenteExcecao(aresta.getOrigem())) {
			
			arestasIndependentesExcecao.add(aresta);
			setIndependente(aresta.getDestino());
		}
		else {
			arestasDependentesExcecao.add(aresta);
		}
		
	}
	
	// insere uma aresta de exceção
	public void inserir(ArestaExcecao aresta) {
		arestasExcecao.add(aresta);
		arestasDependentesExcecao.add(aresta);
		super.inserir(aresta);
		//setDependente(aresta.getDestino());
	}
	
	// retorna todas as arestas regulares
	/**
	 * @return  the arestasRegulares
	 * @uml.property  name="arestasRegulares"
	 */
	public Aresta[] getArestasRegulares() {
		return arestasRegulares.toArray(new Aresta[0]);
	}
	
	// retorna todas as arestas de exceção
	/**
	 * @return  the arestasExcecao
	 * @uml.property  name="arestasExcecao"
	 */
	public ArestaExcecao[] getArestasExcecao() {
		return arestasExcecao.toArray(new ArestaExcecao[0]);
	}
	
	// retorna as saídas regulares de um determinado vértice
	public Aresta[] getSaidasRegulares(Vertice vertice) {
		Vector<Aresta> saidas = new Vector<Aresta>();
		for (Aresta a : arestasRegulares) {
			if (a.getOrigem().equals(vertice)) {
				saidas.add(a);
			}
		}
		return saidas.toArray(new Aresta[0]);
	}
	
	// retorna as saídas de exceção de um determinado vértice
	public ArestaExcecao[] getSaidasExcecao(Vertice vertice) {
		Vector<ArestaExcecao> saidas = new Vector<ArestaExcecao>();
		for (ArestaExcecao a : arestasExcecao) {
			if (a.getOrigem().equals(vertice)) {
				saidas.add(a);
			}
		}
		return saidas.toArray(new ArestaExcecao[0]);
	}
	
	// retorna as entradas regulares de um determinado vértice
	public Aresta[] getEntradasRegulares(Vertice vertice) {
		Vector<Aresta> entradas = new Vector<Aresta>();
		for (Aresta a : arestasRegulares) {
			if (a.getDestino().equals(vertice)) {
				entradas.add(a);
			}
		}
		return entradas.toArray(new Aresta[0]);
	}
	
	// retorna as entradas de exceção de um determinado vértice
	public ArestaExcecao[] getEntradasExcecao(Vertice vertice) {
		Vector<ArestaExcecao> entradas = new Vector<ArestaExcecao>();
		for (ArestaExcecao a : arestasExcecao) {
			if (a.getDestino().equals(vertice)) {
				entradas.add(a);
			}
		}
		return entradas.toArray(new ArestaExcecao[0]);
	}
	
	// remove uma aresta regular
	public void remover(Aresta aresta) {
		arestasRegulares.remove(aresta);
		super.remover(aresta);
		arestasDependentesExcecao.remove(aresta);
		arestasIndependentesExcecao.remove(aresta);
	}
	
	// remove uma aresta de exceção
	void remover(ArestaExcecao aresta) {
		arestasExcecao.remove(aresta);
		super.remover(aresta);
		arestasDependentesExcecao.remove(aresta);
	}
	
	public boolean isDependenteExcecao(Vertice v) {
		return verticesDependentesExcecao.contains(v);
	}
	
	public boolean isIndependenteExcecao(Vertice v) {
		return verticesIndependentesExcecao.contains(v);
	}
	
	public boolean isDependenteExcecao(Aresta a) {
		return arestasDependentesExcecao.contains(a);
	}
	
	public boolean isIndependenteExcecao(Aresta a) {
		return arestasIndependentesExcecao.contains(a);
	}
	
	private void setIndependente(Vertice v) {
		
		verticesDependentesExcecao.remove(v);
		verticesIndependentesExcecao.add(v);		
		for (Aresta a : getSaidasRegulares(v)) {
			arestasDependentesExcecao.remove(a);
			arestasIndependentesExcecao.add(a);
			if (!isIndependenteExcecao(a.getDestino())) {
				setIndependente(a.getDestino());
			}
		}
	}
	
//	private void setDependente(Vertice v) {
//		if (!isIndependenteExcecao(v) && !isDependenteExcecao(v)) {
//			verticesDependentesExcecao.add(v);
//			for (Aresta a : getSaidasRegulares(v)) {
//				arestasDependentesExcecao.add(a);
//				setDependente(a.getDestino());
//			}
//		}
//	}

	/**
	 * @return  the arestasDependentesExcecao
	 * @uml.property  name="arestasDependentesExcecao"
	 */
	public Aresta[] getArestasDependentesExcecao() {
		return arestasDependentesExcecao.toArray(new Aresta[0]);
	}

	/**
	 * @return  the arestasIndependentesExcecao
	 * @uml.property  name="arestasIndependentesExcecao"
	 */
	public Aresta[] getArestasIndependentesExcecao() {
		return arestasIndependentesExcecao.toArray(new Aresta[0]);
	}

	/**
	 * @return  the verticesDependentesExcecao
	 * @uml.property  name="verticesDependentesExcecao"
	 */
	public Vertice[] getVerticesDependentesExcecao() {
		return verticesDependentesExcecao.toArray(new Vertice[0]);
	}

	/**
	 * @return  the verticesIndependentesExcecao
	 * @uml.property  name="verticesIndependentesExcecao"
	 */
	public Vertice[] getVerticesIndependentesExcecao() {
		return verticesIndependentesExcecao.toArray(new Vertice[0]);
	}
	
	

}
