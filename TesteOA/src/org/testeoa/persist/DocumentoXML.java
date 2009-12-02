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

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.testeoa.criterios.Criterio;
import org.testeoa.criterios.TodasArestas;
import org.testeoa.criterios.TodasArestasDepExcecao;
import org.testeoa.criterios.TodasArestasIndepExcecao;
import org.testeoa.criterios.TodasArestasTransversais;
import org.testeoa.criterios.TodasDefinicoes;
import org.testeoa.criterios.TodosUsos;
import org.testeoa.criterios.TodosUsosDepExcecao;
import org.testeoa.criterios.TodosUsosIndepExcecao;
import org.testeoa.criterios.TodosUsosTransversais;
import org.testeoa.criterios.TodosVertices;
import org.testeoa.criterios.TodosVerticesDepExcecao;
import org.testeoa.criterios.TodosVerticesIndepExcecao;
import org.testeoa.criterios.TodosVerticesTransversais;
import org.testeoa.estatica.AnaliseEstatica;
import org.testeoa.excecoes.ExAnaliseEstatica;
import org.testeoa.excecoes.ExImportarProjeto;
import org.testeoa.excecoes.ExInitXML;
import org.testeoa.excecoes.ExProjeto;
import org.testeoa.excecoes.ExcecaoExportarProjeto;
import org.testeoa.grafo.AODU;
import org.testeoa.grafo.Aresta;
import org.testeoa.grafo.ArestaExcecao;
import org.testeoa.grafo.Vertice;
import org.testeoa.grafo.VerticeTransversal;
import org.testeoa.gui.GUI;
import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Ambiente;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;
import org.testeoa.modelo.Projeto;
import org.testeoa.teste.CasoTeste;
import org.testeoa.teste.TesteAdendoAdendo;
import org.testeoa.teste.TesteAdendoMetodo;
import org.testeoa.teste.TesteEstrutural;
import org.testeoa.teste.TesteInterMetodo;
import org.testeoa.teste.TesteInterMetodoAdendo;
import org.testeoa.teste.TesteIntraAdendo;
import org.testeoa.teste.TesteIntraMetodo;
import org.testeoa.teste.TesteMetodoAdendo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class DocumentoXML {
	
	private String arquivo_xml;
	
	private Document documento;
	
	public DocumentoXML(String xf) throws ExInitXML {
		arquivo_xml = xf;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			documento = db.parse(new File(arquivo_xml));
		}
		catch (ParserConfigurationException e) {
			throw new ExInitXML(e);
		}
		catch (IOException e) {
			throw new ExInitXML(e);
		}
		catch (SAXException e) {
			throw new ExInitXML(e);
		}
	}
	
	public void criarAmbiente() {
		Element raiz = (Element) documento.getFirstChild();
		// cria os projetos
		NodeList pl = raiz.getElementsByTagName("projeto");
		for (int ip = 0; ip < pl.getLength(); ip++) {
			Element ep = (Element) pl.item(ip);
			Projeto p = criar(ep);
			Ambiente.inserirProjeto(p);
		}		
	}
	
	public Projeto criar(Element ep) {
		Projeto p = new Projeto(ep.getAttribute("nome"));		
		// cria as classes
		NodeList cl = ep.getElementsByTagName("classe");
		for (int ic = 0; ic < cl.getLength(); ic++) {
			Element ec = (Element) cl.item(ic);
			String nome = ec.getAttribute("nome");
			Classe c = null; 
			try {
				c = AnaliseEstatica.lerClasse(nome);
				for (Metodo m : c.getMetodos()) {
					c.remover(m);
				}
				NodeList ml = ec.getElementsByTagName("metodo");
				for (int im = 0; im < ml.getLength(); im++) {
					Element em = (Element) ml.item(im);
					String mnome = em.getAttribute("nome");
					String desc = em.getAttribute("desc");
					Metodo m = c.getMetodoN(mnome, desc);
					if (m != null) {
						c.inserir(m);
					}
					else {
						GUI.registrar("Problema ao carregar o ambiente: " +
								"Projeto [" + p + "] - O método [" + c + 
								"." + mnome + desc + "] não existe.");
					}
				}
				p.inserir(c);
			}
			catch (ExAnaliseEstatica e) {
				GUI.registrar("Problema ao carregar o ambiente: " +
						"Projeto [" + p + "] - " + e.getMessage());
			}
			catch (ExProjeto e) {
				GUI.registrar("Problema ao carregar o ambiente: " +
						"Projeto [" + p + "] - " + e.getMessage());
			}
		}
		// cria os aspectos
		NodeList al = ep.getElementsByTagName("aspecto");
		for (int ia = 0; ia < al.getLength(); ia++) {
			Element ea = (Element) al.item(ia);
			String nome = ea.getAttribute("nome");
			Aspecto a = null;
			try {
				a = AnaliseEstatica.lerAspecto(nome);
				for (Metodo m : a.getMetodos()) {
					a.remover(m);						
				}
				NodeList ml = ea.getElementsByTagName("metodo");
				for (int im = 0; im < ml.getLength(); im++) {
					Element em = (Element) ml.item(im);
					String mnome = em.getAttribute("nome");
					String desc = em.getAttribute("desc");
					Metodo m = a.getMetodoN(mnome, desc);
					if (m != null) {
						a.inserir(m);
					}
					else {
						GUI.registrar("Problema ao carregar o ambiente: " +
								"Projeto [" + p + "] - O método [" + a + 
								"." + mnome + desc + "] não existe.");				
					}
				}
				for (Adendo ad : a.getAdendos()) {
					a.remover(ad);
				}
				NodeList adl = ea.getElementsByTagName("adendo");
				for (int iad = 0; iad < adl.getLength(); iad++) {
					Element ead = (Element) adl.item(iad);
					String tipo = ead.getAttribute("tipo");
					int num = Integer.valueOf(ead.getAttribute("num"));
					Adendo ad = a.getAdendoN(tipo, num);
					if (ad != null) {
						a.inserir(ad);
					}
					else {
						GUI.registrar("Problema ao carregar o ambiente: " +
								"Projeto [" + p + "] - O adendo [" + a + 
								"." + tipo + num + "] não existe.");
					}
				}
				p.inserir(a);
			}
			catch (ExAnaliseEstatica e) {
				GUI.registrar("Problema ao carregar o ambiente: " +
						"Projeto [" + p + "] - " + e.getMessage());
			}				
			catch (ExProjeto e) {
				GUI.registrar("Problema ao carregar o ambiente: " +
						"Projeto [" + p + "] - " + e.getMessage());
			}
		}
		// cria os testes
		NodeList tl = ep.getElementsByTagName("teste");
		for(int it = 0; it < tl.getLength(); it++) {				
			Element et = (Element) tl.item(it);
			try {
				// leitura do caso de teste
				String caso = et.getAttribute("caso");
				CasoTeste ct = AnaliseEstatica.lerCasoTeste(caso);					
				// leitura da unidade
				Element eu = 
					(Element)et.getElementsByTagName("unidade").item(0);
				String utipo = eu.getAttribute("tipo");
				String nome = eu.getAttribute("nome");
				String owner = eu.getAttribute("owner");
				Metodo uni = null;
				boolean desativado = false;
				if (utipo.equals("metodo")) {
					String desc = eu.getAttribute("desc");
					Classe c = (Classe) p.getGeral(owner);
					if (c != null) {
						uni = c.getMetodo(nome, desc);
						if (uni == null) {
							uni = c.getMetodoN(nome, desc);
							desativado = true;
						}
					}
					else {
						throw new ExAnaliseEstatica("Problema ao carregar" +
								" o ambiente: Projeto [" + p + "] - A " +
								"unidade principal do caso de teste [" + 
								caso + "] não é válida.", null);
					}
				}
				if (utipo.equals("adendo")) {
					Aspecto a = p.getAspecto(owner);
					if (a != null) {
						uni = a.getAdendo(nome);
						if (uni == null) {
							uni = a.getAdendoN(nome);
							desativado = true;
						}
					}
					else {
						throw new ExAnaliseEstatica("Problema ao carregar o ambiente: " +
								"Projeto [" + p + "] - A unidade " +
								"principal do caso de teste [" + caso +
								"] não é válida.", null);
					}
				}
				if (uni == null) {
					throw new ExAnaliseEstatica("Problema ao carregar o ambiente: " +
							"Projeto [" + p + "] - A unidade " +
							"principal do caso de teste [" + caso +
							"] não é válida.", null);
				}
				// instanciar o teste
				int tipo = Integer.valueOf(et.getAttribute("tipo"));
				TesteIntraMetodo t = null;
				switch (tipo) {
				case 0:
					t = new TesteIntraMetodo(ct, uni);						
					break;
				case 1:
					t = new TesteIntraAdendo(ct, (Adendo)uni);						
					break;
				case 2:
					t = new TesteInterMetodoAdendo(ct, uni);						
					break;
				case 3:
					t = new TesteInterMetodo(ct, uni);
					break;
				case 4:
					t = new TesteMetodoAdendo(ct, uni);
					break;
				case 5:
					t = new TesteAdendoAdendo(ct, (Adendo)uni);
					break;
				case 6:
					t = new TesteAdendoMetodo(ct, (Adendo)uni);
					break;
				}
				if (desativado) {
					t.setAtivo(false);
				}
				p.inserir(t);
				// inserção dos critérios
				NodeList crl = et.getElementsByTagName("criterio");
				for (int icr = 0; icr < crl.getLength(); icr++) {
					Element ecr = (Element) crl.item(icr);
					int ctipo = Integer.valueOf(ecr.getAttribute("tipo"));
					switch(ctipo) {
					case 0:
						t.inserir(new TodasArestas());
						break;
					case 1:
						t.inserir(new TodasArestasDepExcecao());
						break;
					case 2:
						t.inserir(new TodasArestasIndepExcecao());
						break;
					case 3:
						t.inserir(new TodasArestasTransversais());
						break;
					case 4:
						t.inserir(new TodasDefinicoes());
						break;
					case 5:
						t.inserir(new TodosUsos());
						break;
					case 6:
						t.inserir(new TodosUsosDepExcecao());
						break;
					case 7:
						t.inserir(new TodosUsosIndepExcecao());
						break;
					case 8:
						t.inserir(new TodosUsosTransversais());
						break;
					case 9:
						t.inserir(new TodosVertices());
						break;
					case 10:
						t.inserir(new TodosVerticesDepExcecao());
						break;
					case 11:
						t.inserir(new TodosVerticesIndepExcecao());
						break;
					case 12:
						t.inserir(new TodosVerticesTransversais());
						break;
					}
				}					
			}
			catch (ExAnaliseEstatica e) {
				GUI.registrar("Problema ao carregar o ambiente: " +
						"Projeto [" + p + "] - " + e.getMessage());
			}
		}
		return p;
	}
	
	public void inserir(Projeto proj) {
		Element d = (Element) documento.getFirstChild();
		d.appendChild(criarElemento(proj));
	}
	
	public void remover(Projeto proj) {
		Element ep = encontrar(proj);
		if (ep != null) {
			documento.getFirstChild().removeChild(ep);
		}
	}
	
	public void inserir(Classe c, Projeto p) {
		Element ep = encontrar(p);
		if (ep != null) {
			ep.appendChild(criarElemento(c));
		}
	}
	
	public void remover(Classe c, Projeto p) {
		Element ec = encontrar(c, p);
		if (ec != null) {
			ec.getParentNode().removeChild(ec);
		}
	}
	
	public void inserir(Aspecto a, Projeto p) {
		Element ep = encontrar(p);
		if (ep != null) {
			ep.appendChild(criarElemento(a));
		}
	}
	
	public void remover(Aspecto a, Projeto p) {
		Element ea = encontrar(a, p);
		if (ea != null) {
			ea.getParentNode().removeChild(ea);
		}
	}
	
	public void inserir(TesteEstrutural t, Projeto p) {
		Element ep = encontrar(p);
		if (ep != null) {
			ep.appendChild(criarElemento(t));
		}
	}
	
	public void remover(TesteEstrutural t, Projeto p) {
		Element et = encontrar(t, p);
		if (et != null) {
			et.getParentNode().removeChild(et);
		}
	}
	
	public void inserir(Metodo m, Classe c) {
		Element ec = encontrar(c, c.getProjeto());
		if (ec == null) {
			ec = encontrar((Aspecto)c, c.getProjeto());
		}
		if (ec != null) {
			ec.appendChild(criarElemento(m));
		}
	}
	
	public void remover(Metodo m, Classe c) {
		Element em = encontrar(m, c);
		if (em != null) {
			em.getParentNode().removeChild(em);
		}
	}
	
	public void inserir(Adendo ad, Aspecto a) {
		Element ea = encontrar(a, a.getProjeto());
		if (ea != null) {
			ea.appendChild(criarElemento(ad));
		}
	}
	
	public void remover(Adendo ad, Aspecto a) {
		Element ead = encontrar(ad, a);
		if (ead != null) {
			ead.getParentNode().removeChild(ead);
		}
	}
	
	private Element encontrar(Projeto proj) {
		Element a = (Element) documento.getFirstChild();
		NodeList pl = a.getElementsByTagName("projeto");
		for (int i = 0; i < pl.getLength(); i++) {
			Element p = (Element) pl.item(i);
			if (p.getAttribute("nome").equals(proj.getNome())) {
				return p;
			}
		}
		return null;
	}
	
	private Element encontrar(Classe c, Projeto p) {
		Element ep = encontrar(p);
		if (ep == null) {
			return null;
		}
		NodeList cl = ep.getElementsByTagName("classe");
		for (int i = 0; i < cl.getLength(); i++) {
			Element ec = (Element) cl.item(i);
			if (ec.getAttribute("nome").equals(c.getNome())) {
				return ec;
			}
		}
		return null;
	}
	
	private Element encontrar(Aspecto a, Projeto p) {
		Element ep = encontrar(p);
		if (ep == null) {
			return null;
		}
		NodeList al = ep.getElementsByTagName("aspecto");
		for (int i = 0; i < al.getLength(); i++) {
			Element ea = (Element) al.item(i);
			if (ea.getAttribute("nome").equals(a.getNome())) {
				return ea;
			}
		}
		return null;
	}
	
	private Element encontrar(TesteEstrutural t, Projeto p) {
		Element ep = encontrar(p);
		if (ep == null) {
			return null;
		}
		NodeList tl = ep.getElementsByTagName("teste");
		for (int i = 0; i < tl.getLength(); i++) {
			Element et = (Element) tl.item(i);
			if(et.getAttribute("caso").equals(t.getCasoTeste().getNome())) {
				return et;
			}
		}
		return null;
	}
	
	private Element encontrar(Metodo m, Classe c) {
		Element ec = encontrar(c, c.getProjeto());
		if (ec == null) {
			ec = encontrar((Aspecto)c, c.getProjeto());
			if (ec == null) {
				return null;
			}
		}
		NodeList ml = ec.getElementsByTagName("metodo");
		for (int i = 0; i < ml.getLength(); i++) {
			Element em = (Element) ml.item(i);
			if (em.getAttribute("nome").equals(m.getNome())) {
				if (em.getAttribute("desc").equals(m.getDesc())) {
					return em;
				}
			}
		}
		return null;
	}
	
	private Element encontrar(Adendo ad, Aspecto a) {
		Element ea = encontrar(a, a.getProjeto());		
		NodeList adl = ea.getElementsByTagName("adendo");
		for (int i = 0; i < adl.getLength(); i++) {
			Element ead = (Element) adl.item(i);
			int num = Integer.valueOf(ead.getAttribute("num"));
			if (num == ad.getNumero()) {
				if (ead.getAttribute("tipo").equals(ad.getTipo())) {
					return ead;
				}
			}
		}
		return null;
	}
	
	public void exportarProjeto(Projeto proj, String path, boolean grafo) 
	throws ExcecaoExportarProjeto {
		Element raiz = (Element) documento.getFirstChild();
		NodeList pl = raiz.getElementsByTagName("projeto");
		for (int i = 0; i < pl.getLength(); i++) {
			Element p = (Element) pl.item(i);
			String pnome = p.getAttribute("nome");
			if (pnome.equals(proj.getNome())) {
				try {
					if (grafo) {
						p = inserirGrafos(p, proj);
					}
					gravarArquivo(p, path, pnome + ".xml");
				}
				catch (TransformerException e) {
					throw new ExcecaoExportarProjeto("", e);
				}
			}
		}
	}
	
	private Element inserirGrafos(Element p, Projeto proj) {
		Element e = (Element) p.cloneNode(true);
		
		NodeList cl = e.getElementsByTagName("classe");
		for (int i = 0; i < cl.getLength(); i++) {
			Element c = (Element) cl.item(i);
			Classe classe = proj.getClasse(c.getAttribute("nome"));
			NodeList ml = c.getElementsByTagName("metodo");
			for (int j = 0; j < ml.getLength(); j++) {
				Element m = (Element) ml.item(j);				
				String nome = m.getAttribute("nome");
				String desc = m.getAttribute("desc");
				Metodo metodo = classe.getMetodo(nome, desc);
				m.appendChild(criarElemento(metodo.getGrafo()));
			}
		}
		
		NodeList al = e.getElementsByTagName("aspecto");
		for (int i = 0; i < al.getLength(); i++) {
			Element a = (Element) al.item(i);
			Aspecto aspecto = proj.getAspecto(a.getAttribute("nome"));			
			NodeList ml = a.getElementsByTagName("metodo");
			for (int j = 0; j < ml.getLength(); j++) {
				Element m = (Element) ml.item(j);				
				String nome = m.getAttribute("nome");
				String desc = m.getAttribute("desc");
				Metodo metodo = aspecto.getMetodo(nome, desc);
				m.appendChild(criarElemento(metodo.getGrafo()));
			}
			NodeList adl = a.getElementsByTagName("adendo");
			for (int j = 0; j < adl.getLength(); j++) {
				Element ad = (Element) adl.item(j);				
				String tipo = ad.getAttribute("tipo");
				String num = ad.getAttribute("num");
				Adendo adendo = aspecto.getAdendo(tipo, Integer.valueOf(num));
				ad.appendChild(criarElemento(adendo.getGrafo()));
			}
		}		
		return e;
	}
	
	private Element criarElemento(AODU grafo) {
		Element	g = documento.createElement("grafo");
		for (Vertice vertice : grafo.getVertices()) {
			Element v = documento.createElement("vertice");
			String label = vertice.getLabel().toString();
			v.setAttribute("label", label);
			if (vertice instanceof VerticeTransversal) {
				VerticeTransversal vt = (VerticeTransversal) vertice;
				v.setAttribute("aspecto", vt.getAspecto());
				v.setAttribute("tipo", vt.getTipo());
				v.setAttribute("num", String.valueOf(vt.getAdNumero()));
			}
			if (grafo.isEntrada(vertice)) {
				v.setAttribute("entrada", "true");
			}
			if (grafo.isSaida(vertice)) {
				v.setAttribute("saida", "true");
			}
			for (String ins : vertice.getInstrucoes()) {
				Element i = documento.createElement("instrucao");
				i.setTextContent(ins);
				v.appendChild(i);
			}
			g.appendChild(v);
		}
		for (Aresta aresta : grafo.getArestas()) {
			Element a = documento.createElement("aresta");
			a.setAttribute("origem", aresta.getOrigem().getLabel().toString());
			a.setAttribute("destino", aresta.getDestino().getLabel().toString());
			if (aresta instanceof ArestaExcecao) {
				a.setAttribute("excecao", ((ArestaExcecao)aresta).getExcecao());
			}
			g.appendChild(a);
		}
		return g;
	}
	
	private Element criarElemento(Projeto proj) {
		Element ep = (Element) documento.createElement("projeto");
		ep.setAttribute("nome", proj.getNome());
		for (Classe c : proj.getClasses()) {
			ep.appendChild(criarElemento(c));
		}
		for (Aspecto a : proj.getAspectos()) {
			ep.appendChild(criarElemento(a));
		}
		return ep;
	}
	
	private Element criarElemento(Classe classe) {
		Element ec = (Element) documento.createElement("classe");
		ec.setAttribute("nome", classe.getNome());
		for (Metodo m : classe.getMetodos()) {
			ec.appendChild(criarElemento(m));
		}
		return ec;
	}
	
	private Element criarElemento(Aspecto aspecto) {
		Element ea = (Element) documento.createElement("aspecto");
		ea.setAttribute("nome", aspecto.getNome());
		for (Metodo m : aspecto.getMetodos()) {
			ea.appendChild(criarElemento(m));
		}
		for (Adendo a : aspecto.getAdendos()) {
			ea.appendChild(criarElemento(a));
		}
		return ea;
	}
	
	private Element criarElemento(Metodo metodo) {
		Element em = (Element) documento.createElement("metodo");
		em.setAttribute("nome", metodo.getNome());
		em.setAttribute("desc", metodo.getDesc());
		return em;
	}
	
	private Element criarElemento(Adendo adendo) {
		Element ea = (Element) documento.createElement("adendo");
		ea.setAttribute("tipo", adendo.getTipo());
		ea.setAttribute("num", String.valueOf(adendo.getNumero()));
		return ea;
	}
	
	private Element criarElemento(TesteEstrutural teste) {
		Element e = (Element) documento.createElement("teste");
		// Caso de teste: nome da classe
		String caso = teste.getCasoTeste().getNome();
		e.setAttribute("caso", caso);
		// Tipo do teste: valor da tabela interna da persistência
		String tipo = String.valueOf(getCodigo(teste));
		e.setAttribute("tipo", tipo);
		// Unidade principal: elemento filho do teste
		Element eu = (Element) documento.createElement("unidade");
		Metodo uni = ((TesteIntraMetodo)teste).getUnidade();
		// Tipo da unidade: método ou adendo
		String t = "metodo";
		if (uni instanceof Adendo) {
			t = "adendo";
		}
		eu.setAttribute("tipo", t);
		// Owner da unidade: classe ou aspecto que a contém
		eu.setAttribute("owner", uni.getClasse().getNome());
		// Nome / Desc. da unidade
		eu.setAttribute("nome", uni.getNome());
		if (t.equals("metodo")) {
			eu.setAttribute("desc", uni.getDesc());
		}
		e.appendChild(eu);
		// Critérios
		for (Criterio c : teste.getCriterios()) {
			Element ec = (Element) documento.createElement("criterio");
			String ctipo = String.valueOf(getCodigo(c));
			ec.setAttribute("tipo", ctipo);
			e.appendChild(ec);
		}
		return e;
	}
	
	public void importarProjeto(File arqXML) throws ExImportarProjeto {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(arqXML);
			NodeList plist = doc.getElementsByTagName("projeto");
			for (int i = 0; i < plist.getLength(); i++) {
				Element ep = (Element) plist.item(i);
				NodeList clist = ep.getElementsByTagName("classe");
				for (int j = 0; j < clist.getLength(); j++) {
					Element ec = (Element) clist.item(j);
					NodeList mlist = ec.getElementsByTagName("metodo");
					for (int k = 0; k < mlist.getLength(); k++) {
						Element em = (Element) mlist.item(k);
						NodeList glist = em.getElementsByTagName("grafo");
						if (glist.getLength() > 0) {
							em.removeChild(glist.item(0));							
						}						
					}
				}
				NodeList alist = ep.getElementsByTagName("aspecto");
				for (int j = 0; j < alist.getLength(); j++) {
					Element ea = (Element) alist.item(j);
					NodeList mlist = ea.getElementsByTagName("metodo");
					for (int k = 0; k < mlist.getLength(); k++) {
						Element em = (Element) mlist.item(k);
						NodeList glist = em.getElementsByTagName("grafo");
						if (glist.getLength() > 0) {
							em.removeChild(glist.item(0));							
						}						
					}
					NodeList adlist = ea.getElementsByTagName("adendo");
					for (int k = 0; k < mlist.getLength(); k++) {
						Element ead = (Element) adlist.item(k);
						NodeList glist = ead.getElementsByTagName("grafo");
						if (glist.getLength() > 0) {
							ead.removeChild(glist.item(0));							
						}						
					}
				}
				Node n = ep.cloneNode(true);
				n = documento.adoptNode(n);
				documento.getFirstChild().appendChild(n);
				salvar();
				Ambiente.inserirProjeto(criar(ep));
				GUI.instancia.atualizarArvore();
			}
		}
		catch (ParserConfigurationException e) {
			throw new ExImportarProjeto(e.getMessage());
		}
		catch (IOException e) {
			throw new ExImportarProjeto(e.getMessage());
		}
		catch (SAXException e) {
			throw new ExImportarProjeto(e.getMessage());
		}
	}
	
	public void salvar() {
		try {
			gravarArquivo(documento, ".", arquivo_xml);
		}
		catch (TransformerException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	private void gravarArquivo(Node node, String path, String nome)
	throws TransformerException {
		DOMSource source = new DOMSource(node);
		StreamResult result = new StreamResult(new File(path + "\\" + nome));
		TransformerFactory tf = TransformerFactory.newInstance();		
		Transformer t = tf.newTransformer();
		t.setOutputProperty("indent", "yes");
		t.transform(source, result);
	}
	
	public int getCodigo(TesteEstrutural t) {
		if (t instanceof TesteIntraAdendo) {
			return 1;
		}
		if (t instanceof TesteInterMetodo) {
			return 3;
		}
		if (t instanceof TesteMetodoAdendo) {
			return 4;
		}
		if (t instanceof TesteAdendoAdendo) {
			return 5;
		}
		if (t instanceof TesteAdendoMetodo) {
			return 6;
		}
		if (t instanceof TesteInterMetodoAdendo) {
			return 2;
		}
		if (t instanceof TesteIntraMetodo) {
			return 0;
		}		
		return -1;
	}
	
	public int getCodigo(Criterio c) {
		if (c instanceof TodasArestas) {
			return 0;
		}
		if (c instanceof TodasArestasDepExcecao) {
			return 1;
		}
		if (c instanceof TodasArestasIndepExcecao) {
			return 2;
		}
		if (c instanceof TodasArestasTransversais) {
			return 3;
		}
		if (c instanceof TodasDefinicoes) {
			return 4;			
		}
		if (c instanceof TodosUsos) {
			return 5;			
		}
		if (c instanceof TodosUsosDepExcecao) {
			return 6;			
		}
		if (c instanceof TodosUsosIndepExcecao) {
			return 7;
		}
		if (c instanceof TodosUsosTransversais) {
			return 8;
		}
		if (c instanceof TodosVertices) {
			return 9;			
		}
		if (c instanceof TodosVerticesDepExcecao) {
			return 10;
		}
		if (c instanceof TodosVerticesIndepExcecao) {
			return 11;
		}
		if (c instanceof TodosVerticesTransversais) {
			return 12;
		}
		return -1;
	}
	
}
