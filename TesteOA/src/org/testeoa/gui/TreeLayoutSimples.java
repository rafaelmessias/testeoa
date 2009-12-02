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

package org.testeoa.gui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

// O layout considera o desenho do grafo em níveis, por exemplo: um grafo com
// uma entrada e dois filhos (um método com apenas um if) tem dois níveis, o
// nível 0 com um vértice e o nível 1 com dois vértices, lado a lado.
//
// O posicionamento é feito de baixo pra cima; primeiro são posicionadas as
// "folhas" da árvore, depois os "pais" são alinhados com o meio dos filhos. 
// Na hora de posicionar uma folha, o layout procura a próxima posição livre no
// nível da folha. Os espaços verticais e horizontais entre os vértices são
// fixos.
//
// Se acontecer de um pai, depois de alinhado, se posicionar em cima de outro
// vértice, ele é movido pra direita até a próxima posição livre do seu nível,
// levando com ele todos os seus filhos (recursivamente).
//
// As arestas não são manipuladas; o framework as desenha automaticamente (as
// vezes as arestas coincidem dando impressões falsas na visualização).

public class TreeLayoutSimples {
	
	// o grafo a ser desenhado
	JGraph grafo;
	
	// o nó de entrada do grafo
	DefaultGraphCell entrada;
	
	// mapa que define um nível para cada vértice
	/**
	 * @uml.property  name="nivel"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Integer"
	 */
	Map<DefaultGraphCell, Integer> nivel = 
			new HashMap<DefaultGraphCell, Integer>();

	// mapa que armazena os "filhos" de cada vértice (como uma árvore)
	/**
	 * @uml.property  name="filhos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.List<DefaultGraphCell>"
	 */
	Map<DefaultGraphCell, List<DefaultGraphCell>> filhos =
			new HashMap<DefaultGraphCell, List<DefaultGraphCell>>();

	// mapa que relaciona uma próxima posição para cada nível do grafo
	/**
	 * @uml.property  name="pos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Long"
	 */
	Map<Integer, Long> pos = new HashMap<Integer, Long>();
	
	// mapa que relaciona uma largura para cada vértice
	/**
	 * @uml.property  name="larg"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Integer"
	 */
	Map<DefaultGraphCell, Integer> larg =
			new HashMap<DefaultGraphCell, Integer>();
	
	// espaço que separa os vértices horizontalmente
	int esp_x = 30;
	
	// espaço que separa os vértices verticalmente
	int esp_y = 30;
	
	// altura fixa de cada vértice
	int alt = 50;
	
	public TreeLayoutSimples(JGraph g, DefaultGraphCell e) {
		grafo = g;
		entrada = e;		
	}
	
	// início do processo
	public void aplicar() {
		// o nó de entrada fica no nível 0
		nivel.put(entrada, 0);
		// inicialmente não possui filhos
		filhos.put(entrada, new ArrayList<DefaultGraphCell>());
		// início do algoritmo recursivo
		layout(entrada);
	}
	
	// Método recursivo principal do algoritmo do layout; será invocado para
	// todos os vértices do grafo, iniciando pela entrada e seguindo pelos
	// filhos.
	//
	// O posicionamento é feito de baixo pra cima, por isso o primeiro passo é
	// identificar os filhos do vértice atual e invocar o método para eles.
	//
	// Nessa fase é importante que todos os filhos do vértice sejam identifica-
	// dos e marcados, depois o método é invocado para cada um; isso porque se
	// os filhos não forem marcados anteriormente, outros vértices poderão
	// reivindicá-los como filhos, antes da recursão voltar ao vértice atual.
	//
	// Quando um vértice não possui filhos, ou depois de todos os seus filhos
	// serem posicionados, o posicionamento é aplicado nele.
	private void layout(DefaultGraphCell vert) {
		//	cálculo da largura
		JLabel jl = new JLabel(vert.getUserObject().toString());
		int lar = jl.getPreferredSize().width;
		if (lar <= 80) {
			larg.put(vert, 100);
		}
		else {
			larg.put(vert, lar + 20);
		}
		// 'port' do vértice atual (conecta o vértice às arestas)
		DefaultPort port = (DefaultPort) vert.getChildAt(0);
		// percorre todas as arestas conectadas ao vértice atual
		for(Iterator ite = grafo.getModel().edges(port); ite.hasNext();) {
			DefaultEdge edge = (DefaultEdge) ite.next();			
			// se a aresta 'parte' do vértice atual, o alvo pode ser um filho
			if (grafo.getModel().acceptsSource(edge, port)) {
				// 'port' do vértice alvo
				DefaultPort port2 = (DefaultPort) edge.getTarget();
				// vértice alvo
				DefaultGraphCell alvo = (DefaultGraphCell) port2.getParent();				
				// se o nível do alvo é nulo, ele ainda não foi marcado; será
				// considerado um filho do vértice atual
				if (nivel.get(alvo) == null) {
					// marca o alvo com um nível abaixo do nível do atual
					nivel.put(alvo, nivel.get(vert) + 1);
					// a lista de filhos do alvo começa vazia
					filhos.put(alvo, new ArrayList<DefaultGraphCell>());
					// adiciona o alvo na lista de filhos do atual
					filhos.get(vert).add(alvo);
				}
			}
		}
		// para cada filho do vértice atual, o layout é aplicado
		for(DefaultGraphCell filho : filhos.get(vert)) {
			layout(filho);
		}
		// depois de aplicado o layout em todos os filhos, o vértice atual é
		// posicionado
		posicionar(vert);
	}
	
