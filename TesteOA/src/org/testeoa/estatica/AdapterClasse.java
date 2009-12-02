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

package org.testeoa.estatica;


import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;


/**
 * @author    Rafael
 */
public class AdapterClasse extends ClassAdapter {
	
	Classe classe;
	
	public AdapterClasse(Classe classe, ClassVisitor visitor) {
		super(visitor);
		this.classe = classe;
	}
	
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		
		MethodVisitor mv = 
			super.visitMethod(access, name, desc, signature, exceptions);
		
		Metodo metodo = classe.getMetodo(name, desc);
		if (metodo != null) {
			MethodVisitor next = 
				new AdapterInstrum(classe.getNome(), name, desc, mv);			
			return new AdapterAODU(next, metodo.getGrafo());
		}
		
		return mv;
	}

}
