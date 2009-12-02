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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.AbstractVisitor;
import org.testeoa.grafo.Aresta;
import org.testeoa.grafo.DUG;
import org.testeoa.grafo.PosProcDUG;
import org.testeoa.grafo.Vertice;


/**
 * @author    Rafael
 */
public class AdapterDUG extends MethodAdapter {
	
	private DUG grafo;
	
	PosProcDUG posProc;
	
	Vertice verticeAtual;
	
	private Vertice verticeAnterior;
	
	private boolean adjacente;
	
	private boolean entrada;
	
	private boolean saida;
	
	/**
	 * @uml.property  name="alvos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.Vector<Label>"
	 */
	private Map<Vertice, Vector<Label>> alvos;
	
	/**
	 * @uml.property  name="defs"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.Set<Integer>"
	 */
	private Map<Vertice, Set<Integer>> defs = new HashMap<Vertice, Set<Integer>>();
	
	/**
	 * @uml.property  name="usos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.Set<Integer>"
	 */
	private Map<Vertice, Set<Integer>> usos = new HashMap<Vertice, Set<Integer>>();
	
	private List<String> vars = new ArrayList<String>();

	public AdapterDUG(MethodVisitor mv, DUG g) {
		super(mv);
		grafo = g;
		posProc = new PosProcDUG(grafo);
		verticeAtual = null;
		verticeAnterior = null;
		entrada = true;
		alvos = new HashMap<Vertice, Vector<Label>>();
	}
	
	void novoVerticeAtual(Label label) {
		verticeAtual = new Vertice(label);
		adjacente = true;
	}
	
	private void novoAlvo(Label label) {
		if (!alvos.containsKey(verticeAtual)) {
			alvos.put(verticeAtual, new Vector<Label>());
		}
		alvos.get(verticeAtual).add(label);
	}
	
	void inserirVerticeAtual() {
		grafo.inserir(verticeAtual);
		
		if (entrada) {
			grafo.setEntrada(verticeAtual);
		}
		entrada = false;
		
		if (saida) {
			grafo.setSaida(verticeAtual);
		}
		saida = false;
		
		inserirArestas();
		
		if (adjacente) {
			verticeAnterior = verticeAtual;
		}
		else {
			verticeAnterior = null;
		}
		
		verticeAtual = null;
	}
	
	void inserirArestas() {
		// se � adjacente ao anterior...
		if (verticeAnterior != null) {
			grafo.inserir(
					new Aresta(verticeAnterior, verticeAtual));
		}
		
		// se algum de seus alvos j� foi inserido...
		if (alvos.containsKey(verticeAtual)) {
			for (Label label : alvos.get(verticeAtual)) {
				Vertice destino = grafo.getVertice(label);
				if (destino != null) {
					grafo.inserir(
							new Aresta(verticeAtual, destino));
				}
			}
		}
		
		// se � alvo de algum desvio...
		for (Vertice origem : alvos.keySet()) {
			if (alvos.get(origem).contains(verticeAtual.getLabel())) {
				grafo.inserir(new Aresta(origem, verticeAtual));
			}
		}
		
		
	}
	
	private void addInstrucao(String instrucao) {
		if (verticeAtual == null) {
			Label l = new Label();
			novoVerticeAtual(l);
			// gambiarra
			super.visitLabel(l);
		}
		verticeAtual.inserirInstrucao(instrucao);
	}
	
	@Override
	public void visitLabel(Label label) {
		if (verticeAtual != null) {
			inserirVerticeAtual();
		}		
		novoVerticeAtual(label);
		super.visitLabel(label);
	}
	
