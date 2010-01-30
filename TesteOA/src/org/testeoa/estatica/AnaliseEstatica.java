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

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.testeoa.excecoes.ExAnaliseEstatica;
import org.testeoa.gui.GUI;
import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;
import org.testeoa.teste.CasoTeste;
import org.testeoa.teste.TesteEstrutural;



public class AnaliseEstatica extends ClassLoader {
	
	public static Classe lerClasse(String nome) throws ExAnaliseEstatica {		
		try {
			ClassReader cr = new ClassReader(nome);		
			ClassNode cn = new ClassNode();
			cr.accept(cn, ClassReader.SKIP_FRAMES);
			
			if (isAspecto(cn)) {
				throw new ExAnaliseEstatica("[" + nome + "] não é uma classe.", 
						null);
			}
			
			Classe c = new Classe(nome);
			for (MethodNode mn : (List<MethodNode>)cn.methods) {
				if (isValido(mn)) {
					c.inserir(new Metodo(mn.name, mn.desc));
				}
			}
			
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//			TraceClassVisitor tcv =
//				new TraceClassVisitor(cw, new PrintWriter(System.out));
			ClassAdapter adapter = new AdapterClasse(c, cw);
			cn.accept(adapter);
			
			c.setBytecode(cw.toByteArray());
			
			return c;
		}
		catch (IOException e) {
			throw new ExAnaliseEstatica(
					"Erro ao tentar abrir a classe [" + nome + "].", e);
		}
	}
	
	public static Aspecto lerAspecto(String nome) throws ExAnaliseEstatica {
		try {
			ClassReader cr = new ClassReader(nome);
			ClassNode cn = new ClassNode();
			cr.accept(cn, ClassReader.SKIP_FRAMES);
			
			if (!isAspecto(cn)) {
				throw new ExAnaliseEstatica("[" + nome + "] não é um aspecto.", 
						null);
			}
			
			Aspecto a = new Aspecto(nome);
			for (MethodNode mn : (List<MethodNode>)cn.methods) {
				if (isAdendo(mn.name)) {
					a.inserir(new Adendo(mn.name, mn.desc));
				}
				else {
					if (isValido(mn)) {
						a.inserir(new Metodo(mn.name, mn.desc));
					}
				}
			}
			
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//			TraceClassVisitor tcv =
//				new TraceClassVisitor(cw, new PrintWriter(System.out));
			ClassAdapter adapter = new AdapterAspecto(a, cw);
			cn.accept(adapter);
			
			a.setBytecode(cw.toByteArray());
			
			return a;
		}
		catch (IOException e) {
			throw new ExAnaliseEstatica(
					"Erro ao tentar abrir o aspecto [" + nome + "].", e);
		}
	}
	
	public static CasoTeste lerCasoTeste(String nome) throws ExAnaliseEstatica {
		try {
			ClassReader cr = new ClassReader(nome);
			ClassNode cn = new ClassNode();
			cr.accept(cn, ClassReader.SKIP_FRAMES);
			
			if (!isCasoTeste(cn)) {
				throw new ExAnaliseEstatica("[" + nome + "] não é um caso de " +
						"teste JUnit válido.", null);
			}
			
			CasoTeste ct = new CasoTeste(nome);
			
			return ct;
		}
		catch (IOException e) {
			throw new ExAnaliseEstatica(
					"Erro ao tentar abrir o caso de teste [" + nome + "].", e);
		}
	}
	
	private static boolean isCasoTeste(ClassNode cn) {
		for (MethodNode mn : (List<MethodNode>)cn.methods) {
			List<AnnotationNode> l = mn.visibleAnnotations;
			if (l == null) {
				continue;
			}
			for (AnnotationNode an : l) {
				if (an.desc.equals("Lorg/junit/Test;")) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isAspecto(ClassNode cn) {
		if (cn.visibleAnnotations != null) {			
			for (AnnotationNode an : (List<AnnotationNode>)cn.visibleAnnotations) {				
				if (an.desc.equals("Lorg/aspectj/lang/annotation/Aspect;")) {
					return true;
				}
			}
		}
		return false;		
	}
	
	public static boolean isAdendo(String nome) {
		
		String[] s = nome.split("\\$");
		if (s.length == 5 && !s[1].equals("pointcut") && !s[1].startsWith("interMethod")) {
			return true;			
		}
		return false;
	}
	
	private static boolean isValido(MethodNode mn) {
		if (mn.name.equals("<init>")) {
			return false;
		}
		if (mn.name.equals("<clinit>")) {
			return false;
		}
		if (mn.name.equals("hasAspect")) {
			return false;
		}
		if (mn.name.equals("aspectOf")) {
			return false;
		}
		if (mn.name.startsWith("ajc$")) {
			if (mn.name.startsWith("ajc$interMethod")) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	public void carregar(Classe classe) {		
		byte[] b = classe.getBytecode();
		try {
			defineClass(classe.getNome(), b, 0, b.length);
		}
		catch (LinkageError e) {
			// ...
		}
	}
	
	public void carregar(TesteEstrutural teste) {
		CasoTeste ct = teste.getCasoTeste();
		try {
			ClassReader cr = new ClassReader(ct.getNome());
			ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
			cr.accept(cw, ClassReader.SKIP_FRAMES);
			byte[] b = cw.toByteArray();
			ct.setCodigo(defineClass(ct.getNome(), b, 0, b.length));
		}
		catch (IOException e) {
			 GUI.registrar(teste.getProjeto() + " > " + e);
		}
		catch (LinkageError e) {
			try {
				ct.setCodigo(loadClass(ct.getNome()));
			}
			catch (ClassNotFoundException e2) {
				// não vai acontecer
			}
		}
	}

}
