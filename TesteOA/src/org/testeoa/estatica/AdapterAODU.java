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

import org.objectweb.asm.MethodVisitor;
import org.testeoa.grafo.AODU;
import org.testeoa.grafo.PosProcAODU;
import org.testeoa.grafo.VerticeTransversal;



/**
 * @author    Rafael
 */
public class AdapterAODU extends AdapterDUGE {
	
	private AODU grafo;
	
	public AdapterAODU(MethodVisitor mv, AODU g) {
		super(mv, g);
		grafo = g;
		posProc = new PosProcAODU(grafo);
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {		
		super.visitMethodInsn(opcode, owner, name, desc);
		
		String[] s = name.split("\\$");
		if (s.length == 5 && !s[1].startsWith("interMethod")) {
			String aspecto = s[2].replace("_", ".");
			String tipo = s[1];
			int numero = Integer.valueOf(s[3]);
			VerticeTransversal vt =
				new VerticeTransversal(verticeAtual, aspecto, tipo, numero);
			grafo.inserir(vt);
			verticeAtual = vt;
			inserirVerticeAtual();			
		}
	}
	
}
