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



/**
 * @author  Rafael
 */
public class Adendo extends Metodo {
	
	/**
	 * @uml.property  name="tipo"
	 */
	String tipo;
	
	/**
	 * @uml.property  name="numero"
	 */
	int numero;

	public Adendo(String nome, String desc) {
		super(nome, desc);		
		String[] s = nome.split("\\$");
		tipo = s[1];
		numero = Integer.valueOf(s[3]);
	}

	/**
	 * @param numero  the numero to set
	 * @uml.property  name="numero"
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @param tipo  the tipo to set
	 * @uml.property  name="tipo"
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return  the tipo
	 * @uml.property  name="tipo"
	 */
	public String getTipo() {
		return tipo;
	}
	
	/**
	 * @return  the numero
	 * @uml.property  name="numero"
	 */
	public int getNumero() {
		return numero;
	}
	
	@Override
	public Aspecto getClasse() {
		return (Aspecto) super.getClasse();
	}
	
	@Override
	public String toString() {
		return tipo + numero;
	}
	
	public String getDescricao() {
		return getClasse().getNomeCurto() + "." + toString();
	}

}
