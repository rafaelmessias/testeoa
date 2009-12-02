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

import org.testeoa.grafo.AODU;



/**
 * @author    Rafael
 */
public class Metodo implements ItemProjeto {
	
	/**
	 * @uml.property  name="nome"
	 */
	String nome;
	
	/**
	 * @uml.property  name="desc"
	 */
	String desc;
	
	AODU grafo;
	
	Classe classe;
	
	public Metodo(String nome, String desc) {
		this.nome = nome;
		this.desc = desc;
		grafo = new AODU();
		grafo.setUnidade(this);
	}	
	
	/**
	 * @return  the nome
	 * @uml.property  name="nome"
	 */
	public String getNome() {
		return nome;
	}
	
	public String getNomeCompleto() {
		return getClasse().getNomeCurto() + "." + getNome() + " " + getDesc();
	}
	
	/**
	 * @return  the desc
	 * @uml.property  name="desc"
	 */
	public String getDesc() {
		return desc;
	}
	
	@Override
	public String toString() {
		return nome + " " + desc;
	}

	/**
	 * @return  the grafo
	 * @uml.property  name="grafo"
	 */
	public AODU getGrafo() {
		return grafo;
	}

	/**
	 * @param desc  the desc to set
	 * @uml.property  name="desc"
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @param nome  the nome to set
	 * @uml.property  name="nome"
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * @return  the classe
	 * @uml.property  name="classe"
	 */
	public Classe getClasse() {
		return classe;	
	}
	
	public Projeto getProjeto() {
		return getClasse().getProjeto();
	}
	
}
