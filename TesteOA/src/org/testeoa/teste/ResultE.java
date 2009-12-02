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

import java.util.Hashtable;
import java.util.Map;

import org.junit.runner.Result;
import org.testeoa.criterios.Criterio;
import org.testeoa.dinamica.Caminho;

public class ResultE extends Result {
	
	private Map<Criterio, Float> res = new Hashtable<Criterio, Float>();
	
	private Caminho caminho;
	
	public void novoResultado(Criterio c, float i) {
		res.put(c, i);
	}
	
	public Map<Criterio, Float> getCriteriosMap() {
		return res;
	}
	
	public void setCaminho(Caminho c) {
		caminho = c;
	}
	
	public Caminho getCaminho() {
		return caminho;
	}

}
