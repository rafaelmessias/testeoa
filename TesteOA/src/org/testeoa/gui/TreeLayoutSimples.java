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

// O layout considera o desenho do grafo em n�veis, por exemplo: um grafo com
// uma entrada e dois filhos (um m�todo com apenas um if) tem dois n�veis, o
// n�vel 0 com um v�rtice e o n�vel 1 com dois v�rtices, lado a lado.
//
// O posicionamento � feito de baixo pra cima; primeiro s�o posicionadas as
// "folhas" da �rvore, depois os "pais" s�o alinhados com o meio dos filhos. 
// Na hora de posicionar uma folha, o layout procura a pr�xima posi��o livre no
// n�vel da folha. Os espa�os verticais e horizontais entre os v�rtices s�o
// fixos.
//
// Se acontecer de um pai, depois de alinhado, se posicionar em cima de outro
// v�rtice, ele � movido pra direita at� a pr�xima posi��o livre do seu n�vel,
// levando com ele todos os seus filhos (recursivamente).
//
// As arestas n�o s�o manipuladas; o framework as desenha automaticamente (as
// vezes as arestas coincidem dando impress�es falsas na visualiza��o).

public class TreeLayoutSimples {
	
	// o grafo a ser desenhado
	JGraph grafo;
	
	// o n� de entrada do grafo
	DefaultGraphCell entrada;
	
	// mapa que define um n�vel para cada v�rtice
	/**
	 * @uml.property  name="nivel"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Integer"
	 */
	Map<DefaultGraphCell, Integer> nivel = 
			new HashMap<DefaultGraphCell, Integer>();

	// mapa que armazena os "filhos" de cada v�rtice (como uma �rvore)
	/**
	 * @uml.property  name="filhos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.List<DefaultGraphCell>"
	 */
	Map<DefaultGraphCell, List<DefaultGraphCell>> filhos =
			new HashMap<DefaultGraphCell, List<DefaultGraphCell>>();

	// mapa que relaciona uma pr�xima posi��o para cada n�vel do grafo
	/**
	 * @uml.property  name="pos"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Long"
	 */
	Map<Integer, Long> pos = new HashMap<Integer, Long>();
	
	// mapa que relaciona uma largura para cada v�rtice
	/**
	 * @uml.property  name="larg"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.lang.Integer"
	 */
	Map<DefaultGraphCell, Integer> larg =
			new HashMap<DefaultGraphCell, Integer>();
	
	// espa�o que separa os v�rtices horizontalmente
	int esp_x = 30;
	
	// espa�o que separa os v�rtices verticalmente
	int esp_y = 30;
	
	// altura fixa de cada v�rtice
	int alt = 50;
	
	public TreeLayoutSimples(JGraph g, DefaultGraphCell e) {
		grafo = g;
		entrada = e;		
	}
	
	// in�cio do processo
	public void aplicar() {
		// o n� de entrada fica no n�vel 0
		nivel.put(entrada, 0);
		// inicialmente n�o possui filhos
		filhos.put(entrada, new ArrayList<DefaultGraphCell>());
		// in�cio do algoritmo recursivo
		layout(entrada);
	}
	
	// M�todo recursivo principal do algoritmo do layout; ser� invocado para
	// todos os v�rtices do grafo, iniciando pela entrada e seguindo pelos
	// filhos.
	//
	// O posicionamento � feito de baixo pra cima, por isso o primeiro passo �
	// identificar os filhos do v�rtice atual e invocar o m�todo para eles.
	//
	// Nessa fase � importante que todos os filhos do v�rtice sejam identifica-
	// dos e marcados, depois o m�todo � invocado para cada um; isso porque se
	// os filhos n�o forem marcados anteriormente, outros v�rtices poder�o
	// reivindic�-los como filhos, antes da recurs�o voltar ao v�rtice atual.
	//
	// Quando um v�rtice n�o possui filhos, ou depois de todos os seus filhos
	// serem posicionados, o posicionamento � aplicado nele.
	private void layout(DefaultGraphCell vert) {
		//	c�lculo da largura
		JLabel jl = new JLabel(vert.getUserObject().toString());
		int lar = jl.getPreferredSize().width;
		if (lar <= 80) {
			larg.put(vert, 100);
		}
		else {
			larg.put(vert, lar + 20);
		}
		// 'port' do v�rtice atual (conecta o v�rtice �s arestas)
		DefaultPort port = (DefaultPort) vert.getChildAt(0);
		// percorre todas as arestas conectadas ao v�rtice atual
		for(Iterator ite = grafo.getModel().edges(port); ite.hasNext();) {
			DefaultEdge edge = (DefaultEdge) ite.next();			
			// se a aresta 'parte' do v�rtice atual, o alvo pode ser um filho
			if (grafo.getModel().acceptsSource(edge, port)) {
				// 'port' do v�rtice alvo
				DefaultPort port2 = (DefaultPort) edge.getTarget();
				// v�rtice alvo
				DefaultGraphCell alvo = (DefaultGraphCell) port2.getParent();				
				// se o n�vel do alvo � nulo, ele ainda n�o foi marcado; ser�
				// considerado um filho do v�rtice atual
				if (nivel.get(alvo) == null) {
					// marca o alvo com um n�vel abaixo do n�vel do atual
					nivel.put(alvo, nivel.get(vert) + 1);
					// a lista de filhos do alvo come�a vazia
					filhos.put(alvo, new ArrayList<DefaultGraphCell>());
					// adiciona o alvo na lista de filhos do atual
					filhos.get(vert).add(alvo);
				}
			}
		}
		// para cada filho do v�rtice atual, o layout � aplicado
		for(DefaultGraphCell filho : filhos.get(vert)) {
			layout(filho);
		}
		// depois de aplicado o layout em todos os filhos, o v�rtice atual �
		// posicionado
		posicionar(vert);
	}
	
