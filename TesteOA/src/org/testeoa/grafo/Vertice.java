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

import org.objectweb.asm.Label;


/**
 * @author  Rafael
 */
public class Vertice {
	
	/**
	 * @uml.property  name="numero"
	 */
	String numero;
	
	// informações do fluxo de controle
	/**
	 * @uml.property  name="label"
	 */
	Label label;
	List<String> instrucoes;
	
	// informações do fluxo de dados
	Set<String> definicoes;
	Set<String> usos;
	
	public Vertice(Label label) {
		this.label = label;
		instrucoes = new ArrayList<String>();
		definicoes = new HashSet<String>();
		usos = new HashSet<String>();
	}
	
	/**
	 * @return  the label
	 * @uml.property  name="label"
	 */
	public Label getLabel() {
		return label;
	}
	
	/**
	 * @return  the instrucoes
	 * @uml.property  name="instrucoes"
	 */
	public String[] getInstrucoes() {
		return instrucoes.toArray(new String[0]);
	}
	
	/**
	 * @return  the definicoes
	 * @uml.property  name="definicoes"
	 */
	public String[] getDefinicoes() {
		return definicoes.toArray(new String[0]);
	}
	
	/**
	 * @return  the usos
	 * @uml.property  name="usos"
	 */
	public String[] getUsos() {
		return usos.toArray(new String[0]);
	}
	
	/**
	 * @param label  the label to set
	 * @uml.property  name="label"
	 */
	public void setLabel(Label label) {
		this.label = label;
	}
	
	public void inserirInstrucao(String instrucao) {
		instrucoes.add(instrucao);
	}
	
	public void inserirDefinicao(String variavel) {
		definicoes.add(variavel);
	}
	
	public void inserirUso(String variavel) {
		usos.add(variavel);
	}

	/**
	 * @return  the numero
	 * @uml.property  name="numero"
	 */
	public String getNumero() {
		return numero;
	}

	@Override
	public String toString() {
		String s = "<html><p align=center><font size=5>";
		s += numero + "</font></p></html>";
		return s;
	}
	
}
