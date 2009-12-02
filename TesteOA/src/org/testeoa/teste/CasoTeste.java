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

package org.testeoa.teste;

public class CasoTeste {
	
	String nome;
	
	Class<?> codigo;
	
	public CasoTeste(String n) {
		nome = n;
	}

	public Class<?> getCodigo() {
		return codigo;
	}

	public void setCodigo(Class<?> codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}
	
	public String getNomeCurto() {
		String[] s = nome.split("\\.");
		return s[s.length - 1];
	}
}
