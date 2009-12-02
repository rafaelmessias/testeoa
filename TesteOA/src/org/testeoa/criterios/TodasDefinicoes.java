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
import org.testeoa.grafo.Vertice;

public class TodasDefinicoes extends Criterio {
	
	@Override
	public String toString() {
		return "Todas-Definicoes";
	}

	@Override
	public float verificar(Caminho caminho) {
		float total = 0, parcial = 0;
		for (Vertice v : grafo.getVertices()) {
			int n = v.getDefinicoes().length;
			total += n;
			if (caminho.contem(v)) {
				parcial += n;
			}
		}
		if (total == 0) {
			return 0;
		}
		return parcial / total;
	}

}
