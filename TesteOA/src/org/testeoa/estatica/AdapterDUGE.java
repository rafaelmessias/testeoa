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

import java.util.Vector;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.testeoa.grafo.ArestaExcecao;
import org.testeoa.grafo.DUGE;
import org.testeoa.grafo.PosProcDUGE;
import org.testeoa.grafo.Vertice;


/**
 * @author    Rafael
 */
public class AdapterDUGE extends AdapterDUG {
	
	private DUGE grafo;
	
	private Vector<BlocoTryCatch> blocosTryCatch;
	
	public AdapterDUGE(MethodVisitor mv, DUGE g) {
		super(mv, g);
		grafo = g;
		posProc = new PosProcDUGE(grafo);
		blocosTryCatch = new Vector<BlocoTryCatch>();
	}
	
	@Override
	void inserirVerticeAtual() {
		for (BlocoTryCatch btc : blocosTryCatch) {
			if (btc.ativo) {
				btc.vertices.add(verticeAtual);
			}
		}
		super.inserirVerticeAtual();
	}
	
	@Override
	void inserirArestas() {
		for (BlocoTryCatch btc : blocosTryCatch) {
			if (btc.tratador.equals(verticeAtual.getLabel())) {
				for (Vertice v : btc.vertices) {
					grafo.inserir(
							new ArestaExcecao(v, verticeAtual, btc.excecao));
				}
			}
		}
		super.inserirArestas();
	}
	
	@Override
	void novoVerticeAtual(Label label) {
		for (BlocoTryCatch btc : blocosTryCatch) {
			if (btc.inicio.equals(label)) {
				btc.ativo = true;
			}
			if (btc.fim.equals(label)) {
				btc.ativo = false;
			}
		}
		super.novoVerticeAtual(label);
	}
	
	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		blocosTryCatch.add(new BlocoTryCatch(start, end, handler, type));
		super.visitTryCatchBlock(start, end, handler, type);
	}
	
	public class BlocoTryCatch {
		
		Label inicio;
		Label fim;
		Label tratador;
		
		String excecao;
		
		boolean ativo;
		
		Vector<Vertice> vertices;
		
		public BlocoTryCatch(Label inicio, Label fim, Label tratador, String excecao) {
			this.inicio = inicio;
			this.fim = fim;
			this.tratador = tratador;
			this.excecao = excecao;
			ativo = false;
			vertices = new Vector<Vertice>();
		}

	}

}