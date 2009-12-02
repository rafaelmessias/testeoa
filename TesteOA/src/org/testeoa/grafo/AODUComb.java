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

import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Metodo;

/**
 * @author    Rafael
 */
public class AODUComb extends AODU {
	
	private AODU principal = new AODU();
	
	private List<AODU> secundarios = new ArrayList<AODU>();
	
	private Set<Aresta> backup = new HashSet<Aresta>();
	
	public AODUComb(AODU p) {
		principal.unidade = p.unidade;
		inserirTudo(p, principal);
		inserirTudo(p, this);
		principal.entrada = p.entrada;
		principal.saidas.addAll(p.saidas);
		renumerar();
	}
	
	@Override
	public Aresta getAresta(Vertice origem, Vertice destino) {		
		if (origem == null || destino == null) {
			return null;
		}
		
		for (Aresta a : arestas) {
			
			if (a.getOrigem().getNumero().equals(origem.getNumero()) && 
					a.getDestino().getNumero().equals(destino.getNumero())) {
				return a;
			}
		}
		return null;
	}
	
	public void inserir(AODU g) {
		if (secundarios.contains(g)) {
			return;
		}
		secundarios.add(g);
		inserirTudo(g, this);
		Metodo u = g.unidade;
		if (u instanceof Adendo) {
			Adendo a = (Adendo) u;
			for (VerticeTransversal vt : principal.verticesTransversais) {
				if (vt.getAspecto().equals(a.getClasse().getNome())) {
					if (vt.getAdNumero() == a.getNumero()) {
						conectar(vt, g);
					}
				}
			}
		}
		if (u instanceof Metodo) {
			Metodo m = (Metodo) u;
			for (Vertice v : principal.getVertices()) {
				for (String i : v.getInstrucoes()) {
					if (i.startsWith("INVOKE")) {
						// TODO Será que existe ponto no desc??
						String[] s = i.split("[ \\.]");
						String o = s[1].replace('/', '.');
						if (o.equals(m.getClasse().getNome())) {
							if (s[2].equals(m.getNome()) && s[3].equals(m.getDesc())) {
								conectar(v, g);
							}
						}
					}
				}
			}
		}
		renumerar();
	}
	
	@Override
	void renumerar() {		
		for (Vertice v : principal.vertices) {
			v.numero = "1." + (principal.vertices.indexOf(v) + 1); 
		}
		for (AODU g : secundarios) {
			for (Vertice v : g.vertices) {
				v.numero = (secundarios.indexOf(g) + 2) + "." + 
					(g.vertices.indexOf(v) + 1);
			}
		}
	}
	
	public void remover(AODU g) {
		secundarios.remove(g);
		Set<Vertice> vts = new HashSet<Vertice>();
		for (Vertice v : g.vertices) {
			if (g.isEntrada(v)) {
				for (Aresta a : getEntradas(v)) {
					vts.add(a.getOrigem());
				}
			}
			remover(v);			
		}
		for (Aresta a : backup) {
			if (vts.contains(a.getOrigem())) {
				inserir(a);
			}
		}
	}
	
	private void inserirTudo(AODU orig, AODU dest) {
		dest.vertices.addAll(orig.vertices);
		dest.verticesTransversais.addAll(orig.verticesTransversais);
		dest.verticesDependentesExcecao.addAll(orig.verticesDependentesExcecao);
		dest.verticesIndependentesExcecao.addAll(orig.verticesIndependentesExcecao);
		dest.arestas.addAll(orig.arestas);
		dest.arestasRegulares.addAll(orig.arestasRegulares);
		dest.arestasExcecao.addAll(orig.arestasExcecao);
		dest.arestasTransversais.addAll(orig.arestasTransversais);
		dest.arestasDependentesExcecao.addAll(orig.arestasDependentesExcecao);
		dest.arestasIndependentesExcecao.addAll(orig.arestasIndependentesExcecao);
	}
	
	private void conectar(VerticeTransversal vt, AODU g) {
		Aresta a = getSaidasRegulares(vt)[0];		
		Vertice v = a.getDestino();		
		backup.add(a);
		remover(a);
		inserir(new Aresta(vt, g.entrada));
		for (Vertice vs : g.saidas) {
			inserir(new Aresta(vs, v));
		}		
	}
	
	private void conectar(Vertice v, AODU g) {
		inserir(new Aresta(v, g.entrada));
//		for (Vertice vs : g.saidas) {
//			inserir(new Aresta(vs, v));
//		}
	}
	
	/**
	 * @return  the principal
	 * @uml.property  name="principal"
	 */
	public AODU getPrincipal() {
		return principal;
	}
	
	@Override
	public boolean isEntrada(Vertice v) {		
		return principal.isEntrada(v);
	}
	
	@Override
	public boolean isSaida(Vertice v) {
		return principal.isSaida(v);
	}
	
	@Override
	public Metodo getUnidade() {
		return principal.getUnidade();
	}
	
	/**
	 * @return  the secundarios
	 * @uml.property  name="secundarios"
	 */
	public AODU[] getSecundarios() {
		return secundarios.toArray(new AODU[0]);
	}

}
