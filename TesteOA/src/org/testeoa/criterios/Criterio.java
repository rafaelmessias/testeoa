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

package org.testeoa.criterios;

import org.testeoa.dinamica.Caminho;
import org.testeoa.grafo.AODU;

/**
 * @author    Rafael
 */
public abstract class Criterio {
	
	AODU grafo;
	
	/**
	 * @param grafo  the grafo to set
	 * @uml.property  name="grafo"
	 */
	public void setGrafo(AODU g) {
		grafo = g;
	}

	public abstract float verificar(Caminho caminho);

}
