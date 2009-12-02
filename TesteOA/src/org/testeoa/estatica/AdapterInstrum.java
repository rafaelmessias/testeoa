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

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class AdapterInstrum extends MethodAdapter {
	
	private String owner;
	private String nome;	
	private String desc;
	
	public AdapterInstrum(String owner, String nome, String desc, 
			MethodVisitor mv) {
		super(mv);
		this.owner = owner;
		this.nome = nome;
		this.desc = desc;
	}
	
	@Override
	public void visitCode() {
		super.visitCode();
		inserirUnidade(owner, nome, desc);
	}
	
	@Override
	public void visitLabel(Label label) {		
		super.visitLabel(label);
		inserirLabel(label);
	}
	
	@Override
	public void visitInsn(int opcode) {
		if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
			inserirRetorno();
		}
		if (opcode == Opcodes.ATHROW) {
			//TODO O ATHROW SÓ PODE RETORNAR SE ESTIVER FORA DO TRY-CATCH
			//inserirRetorno();
		}
		super.visitInsn(opcode);
	}
	
	void inserirUnidade(String owner, String nome, String desc) {
		super.visitLdcInsn(owner);
		super.visitLdcInsn(nome);
		super.visitLdcInsn(desc);
		super.visitMethodInsn(
				Opcodes.INVOKESTATIC,
				"org/testeoa/dinamica/AnaliseDinamica",
				"UNIDADE",
				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	}
	
	void inserirLabel(Label label) {
		super.visitLdcInsn(label.toString());
		super.visitMethodInsn(
				Opcodes.INVOKESTATIC, 
				"org/testeoa/dinamica/AnaliseDinamica",
				"LABEL",
				"(Ljava/lang/String;)V");
	}
	
	void inserirRetorno() {
		super.visitMethodInsn(
				Opcodes.INVOKESTATIC,
				"org/testeoa/dinamica/AnaliseDinamica",
				"RETORNO",
				"()V");
	}

}
