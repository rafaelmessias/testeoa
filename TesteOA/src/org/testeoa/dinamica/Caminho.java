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

package org.testeoa.dinamica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testeoa.grafo.Aresta;
import org.testeoa.grafo.Vertice;

/**
 * @author  Rafael
 */
public class Caminho {
	
	List<Vertice> vertices = new ArrayList<Vertice>();
	
	List<Aresta> arestas = new ArrayList<Aresta>();
	
	/**
	 * @uml.property  name="frequencia"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Integer"
	 */
	Map<Object, Integer> frequencia = new HashMap<Object, Integer>();
	
	// TODO frequencia das arestas
	
	int max_freq = 0;
		
	public void inserir(Vertice v) {
		vertices.add(v);
		int f = 0;
		if (frequencia.get(v) == null) {
			f = 1;
		}
		else {
			f = frequencia.get(v) + 1;
		}
		frequencia.put(v, f);
		if (f > max_freq) {
			max_freq = f;
		}
	}
	
	public boolean contem(Vertice v) {
		return vertices.contains(v);
	}
	
	/**
	 * @return  the vertices
	 * @uml.property  name="vertices"
	 */
	public Vertice[] getVertices() {
		return vertices.toArray(new Vertice[0]);
	}
	
	public int getFrequencia(Object o) {
		if (frequencia.get(o) != null) {
			return frequencia.get(o);
		}
		else {
			return 0;
		}
	}
	
	public int getMaxFreq() {
		return max_freq;
	}
	
	public Vertice ultimoVertice() {
		if (vertices.size() > 0) {
			return vertices.get(vertices.size()-1);
		}
		return null;
	}
	
	public void inserir(Aresta a) {
		arestas.add(a);
		int f = 0;
		if (frequencia.get(a) == null) {
			f = 1;
		}
		else {
			f = frequencia.get(a) + 1;
		}
		frequencia.put(a, f);
		if (f > max_freq) {
			max_freq = f;
		}
	}
	
	public boolean contem(Aresta a) {
		return arestas.contains(a);
	}

}
