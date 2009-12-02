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

import org.testeoa.excecoes.ExInitXML;
import org.testeoa.persist.DocumentoXML;


/**
 * @author    Rafael
 */
public abstract class Ambiente {
	
	public static DocumentoXML docXML;
	
	private static Set<Projeto> projetos = new LinkedHashSet<Projeto>();
	
	public static void iniciar() {
		try {
			docXML = new DocumentoXML("ambiente.xml");			
			docXML.criarAmbiente();
		}
		catch (ExInitXML e) {
			System.err.println(e.getCause());
		}
	}
	
	public static void inserirProjeto(Projeto p) {
		projetos.add(p);
	}
	
	public static void removerProjeto(Projeto p) {
		projetos.remove(p);
	}
	
	/**
	 * @return  the projetos
	 * @uml.property  name="projetos"
	 */
	public static Projeto[] getProjetos() {
		return projetos.toArray(new Projeto[0]);
	}
	
	public static boolean contem(Projeto p) {
		return projetos.contains(p);
	}
}
