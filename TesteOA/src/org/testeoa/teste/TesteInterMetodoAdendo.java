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

import java.util.HashSet;
import java.util.Set;

import org.testeoa.dinamica.AnaliseDinamica;
import org.testeoa.grafo.AODUComb;
import org.testeoa.modelo.Metodo;

public class TesteInterMetodoAdendo extends TesteIntraMetodo {

	AODUComb grafo;
	
	Set<Metodo> unidadesSecundarias = new HashSet<Metodo>();
	
	public TesteInterMetodoAdendo(CasoTeste ct, Metodo m) {
		super(ct, m);
		grafo = new AODUComb(m.getGrafo());
	}	

	@Override
	public AODUComb getGrafo() {
		return grafo;
	}
	
	public void inserir(Metodo u) {
		if (!unidadesSecundarias.contains(u)) {
			grafo.inserir(u.getGrafo());
			reiniciar();
			unidadesSecundarias.add(u);
		}		
	}
	
	public void remover(Metodo u) {
		if (unidadesSecundarias.contains(u)) {
			grafo.remover(u.getGrafo());
			reiniciar();
			unidadesSecundarias.remove(u);		
		}		
	}
	
	public Metodo[] getUnidadesSecundarias() {
		return unidadesSecundarias.toArray(new Metodo[0]);
	}
	
	@Override
	void registrarUnidades() {
		AnaliseDinamica.registrar(getGrafo());		
	}
	
	@Override
	public String getTipo() {
		return "Inter-Método-Adendo";
	}
	
	@Override
	String getLegenda() {
		return " [IMA]";
	}

}
