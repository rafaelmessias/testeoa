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

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Aspecto;



/**
 * @author    Rafael
 */
public class AdapterAspecto extends AdapterClasse {
	
	private Aspecto aspecto;
	
	public AdapterAspecto(Aspecto aspecto, ClassVisitor visitor) {
		super(aspecto, visitor);
		this.aspecto = aspecto;
	}
	
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		
		MethodVisitor mv = 
			super.visitMethod(access, name, desc, signature, exceptions);
		
		if(AnaliseEstatica.isAdendo(name)) {
			String[] s = name.split("\\$");
			String tipo = s[1];
			int numero = Integer.valueOf(s[3]);
			Adendo adendo = aspecto.getAdendo(tipo, numero);
			if (adendo != null) {
				MethodVisitor next = 
					new AdapterInstrum(classe.getNome(), name, desc, mv);			
				return new AdapterAODU(next, adendo.getGrafo());
			}
		}
		
		return mv;
	}

}
