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

package org.testeoa.persist;

import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Ambiente;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;
import org.testeoa.modelo.Projeto;
import org.testeoa.teste.TesteEstrutural;


public aspect Persistencia {
	
	private DocumentoXML docXML;
	
	pointcut inicio(): execution(static void Ambiente.iniciar());
	
	pointcut addProjeto(Projeto p):
		!within(DocumentoXML) && args(p) &&
		call(static void Ambiente.inserirProjeto(Projeto));
	
	pointcut remProjeto(Projeto p):
		args(p) && call(static void Ambiente.removerProjeto(Projeto));
	
	pointcut addClasse(Classe c, Projeto p):
		!within(DocumentoXML) && args(c) && target(p) &&
		if(Ambiente.contem(p)) && call(void Projeto.inserir(Classe));
	
	pointcut remClasse(Classe c, Projeto p):
		args(c) && target(p) && if(Ambiente.contem(p)) && 
		call(void Projeto.remover(Classe));
	
	pointcut addAspecto(Aspecto a, Projeto p):
		!within(DocumentoXML) && args(a) && target(p) && 
		if(Ambiente.contem(p)) && call(void Projeto.inserir(Aspecto));
	
	pointcut remAspecto(Aspecto a, Projeto p):
		args(a) && target(p) && if(Ambiente.contem(p)) && 
		call(void Projeto.remover(Aspecto));
	
	pointcut addTeste(TesteEstrutural t, Projeto p):
		!within(DocumentoXML) && args(t) && target(p) && 
		if(Ambiente.contem(p)) && call(void Projeto.inserir(TesteEstrutural));
	
	pointcut remTeste(TesteEstrutural t, Projeto p):
		args(t) && target(p) && if(Ambiente.contem(p)) &&
		call(void Projeto.remover(TesteEstrutural));
	
	pointcut addMetodo(Metodo m, Classe c):
		!within(DocumentoXML) && args(m) && target(c) &&
		if(Ambiente.contem(c.getProjeto())) && 
		call(void inserir(Metodo));
	
	pointcut remMetodo(Metodo m, Classe c):
		!within(DocumentoXML) && args(m) && target(c) &&
		if(Ambiente.contem(c.getProjeto())) &&
		call(void remover(Metodo));
	
	pointcut addAdendo(Adendo ad, Aspecto a):
		!within(DocumentoXML) && args(ad) && target(a) &&
		if(Ambiente.contem(a.getProjeto())) &&
		call(void inserir(Adendo));
	
	pointcut remAdendo(Adendo ad, Aspecto a):
		!within(DocumentoXML) && args(ad) && target(a) &&
		if(Ambiente.contem(a.getProjeto())) &&
		call(void remover(Adendo));
	
	after(): inicio() {
		docXML = Ambiente.docXML;
	}
	
	after(Projeto p): addProjeto(p) {
		docXML.inserir(p);
		docXML.salvar();
	}
	
	after(Projeto p): remProjeto(p) {
		docXML.remover(p);
		docXML.salvar();
	}
	
	after(Classe c, Projeto p) returning(): addClasse(c, p) {
		docXML.inserir(c, p);
		docXML.salvar();
	}
	
	after(Classe c, Projeto p): remClasse(c, p) {
		docXML.remover(c, p);
		docXML.salvar();
	}
	
	after(Aspecto a, Projeto p) returning(): addAspecto(a, p) {
		docXML.inserir(a, p);
		docXML.salvar();
	}
	
	after(Aspecto a, Projeto p): remAspecto(a, p) {
		docXML.remover(a, p);
		docXML.salvar();
	}
	
	after(TesteEstrutural t, Projeto p): addTeste(t, p) {
		docXML.inserir(t, p);
		docXML.salvar();
	}
	
	after(TesteEstrutural t, Projeto p): remTeste(t, p) {
		docXML.remover(t, p);
		docXML.salvar();
	}
	
	after(Metodo m, Classe c): addMetodo(m, c) {
		docXML.inserir(m, c);
		docXML.salvar();
	}
	
	after(Metodo m, Classe c): remMetodo(m, c) {
		docXML.remover(m, c);
		docXML.salvar();
	}		
	
	after(Adendo ad, Aspecto a): addAdendo(ad, a) {
		docXML.inserir(ad, a);
		docXML.salvar();
	}
	
	after(Adendo ad, Aspecto a): remAdendo(ad, a) {
		docXML.remover(ad, a);
		docXML.salvar();
	}

}
