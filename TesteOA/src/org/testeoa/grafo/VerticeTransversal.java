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
 * @author  Rafael
 */
public class VerticeTransversal extends Vertice {
	
	/**
	 * @uml.property  name="aspecto"
	 */
	String aspecto;
	/**
	 * @uml.property  name="tipo"
	 */
	String tipo;
	int ad_numero;
	
	public VerticeTransversal(Vertice v, String aspecto, String tipo, int numero) {
		super(v.getLabel());
		this.instrucoes = v.instrucoes;
		this.definicoes = v.definicoes;
		this.usos = v.usos;
		this.aspecto = aspecto;
		this.tipo = tipo;
		this.ad_numero = numero;
	}
	
	/**
	 * @return  the aspecto
	 * @uml.property  name="aspecto"
	 */
	public String getAspecto() {
		return aspecto;
	}
	
	/**
	 * @return  the tipo
	 * @uml.property  name="tipo"
	 */
	public String getTipo() {
		return tipo;		
	}
	
	public int getAdNumero() {
		return ad_numero;
	}
	
	@Override
	public String toString() {
		String[] s = aspecto.split("\\.");
		String a = s[s.length-1];
		return "<html><p align=center>&lt;" + a + "." + tipo + ad_numero + 
			"&gt;<br><font size=5>" + numero + "</font></p></html>";
	}

}
