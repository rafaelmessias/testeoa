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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.testeoa.grafo.AODU;
import org.testeoa.grafo.AODUComb;
import org.testeoa.grafo.Aresta;
import org.testeoa.grafo.Vertice;
import org.testeoa.modelo.Metodo;

/**
 * @author Rafael
 */
public class AnaliseDinamica {

	private static AODU grafo;

	private static Set<Metodo> unidades = new HashSet<Metodo>();

	private static Stack<Metodo> pilha = new Stack<Metodo>();

	private static Caminho caminho = new Caminho();

	public static Map<String, Integer> contagem = new HashMap<String, Integer>();

	public static void reiniciar() {
		grafo = null;
		unidades = new HashSet<Metodo>();
		pilha = new Stack<Metodo>();
		caminho = new Caminho();
	}

	public static void registrar(AODU g) {
		grafo = g;
		unidades.add(grafo.getUnidade());
		Integer cont = contagem.get(grafo.getUnidade().getNome());
		if (cont == null) {
			cont = 1;
		} else {
			cont++;
		}
		contagem.put(grafo.getUnidade().getNome(), cont);
	}

	public static void registrar(AODUComb g) {
		registrar((AODU) g);
		for (AODU gs : g.getSecundarios()) {
			unidades.add(gs.getUnidade());
		}
	}

	public static void LABEL(String label) {
		if (pilha.peek() != null) {
			Vertice u = caminho.ultimoVertice();
			Vertice v = grafo.getVertice(label);
			if (v != null) {
				Aresta a = grafo.getAresta(u, v);
				if (a != null) {
					caminho.inserir(a);
				}
				caminho.inserir(v);
			}

		}
	}

	public static void UNIDADE(String owner, String nome, String desc) {
		for (Metodo u : unidades) {
			if (u.getClasse().getNome().equals(owner)) {
				if (u.getNome().equals(nome) && u.getDesc().equals(desc)) {
					pilha.push(u);
					return;
				}
			}
		}
		pilha.push(null);
	}

	public static void RETORNO() {
		pilha.pop();
	}

	/**
	 * @return the caminho
	 * @uml.property name="caminho"
	 */
	public static Caminho getCaminho() {
		return caminho;
	}

}
