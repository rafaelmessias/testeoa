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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;



/**
 * @author    Rafael
 */
public class Classe implements ItemProjeto {	
	
	/**
	 * @uml.property  name="nome"
	 */
	String nome;
	
	/**
	 * @uml.property  name="nomeCurto"
	 */
	String nomeCurto;	
	
	/**
	 * @uml.property  name="bytecode"
	 */
	byte[] bytecode;
	
	List<Metodo> unidades;	
	
	private Projeto projeto;
	
	Set<Metodo> metodos = new LinkedHashSet<Metodo>();
	
	Set<Metodo> metodosN = new LinkedHashSet<Metodo>();
	
	public Classe(String nome) {
		this.nome = nome;
		String[] s = nome.split("\\.");
		this.nomeCurto = s[s.length - 1];
		unidades = new ArrayList<Metodo>();
	}
	
	/**
	 * @return  the nome
	 * @uml.property  name="nome"
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return  the nomeCurto
	 * @uml.property  name="nomeCurto"
	 */
	public String getNomeCurto() {
		return nomeCurto;
	}
	
	/**
	 * @return  the unidades
	 * @uml.property  name="unidades"
	 */
	public Metodo[] getUnidades() {
		return unidades.toArray(new Metodo[0]);
	}

	/**
	 * @return  the bytecode
	 * @uml.property  name="bytecode"
	 */
	public byte[] getBytecode() {
		return bytecode;
	}

	/**
	 * @param bytecode  the bytecode to set
	 * @uml.property  name="bytecode"
	 */
	public void setBytecode(byte[] bytecode) {
		this.bytecode = bytecode;
	}

	@Override
	public String toString() {
		return nomeCurto;
	}
	
	/**
	 * @return  the projeto
	 * @uml.property  name="projeto"
	 */
	public Projeto getProjeto() {
		return projeto;
	}

	/**
	 * @param projeto  the projeto to set
	 * @uml.property  name="projeto"
	 */
	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	
	/**
	 * @return  the metodos
	 * @uml.property  name="metodos"
	 */
	public Metodo[] getMetodos() {
		return metodos.toArray(new Metodo[0]);
	}
	
	/**
	 * @return  the metodosN
	 * @uml.property  name="metodosN"
	 */
	public Metodo[] getMetodosN() {
		return metodosN.toArray(new Metodo[0]);
	}
	
	public Metodo getMetodo(String nome, String desc) {
		for (Metodo m : metodos) {			
			if (m.getNome().equals(nome) && m.getDesc().equals(desc)) {
				return m;
			}
		}
		return null;
	}
	
	public Metodo getMetodoN(String nome, String desc) {
		for (Metodo m : metodosN) {			
			if (m.getNome().equals(nome) && m.getDesc().equals(desc)) {
				return m;
			}
		}
		return null;
	}
	
	public void inserir(Metodo m) {
		metodos.add(m);
		metodosN.remove(m);
		inserirUnidade(m);
		if (getProjeto() != null) {
			getProjeto().ativar(m);
		}
	}
	
	public void inserirUnidade(Metodo m) {
		unidades.add(m);
		m.classe = this;
	}
	
	public void remover(Metodo m) {
		metodos.remove(m);
		metodosN.add(m);		
		unidades.remove(m);
		// desativar testes cuja unidade principal é 'm'
		if (getProjeto() != null) {
			getProjeto().desativar(m);
		}
	}
	
}
