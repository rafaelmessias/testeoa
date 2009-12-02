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

package org.testeoa.modelo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.testeoa.estatica.AnaliseEstatica;
import org.testeoa.excecoes.ExProjeto;
import org.testeoa.grafo.Vertice;
import org.testeoa.grafo.VerticeTransversal;
import org.testeoa.gui.GUI;
import org.testeoa.teste.TesteEstrutural;
import org.testeoa.teste.TesteInterMetodoAdendo;
import org.testeoa.teste.TesteIntraMetodo;

/**
 * @author    Rafael
 */
public class Projeto {
	
	/**
	 * @uml.property  name="nome"
	 */
	String nome;
	
	AnaliseEstatica analise;
	
	Set<Aspecto> aspectos = new LinkedHashSet<Aspecto>();
	
	Set<Classe> classes = new LinkedHashSet<Classe>();
	
	Set<Classe> owners = new LinkedHashSet<Classe>();
	
	Set<TesteEstrutural> testes = new LinkedHashSet<TesteEstrutural>();
	
	/**
	 * @uml.property  name="aberto"
	 */
	boolean aberto = false;

	public Projeto(String nome) {
		this.nome = nome;
	}
	
	// Operações
	
	public void abrir() {		
		analise = new AnaliseEstatica();
		for (Classe c : classes) {
			analise.carregar(c);			
		}
		for (Aspecto a : aspectos) {
			analise.carregar(a);
		}		
		for (TesteEstrutural t : testes) {
			analise.carregar(t);
		}
		aberto = true;
	}
	
	public void fechar() {
		analise = null;
		aberto = false;
	}
	
	public Classe[] getGeral() {
		return owners.toArray(new Classe[0]);
	}
	
	public Classe getGeral(String nome) {
		for (Classe o : owners) {
			if (o.getNome().equals(nome) || o.getNomeCurto().equals(nome)) {
				return o;
			}
		}
		return null;
	}
	
	// Classes
	
	/**
	 * @return  the classes
	 * @uml.property  name="classes"
	 */
	public Classe[] getClasses() {
		return classes.toArray(new Classe[0]);
	}
	
	public Classe getClasse(String nome) {
		for (Classe c : classes) {
			if (c.getNome().equals(nome) || c.getNomeCurto().equals(nome)) {
				return c;
			}
		}
		return null;
	}	
	
	public void inserir(Classe c) throws ExProjeto {
		if (getClasse(c.getNome()) != null) {
			throw new ExProjeto("Classe [" + c.getNomeCurto() + "] duplicada.");
		}
		owners.add(c);
		classes.add(c);
		c.setProjeto(this);
		if (aberto) {
			analise.carregar(c);
		}
		for (Metodo m : c.getMetodos()) {
			ativar(m);
		}
	}
	
	public void remover(Classe c) {
		owners.remove(c);
		classes.remove(c);
		for (Metodo m : c.getMetodos()) {
			desativar(m);
		}
		c.setProjeto(null);
	}
	
	// Aspectos
	
	/**
	 * @return  the aspectos
	 * @uml.property  name="aspectos"
	 */
	public Aspecto[] getAspectos() {
		return aspectos.toArray(new Aspecto[0]);
	}
	
	public Aspecto getAspecto(String nome) {
		for (Aspecto a : aspectos) {
			if (a.getNome().equals(nome) || a.getNomeCurto().equals(nome)) {
				return a;
			}
		}
		return null;
	}
	
	public void inserir(Aspecto a) throws ExProjeto {
		if (getAspecto(a.getNome()) != null) {
			throw new ExProjeto("Aspecto duplicado.");
		}
		owners.add(a);
		aspectos.add(a);
		a.setProjeto(this);
		if (aberto) {
			analise.carregar(a);
		}
		for (Metodo m : a.getMetodos()) {
			ativar(m);
		}
		for (Adendo ad : a.getAdendos()) {
			ativar(ad);
		}
	}
	
	public void remover(Aspecto a) {
		owners.remove(a);
		aspectos.remove(a);
		for (Metodo m : a.getMetodos()) {
			desativar(m);
		}
		for (Adendo ad : a.getAdendos()) {
			desativar(ad);
		}
		a.setProjeto(null);
	}
	
	// Testes
	