	// O posicionamento de um v�rtice depende se ele possui ou n�o filhos; se o
	// v�rtice n�o possui filhos, ele ser� posicionado na pr�xima posi��o livre
	// de seu n�vel. Se ele possui, ser� calculada a posi��o exatamente no meio
	// de todos os seus filhos (que podem ser mais de dois, no caso de um
	// 'switch'.
	//
	// Caso um v�rtice pai seja posicionado antes da pr�xima posi��o livre de
	// seu n�vel, ele ser� movido para a direita junto com seus filhos, at� uma
	// posi��o v�lida.
	//
	// O 'x' e o 'y' representam as componentes horizontal e vertical do canto
	// superior-esquerdo do v�rtice.
	private void posicionar(DefaultGraphCell vert) {
		// n�vel do v�rtice
		int niv = nivel.get(vert);
		// se n�o existe uma pr�xima posi��o livre para o n�vel do v�rtice,
		// ele � o primeiro v�rtice a ser posicionado em seu n�vel. A pri-
		// meira posi��o � o espa�o horizontal a partir da borda da janela.
		if (pos.get(niv) == null) {
			pos.put(niv, (long)esp_x);
		}
		// posi��o do v�rtice, a ser calculada
		long x = 0;
		// se o v�rtice n�o possui filhos:
		if (filhos.get(vert).size() == 0) {			
			// define a posi��o do v�rtice
			x = pos.get(niv);
		}
		// se o v�rtice possui filhos:
		else {
			// primeiro filho e sua posi��o
			DefaultGraphCell f0 = filhos.get(vert).get(0);
			double f0x = GraphConstants.getBounds(f0.getAttributes()).getX();
			// �ltimo filho e sua posi��o
			DefaultGraphCell fn = 
					filhos.get(vert).get(filhos.get(vert).size() - 1);
			double fnx = GraphConstants.getBounds(fn.getAttributes()).getX();
			// c�lculo da posi��o no meio dos filhos
			long m = Math.round((fnx + larg.get(fn) - f0x) / 2);
			x = Math.round(f0x + m - larg.get(vert) / 2);
		}
		// a profundidade � calculada a partir do n�vel e do espa�o vertical
		long y = (esp_y + alt) * niv + esp_y;
		// aplica��o do posicionamento no v�rtice atual
		GraphConstants.setBounds(
				vert.getAttributes(), new Rectangle2D.Double(x, y, larg.get(vert), alt));
		// verifica se o v�rtice foi posicionado antes da pr�x. posi��o livre
		if (x < pos.get(niv)) {			
			// move o v�rtice para a pr�xima posi��o livre
			long d = pos.get(niv) - x;
			mover(vert, d);
		}
		else {
			// atualiza a pr�xima posi��o livre do n�vel do v�rtice
			pos.put(niv, x + larg.get(vert) + esp_x);
		}
	}
	
	// Move o v�rtice para a direita a dist�ncia de 'd' pixels. Invoca o m�todo
	// com o mesmo 'd' para todos os filhos.
	private void mover(DefaultGraphCell vert, long d) {
		// posi��o atual
		Rectangle2D p = GraphConstants.getBounds(vert.getAttributes());
		// nova posi��o
		p.setRect(p.getX() + d, p.getY(), p.getWidth(), p.getHeight());		
		GraphConstants.setBounds(vert.getAttributes(), p);
		// invoca o m�todo para os filhos com o mesmo 'd'
		for (DefaultGraphCell filho : filhos.get(vert)) {
			mover(filho, d);
		}
		// atualiza a pr�xima posi��o livre do n�vel do v�rtice
		pos.put(nivel.get(vert), (long)(p.getX() + larg.get(vert) + esp_x));
	}

}
