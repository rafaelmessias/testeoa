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
public class Aresta {
	
	private Vertice origem;
	
	private Vertice destino;

	public Aresta(Vertice origem, Vertice destino) {
		this.origem = origem;
		this.destino = destino;
	}
	
	/**
	 * @return  the origem
	 * @uml.property  name="origem"
	 */
	public Vertice getOrigem() {
		return origem;
	}
	
	/**
	 * @return  the destino
	 * @uml.property  name="destino"
	 */
	public Vertice getDestino() {
		return destino;
	}
	
	/**
	 * @param destino  the destino to set
	 * @uml.property  name="destino"
	 */
	void setDestino(Vertice destino) {
		this.destino = destino;
	}

	/**
	 * @param origem  the origem to set
	 * @uml.property  name="origem"
	 */
	void setOrigem(Vertice origem) {
		this.origem = origem;
	}

	@Override
	public String toString() {
		return "[Aresta (" + getOrigem() + " > " + getDestino() + ")]";
	}
	
}