	/**
	 * @return  the testes
	 * @uml.property  name="testes"
	 */
	public TesteEstrutural[] getTestes() {
		return testes.toArray(new TesteEstrutural[0]);
	}
	
	public TesteEstrutural getTeste(String nome) {
		for (TesteEstrutural t : testes) {
			if (t.getCasoTeste().getNome().equals(nome)) {
				return t;
			}
		}
		return null;
	}
	
	public void inserir(TesteEstrutural t) {
		testes.add(t);
		t.setProjeto(this);
		if (aberto) {
			analise.carregar(t);
		}
		if (t instanceof TesteInterMetodoAdendo) {
			TesteInterMetodoAdendo tm = (TesteInterMetodoAdendo)t;
			for (Metodo m : getMetodosRel(tm.getUnidade())) {
				tm.inserir(m);
			}
			for (Adendo ad : getAdendosRel(tm.getUnidade())) {
				tm.inserir(ad);
			}
		}
		// TODO verificar a unidade principal no momento da inserção
		// TODO verificar a possibilidade de un. secundarias que não pertencem
	}
	
	private Adendo[] getAdendosRel(Metodo u) {
		Set<Adendo> set = new HashSet<Adendo>();
		for (VerticeTransversal vt : u.getGrafo().getVerticesTransversais()) {
			Aspecto a = getAspecto(vt.getAspecto());
			if (a != null) {
				Adendo ad = a.getAdendo(vt.getTipo(), vt.getAdNumero());
				if (ad != null) {
					set.add(ad);
				}
			}
		}
		return set.toArray(new Adendo[0]);
	}
	
	private Metodo[] getMetodosRel(Metodo u) {
		Set<Metodo> set = new HashSet<Metodo>();
		for (Vertice v : u.getGrafo().getVertices()) {
			for (String i : v.getInstrucoes()) {
				if (i.startsWith("INVOKE")) {
					// TODO Será que existe ponto no desc??
					String[] s = i.split("[ \\.]");
					Classe c = getClasse(s[1].replace('/','.'));
					if (c != null) {
						Metodo m = c.getMetodo(s[2], s[3]);
						if (m != null) {
							set.add(m);
						}
					}
				}
			}
		}
		return set.toArray(new Metodo[0]);
	}
	
	public void remover(TesteEstrutural t) {
		testes.remove(t);
		t.setProjeto(null);
	}
	
	/**
	 * @return  the aberto
	 * @uml.property  name="aberto"
	 */
	public boolean isAberto() {
		return aberto;
	}	

	/**
	 * @return  the nome
	 * @uml.property  name="nome"
	 */
	public String getNome() {
		return nome;
	}	
	
	void desativar(Metodo u) {
		for (TesteEstrutural t : testes) {
			Metodo tu = ((TesteIntraMetodo)t).getUnidade();
			// checa unidade principal
			if (tu.equals(u)) {				
				t.setAtivo(false);
				GUI.registrar("Aviso: A unidade principal [" + u + "] do " +
						"teste [" + t +	"] foi removida do projeto [" + 
						t.getProjeto() + "].");
			}
			// checa unidades secundarias
			if (t instanceof TesteInterMetodoAdendo) {
				TesteInterMetodoAdendo tm = (TesteInterMetodoAdendo)t;
				if (Arrays.asList(tm.getUnidadesSecundarias()).contains(u)) {
					tm.remover(u);
					GUI.registrar("Aviso: A unidade secundária [" + u + "] " +
							"do teste [" + t + "] foi removida do projeto [" + 
							t.getProjeto() + "].");
				}
			}
		}
	}
	
	void ativar(Metodo u) {
		for (TesteEstrutural t : testes) {
			Metodo tu = ((TesteIntraMetodo)t).getUnidade();
			// checa unidade principal
			if (tu.getNomeCompleto().equals(u.getNomeCompleto())) {
				t.setAtivo(true);
			}
			// checa unidades secundárias
			if (t instanceof TesteInterMetodoAdendo) {
				TesteInterMetodoAdendo tm = (TesteInterMetodoAdendo)t;
				if (u instanceof Metodo) {
					if (Arrays.asList(getMetodosRel(tu)).contains(u)) {
						tm.inserir(u);
					}
				}
				if (u instanceof Adendo) {
					if (Arrays.asList(getAdendosRel(tu)).contains(u)) {
						tm.inserir(u);
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return getNome();
	}

}
