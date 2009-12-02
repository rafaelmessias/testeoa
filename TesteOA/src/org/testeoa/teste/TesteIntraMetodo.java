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

import org.testeoa.criterios.Criterio;
import org.testeoa.dinamica.AnaliseDinamica;
import org.testeoa.grafo.AODU;
import org.testeoa.modelo.Metodo;

public class TesteIntraMetodo extends TesteEstrutural {
	
	private Metodo unidade;
	
	public TesteIntraMetodo(CasoTeste c, Metodo m) {
		super(c);
		unidade = m;
	}
	
	@Override
	public AODU getGrafo() {
		return unidade.getGrafo();
	}
	
	public void inserir(Criterio c) {
		c.setGrafo(getGrafo());
		super.inserir(c);
	}
	
	@Override
	void registrarUnidades() {
		AnaliseDinamica.registrar(getGrafo());		
	}	
	
	public Metodo getUnidade() {
		return unidade;
	}
	
	@Override
	public String getTipo() {
		return "Intra-Método";
	}
	
	String getLegenda() {
		return " [M]";
	}
	
	@Override
	public String toString() {
		return super.toString() + getLegenda();
	}

}
