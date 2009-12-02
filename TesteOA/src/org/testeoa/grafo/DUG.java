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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.objectweb.asm.Label;
import org.testeoa.modelo.Metodo;

/**
 * @author    Rafael
 */
public class DUG {
	
	// todos os vértices
	List<Vertice> vertices = new ArrayList<Vertice>();
	
	Vertice entrada;
	
	// vértices de saída
	Set<Vertice> saidas;
	
	// todas as arestas
	List<Aresta> arestas;
	
	Metodo unidade;
	
	public DUG() {		
		saidas = new HashSet<Vertice>();
		arestas = new ArrayList<Aresta>();
	}
	
	// operações com vértices
	
	// inserção de um vértice
	public void inserir(Vertice vertice) {
		if (!vertices.contains(vertice)) {
			vertices.add(vertice);			
			vertice.numero = String.valueOf(vertices.size());			
		}		
	}
	
	// retorna os vértices
	/**
	 * @return  the vertices
	 * @uml.property  name="vertices"
	 */
	public Vertice[] getVertices() {
		return vertices.toArray(new Vertice[0]);
	}
	
	// retorna um determinado vértice a partir de seu label
	public Vertice getVertice(Label label) {
		for (Vertice v : vertices) {
			if (v.getLabel().equals(label)) {
				return v;
			}
		}
		return null;
	}
	
	// retorna um determinado vértice a partir da string do label
	public Vertice getVertice(String label) {
		for (Vertice v : vertices) {
			if (v.getLabel().toString().equals(label)) {
				return v;
			}
		}
		return null;
	}
	
	// remove um vértice do grafo
	// --- retornar uma excecao se o vertice não está no grafo??
	public void remover(Vertice v) {
		Vector<Aresta> remover = new Vector<Aresta>();
		for (Aresta a : arestas) {
			if (a.getOrigem().equals(v) || a.getDestino().equals(v)) {
				remover.add(a);
			}
		}
		for (Aresta a : remover) {
			remover(a);
		}
		vertices.remove(v);
		renumerar();
	}
	
	void renumerar() {
		for (Vertice v : vertices) {
			v.numero = String.valueOf(vertices.indexOf(v) + 1);
		}
	}
	
	// define v como vértice de entrada
	/**
	 * @param entrada  the entrada to set
	 * @uml.property  name="entrada"
	 */
	public void setEntrada(Vertice v) {
		entrada = v;
	}
	
	// verifica se v é o vértice de entrada
	public boolean isEntrada(Vertice v) {
		return (v.equals(entrada));
	}
	
	// define o vértice como vértice de saída
	// lançar uma exceção se o vértice não está no grafo?
	public void setSaida(Vertice saida) {
		saidas.add(saida);
	}
	
	// verifica se v é um vértice de saída
	public boolean isSaida(Vertice v) {
		return (saidas.contains(v));
	}	
	
	// operações com arestas
	
	// inserção de uma aresta
	public void inserir(Aresta aresta) {
		arestas.add(aresta);
	}
	
	// retorna todas as arestas
	/**
	 * @return  the arestas
	 * @uml.property  name="arestas"
	 */
	public Aresta[] getArestas() {
		return arestas.toArray(new Aresta[0]);
	}
	
	// retorna as saídas de um determinado vértice
	public Aresta[] getSaidas(Vertice vertice) {
		Vector<Aresta> saidas = new Vector<Aresta>();
		for (Aresta a : arestas) {
			if (a.getOrigem().equals(vertice)) {
				saidas.add(a);
			}
		}
		return saidas.toArray(new Aresta[0]);
	}
	
	// retorna as entradas de um determinado vértice
	public Aresta[] getEntradas(Vertice vertice) {
		Vector<Aresta> entradas = new Vector<Aresta>();
		for (Aresta a : arestas) {
			if (a.getDestino().equals(vertice)) {
				entradas.add(a);
			}
		}
		return entradas.toArray(new Aresta[0]);
	}
	
	// recupera uma aresta específica
	public Aresta getAresta(Vertice origem, Vertice destino) {
		for (Aresta a : arestas) {
			if (a.getOrigem().equals(origem) && a.getDestino().equals(destino)) {
				return a;
			}
		}
		return null;
	}
	
	// remove uma aresta
	public void remover(Aresta a) {
		arestas.remove(a);
	}
	
	/**
	 * @return  the unidade
	 * @uml.property  name="unidade"
	 */
	public Metodo getUnidade() {
		return unidade;
	}
	
	/**
	 * @param unidade  the unidade to set
	 * @uml.property  name="unidade"
	 */
	public void setUnidade(Metodo unidade) {
		this.unidade = unidade;
	}

}
