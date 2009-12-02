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

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * @author  Rafael
 */
public class Aspecto extends Classe {
	
	Set<Adendo> adendos = new LinkedHashSet<Adendo>();
	
	Set<Adendo> adendosN = new LinkedHashSet<Adendo>();
	
	public Aspecto(String nome) {
		super(nome);
	}
	
	// Adendos
	
	/**
	 * @return  the adendos
	 * @uml.property  name="adendos"
	 */
	public Adendo[] getAdendos() {
		return adendos.toArray(new Adendo[0]);
	}
	
	/**
	 * @return  the adendosN
	 * @uml.property  name="adendosN"
	 */
	public Adendo[] getAdendosN() {
		return adendosN.toArray(new Adendo[0]);
	}
	
	public Adendo getAdendo(String tipo, int numero) {
		for (Adendo a : adendos) {			
			if (a.getTipo().equals(tipo) && a.getNumero() == numero) {
				return a;
			}
		}
		return null;
	}
	
	public Adendo getAdendo(String nome) {
		for (Adendo a : adendos) {
			if (a.getNome().equals(nome)) {
				return a;
			}
		}
		return null;
	}
	
	public Adendo getAdendoN(String tipo, int numero) {
		for (Adendo a : adendosN) {			
			if (a.getTipo().equals(tipo) && a.getNumero() == numero) {
				return a;
			}
		}
		return null;
	}
	
	public Adendo getAdendoN(String nome) {
		for (Adendo a : adendosN) {			
			if (a.getNome().equals(nome)) {
				return a;
			}
		}
		return null;
	}
	
	public void inserir(Adendo a) {
		adendos.add(a);
		adendosN.remove(a);
		inserirUnidade(a);
		if (getProjeto() != null) {
			getProjeto().ativar(a);
		}		
	}
	
	public void remover(Adendo a) {
		adendos.remove(a);
		adendosN.add(a);
		//super.remover(a);
		if (getProjeto() != null) {
			getProjeto().desativar(a);
		}
	}

}
