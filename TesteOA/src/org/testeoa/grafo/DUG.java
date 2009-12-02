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
	
	// todos os v�rtices
	List<Vertice> vertices = new ArrayList<Vertice>();
	
	Vertice entrada;
	
	// v�rtices de sa�da
	Set<Vertice> saidas;
	
	// todas as arestas
	List<Aresta> arestas;
	
	Metodo unidade;
	
	public DUG() {		
		saidas = new HashSet<Vertice>();
		arestas = new ArrayList<Aresta>();
	}
	
	// opera��es com v�rtices
	
	// inser��o de um v�rtice
	public void inserir(Vertice vertice) {
		if (!vertices.contains(vertice)) {
			vertices.add(vertice);			
			vertice.numero = String.valueOf(vertices.size());			
		}		
	}
	
	// retorna os v�rtices
	/**
	 * @return  the vertices
	 * @uml.property  name="vertices"
	 */
	public Vertice[] getVertices() {
		return vertices.toArray(new Vertice[0]);
	}
	
	// retorna um determinado v�rtice a partir de seu label
	public Vertice getVertice(Label label) {
		for (Vertice v : vertices) {
			if (v.getLabel().equals(label)) {
				return v;
			}
		}
		return null;
	}
	
	// retorna um determinado v�rtice a partir da string do label
	public Vertice getVertice(String label) {
		for (Vertice v : vertices) {
			if (v.getLabel().toString().equals(label)) {
				return v;
			}
		}
		return null;
	}
	
	// remove um v�rtice do grafo
	// --- retornar uma excecao se o vertice n�o est� no grafo??
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
	
	// define v como v�rtice de entrada
	/**
	 * @param entrada  the entrada to set
	 * @uml.property  name="entrada"
	 */
	public void setEntrada(Vertice v) {
		entrada = v;
	}
	
	// verifica se v � o v�rtice de entrada
	public boolean isEntrada(Vertice v) {
		return (v.equals(entrada));
	}
	
	// define o v�rtice como v�rtice de sa�da
	// lan�ar uma exce��o se o v�rtice n�o est� no grafo?
	public void setSaida(Vertice saida) {
		saidas.add(saida);
	}
	
	// verifica se v � um v�rtice de sa�da
	public boolean isSaida(Vertice v) {
		return (saidas.contains(v));
	}	
	
	// opera��es com arestas
	
	// inser��o de uma aresta
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
	
	// retorna as sa�das de um determinado v�rtice
	public Aresta[] getSaidas(Vertice vertice) {
		Vector<Aresta> saidas = new Vector<Aresta>();
		for (Aresta a : arestas) {
			if (a.getOrigem().equals(vertice)) {
				saidas.add(a);
			}
		}
		return saidas.toArray(new Aresta[0]);
	}
	
	// retorna as entradas de um determinado v�rtice
	public Aresta[] getEntradas(Vertice vertice) {
		Vector<Aresta> entradas = new Vector<Aresta>();
		for (Aresta a : arestas) {
			if (a.getDestino().equals(vertice)) {
				entradas.add(a);
			}
		}
		return entradas.toArray(new Aresta[0]);
	}
	
	// recupera uma aresta espec�fica
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