	@Override
	public void visitJumpInsn(int opcode, Label label) {
		addInstrucao(getInstrucao(opcode) + " " + label);
		novoAlvo(label);
		if (opcode == Opcodes.GOTO) {
			adjacente = false;
		}
		inserirVerticeAtual();
		super.visitJumpInsn(opcode, label);
	}
	
	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {		
		for (Label label : labels) {
			novoAlvo(label);
		}
		novoAlvo(dflt);
		
		String instrucao = "LOOKUPSWITCH";
		for (int i = 0; i < keys.length; i++) {
			instrucao += " " + keys[i] + ": " + labels[i] + ";";
		}
		instrucao += " default: " + dflt;
		addInstrucao(instrucao);
		
		adjacente = false;
		
		inserirVerticeAtual();
		
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}
	
	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt,
			Label[] labels) {
		for (Label label : labels) {
			novoAlvo(label);
		}
		novoAlvo(dflt);
		
		String instrucao = "TABLESWITCH";
		for (int i = 0; i <= (max - min); i++) {
			instrucao += " " + (min + i) + ": " + labels[i] + ";";
		}
		instrucao += " default: " + dflt;
		addInstrucao(instrucao);
		
		adjacente = false;
		
		inserirVerticeAtual();
		
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}
	
	@Override
	public void visitInsn(int opcode) {
		addInstrucao(getInstrucao(opcode));
		if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
			adjacente = false;
			saida = true;
			inserirVerticeAtual();
		}
		else if (opcode == Opcodes.ATHROW) {
			adjacente = false;
			saida = true;
			inserirVerticeAtual();
		}
		super.visitInsn(opcode);
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
		// insere a descri��o da instru��o
		String instrucao = getInstrucao(opcode);
		instrucao += " " + owner + "." + name + " " + desc;
		addInstrucao(instrucao);
		// An�lise do Fluxo de Dados
		String[] s = owner.split("/");
		String o = s[s.length-1];
		if (opcode == Opcodes.PUTFIELD || opcode == Opcodes.PUTSTATIC) {
			verticeAtual.inserirDefinicao(o + "." + name);
		}
		if (opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC) {
			verticeAtual.inserirUso(o + "." + name);
		}
		super.visitFieldInsn(opcode, owner, name, desc);
	}
	
	@Override
	public void visitIincInsn(int var, int increment) {
		addInstrucao("IINC " + var + " " + increment);
		// An�lise do Fluxo de Dados
		addUso(var);
		addDef(var);
		super.visitIincInsn(var, increment);
	}
	
	@Override
	public void visitIntInsn(int opcode, int operand) {
		String instrucao = getInstrucao(opcode);
		if (opcode == Opcodes.NEWARRAY) {
			instrucao += " " + getTipo(operand);
		}
		else {
			instrucao += " " + operand;
		}		
		addInstrucao(instrucao);
		super.visitIntInsn(opcode, operand);
	}
	
	@Override
	public void visitLdcInsn(Object cst) {
		String instrucao = "LDC";
		if (cst instanceof String) {
			instrucao += " \"" + cst + "\"";
		}
		else {
			instrucao += " " + cst;
		}
		addInstrucao(instrucao);
		super.visitLdcInsn(cst);
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		String instrucao = getInstrucao(opcode);
		instrucao += " " + owner + "." + name + " " + desc;
		addInstrucao(instrucao);
		
		super.visitMethodInsn(opcode, owner, name, desc);
	}
	
	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		addInstrucao("MULTIANEWARRAY " + desc + " " + dims);		
		super.visitMultiANewArrayInsn(desc, dims);
	}
	
	@Override
	public void visitTypeInsn(int opcode, String desc) {
		addInstrucao(getInstrucao(opcode) + " " + desc);
		super.visitTypeInsn(opcode, desc);
	}
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		String ins = getInstrucao(opcode) + " " + var;
		addInstrucao(ins);
		// An�lise do Fluxo de Dados
		if (opcode >= Opcodes.ILOAD && opcode <= Opcodes.ALOAD) {
			addUso(var);
		}
		if (opcode >= Opcodes.ISTORE && opcode <= Opcodes.ASTORE) {
			addDef(var);
		}
		super.visitVarInsn(opcode, var);
	}
	
	@Override
	public void visitLineNumber(int line, Label start) {
		addInstrucao("LINENUMBER " + line);
		super.visitLineNumber(line, start);
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature, 
			Label start, Label end, int index) {		
		//vars.add(index, name);
		vars.add(name);
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}
	
	private String getInstrucao(int opcode) {
		return AbstractVisitor.OPCODES[opcode];
	}
	
	private String getTipo(int tipo) {
		return AbstractVisitor.TYPES[tipo];
	}
	
	private void addDef(int var) {
		if (defs.get(verticeAtual) == null) {
			defs.put(verticeAtual, new HashSet<Integer>());
		}
		defs.get(verticeAtual).add(var);
	}
	
	private void addUso(int var) {
		if (usos.get(verticeAtual) == null) {
			usos.put(verticeAtual, new HashSet<Integer>());
		}
		usos.get(verticeAtual).add(var);
	}
	
	@Override
	public void visitEnd() {
		super.visitEnd();
		for (Vertice v : defs.keySet()) {
			for (int i : defs.get(v)) {
				if (i < vars.size()) {
					v.inserirDefinicao(vars.get(i));
				}
			}
		}
		for (Vertice v : usos.keySet()) {
			for (int i : usos.get(v)) {
				if (i < vars.size()) {
					v.inserirUso(vars.get(i));
				}				
			}
		}
		posProc.otimizar();
	}
	
}