	// O posicionamento de um vértice depende se ele possui ou não filhos; se o
	// vértice não possui filhos, ele será posicionado na próxima posição livre
	// de seu nível. Se ele possui, será calculada a posição exatamente no meio
	// de todos os seus filhos (que podem ser mais de dois, no caso de um
	// 'switch'.
	//
	// Caso um vértice pai seja posicionado antes da próxima posição livre de
	// seu nível, ele será movido para a direita junto com seus filhos, até uma
	// posição válida.
	//
	// O 'x' e o 'y' representam as componentes horizontal e vertical do canto
	// superior-esquerdo do vértice.
	private void posicionar(DefaultGraphCell vert) {
		// nível do vértice
		int niv = nivel.get(vert);
		// se não existe uma próxima posição livre para o nível do vértice,
		// ele é o primeiro vértice a ser posicionado em seu nível. A pri-
		// meira posição é o espaço horizontal a partir da borda da janela.
		if (pos.get(niv) == null) {
			pos.put(niv, (long)esp_x);
		}
		// posição do vértice, a ser calculada
		long x = 0;
		// se o vértice não possui filhos:
		if (filhos.get(vert).size() == 0) {			
			// define a posição do vértice
			x = pos.get(niv);
		}
		// se o vértice possui filhos:
		else {
			// primeiro filho e sua posição
			DefaultGraphCell f0 = filhos.get(vert).get(0);
			double f0x = GraphConstants.getBounds(f0.getAttributes()).getX();
			// último filho e sua posição
			DefaultGraphCell fn = 
					filhos.get(vert).get(filhos.get(vert).size() - 1);
			double fnx = GraphConstants.getBounds(fn.getAttributes()).getX();
			// cálculo da posição no meio dos filhos
			long m = Math.round((fnx + larg.get(fn) - f0x) / 2);
			x = Math.round(f0x + m - larg.get(vert) / 2);
		}
		// a profundidade é calculada a partir do nível e do espaço vertical
		long y = (esp_y + alt) * niv + esp_y;
		// aplicação do posicionamento no vértice atual
		GraphConstants.setBounds(
				vert.getAttributes(), new Rectangle2D.Double(x, y, larg.get(vert), alt));
		// verifica se o vértice foi posicionado antes da próx. posição livre
		if (x < pos.get(niv)) {			
			// move o vértice para a próxima posição livre
			long d = pos.get(niv) - x;
			mover(vert, d);
		}
		else {
			// atualiza a próxima posição livre do nível do vértice
			pos.put(niv, x + larg.get(vert) + esp_x);
		}
	}
	
	// Move o vértice para a direita a distância de 'd' pixels. Invoca o método
	// com o mesmo 'd' para todos os filhos.
	private void mover(DefaultGraphCell vert, long d) {
		// posição atual
		Rectangle2D p = GraphConstants.getBounds(vert.getAttributes());
		// nova posição
		p.setRect(p.getX() + d, p.getY(), p.getWidth(), p.getHeight());		
		GraphConstants.setBounds(vert.getAttributes(), p);
		// invoca o método para os filhos com o mesmo 'd'
		for (DefaultGraphCell filho : filhos.get(vert)) {
			mover(filho, d);
		}
		// atualiza a próxima posição livre do nível do vértice
		pos.put(nivel.get(vert), (long)(p.getX() + larg.get(vert) + esp_x));
	}

}
