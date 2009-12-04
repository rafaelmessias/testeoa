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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.tree.MutableTreeNode;

import org.testeoa.dutra.LeoLoader;
import org.testeoa.dutra.TMTreeHandler;
import org.testeoa.dutra.TMTreeRoot;
import org.testeoa.dutra.TMViewer;
import org.testeoa.dutra.Unidade;
import org.testeoa.estatica.AnaliseEstatica;
import org.testeoa.excecoes.ExAnaliseEstatica;
import org.testeoa.excecoes.ExProjeto;
import org.testeoa.gui.arvore.ArvoreProjetos;
import org.testeoa.gui.arvore.NodeAP;
import org.testeoa.gui.arvore.NodeAdendo;
import org.testeoa.gui.arvore.NodeAspecto;
import org.testeoa.gui.arvore.NodeClasse;
import org.testeoa.gui.arvore.NodeMetodo;
import org.testeoa.gui.arvore.NodeProjeto;
import org.testeoa.gui.arvore.NodeTeste;
import org.testeoa.modelo.Adendo;
import org.testeoa.modelo.Ambiente;
import org.testeoa.modelo.Aspecto;
import org.testeoa.modelo.Classe;
import org.testeoa.modelo.Metodo;
import org.testeoa.modelo.Projeto;
import org.testeoa.teste.TesteEstrutural;

/**
 * @author Rafael
 */
public class GUI extends JFrame {
	public static final long serialVersionUID = 1L;

	public static GUI instancia;

	ArvoreProjetos arvore;
	public static JDesktopPane conteudo;
	JSplitPane split1;
	static JTextArea mensagens = new JTextArea();

	JToolBar barraSup;
	JButton btAbProj;
	JButton btFeProj;
	JButton btImProj;
	JButton btExProj;
	JButton btNoCla;
	JButton btNoAsp;
	JButton btNoUni;
	JButton btNoTeste;
	JButton btVis;
	JButton btExec;
	JButton btRem;
	JButton btImpLeo;
	
	TMTreeRoot TMRoot = new TMTreeRoot();

	public GUI() {
		super("TesteOA");
		instancia = this;
	}

	public void iniciar() {

		// Árvore de Projetos
		arvore = new ArvoreProjetos();
		JScrollPane sp = new JScrollPane(arvore);
		sp.setPreferredSize(new Dimension(150, 0));
		JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(new TitledBorder("Projetos de Teste"));
		p1.add(sp);

		// Barra de Ferramentas
		barraSup = new JToolBar("nome");

		JButton btNoProj = new JButton(new ImageIcon("fig/np.png"));
		btNoProj.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		btNoProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				arvore.novoProjeto();
			}
		});
		btNoProj.setToolTipText("Novo Projeto");
		barraSup.add(btNoProj);

		btImProj = new JButton(new ImageIcon("fig/ip.png"));
		btImProj.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		btImProj.setToolTipText("Importar Projeto");
		btImProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				arvore.importarProjeto();
			}
		});
		barraSup.add(btImProj);
		btImProj.setEnabled(true);

		btExProj = new JButton(new ImageIcon("fig/ep.png"));
		btExProj.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		barraSup.add(btExProj);
		btExProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((NodeProjeto) arvore.getSelecionado()).exportar();
			}
		});
		btExProj.setToolTipText("Exportar Projeto");
		btExProj.setEnabled(false);

		btAbProj = new JButton(new ImageIcon("fig/pas.png"));
		btAbProj.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		barraSup.add(btAbProj);
		btAbProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((NodeProjeto) arvore.getSelecionado()).abrir();
			}
		});
		btAbProj.setToolTipText("Abrir Projeto");
		btAbProj.setEnabled(false);

		btFeProj = new JButton(new ImageIcon("fig/pfs.png"));
		btFeProj.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		barraSup.add(btFeProj);
		btFeProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((NodeProjeto) arvore.getSelecionado()).fechar();
			}
		});
		btFeProj.setToolTipText("Fechar Projeto");
		btFeProj.setEnabled(false);

		btNoCla = new JButton(new ImageIcon("fig/nc.png"));
		btNoCla.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		barraSup.add(btNoCla);
		btNoCla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((NodeProjeto) arvore.getSelecionado()).novaClasse();
			}
		});
		btNoCla.setToolTipText("Nova Classe");
		btNoCla.setEnabled(false);

		btNoAsp = new JButton(new ImageIcon("fig/na.png"));
		btNoAsp.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		barraSup.add(btNoAsp);
		btNoAsp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((NodeProjeto) arvore.getSelecionado()).novoAspecto();
			}
		});
		btNoAsp.setToolTipText("Novo Aspecto");
		btNoAsp.setEnabled(false);

		btNoTeste = new JButton(new ImageIcon("fig/nt.png"));
		btNoTeste.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		barraSup.add(btNoTeste);
		btNoTeste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((NodeProjeto) arvore.getSelecionado()).novoTeste();
			}
		});
		btNoTeste.setToolTipText("Novo Teste");
		btNoTeste.setEnabled(false);

		btNoUni = new JButton(new ImageIcon("fig/nu.png"));
		btNoUni.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		btNoUni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NodeAP sel = arvore.getSelecionado();
				if (sel instanceof NodeClasse) {
					((NodeClasse) sel).novaUnidade();
				} else if (sel instanceof NodeAspecto) {
					((NodeAspecto) sel).novaUnidade();
				}
			}
		});

		barraSup.add(btNoUni);
		btNoUni.setToolTipText("Nova Unidade");
		btNoUni.setEnabled(false);

		btVis = new JButton(new ImageIcon("fig/v.png"));
		btVis.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		btVis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NodeAP sel = arvore.getSelecionado();
				if (sel instanceof NodeMetodo) {
					((NodeMetodo) sel).visualizar();
				} else if (sel instanceof NodeAdendo) {
					((NodeAdendo) sel).visualizar();
				} else if (sel instanceof NodeTeste) {
					((NodeTeste) sel).visualizar();
				}
			}
		});
		barraSup.add(btVis);
		btVis.setToolTipText("Visualizar");
		btVis.setEnabled(false);

		btExec = new JButton(new ImageIcon("fig/ex.png"));
		btExec.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		btExec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				((NodeTeste) arvore.getSelecionado()).executar();
			}
		});
		barraSup.add(btExec);
		btExec.setToolTipText("Executar Teste");
		btExec.setEnabled(false);

		btRem = new JButton(new ImageIcon("fig/x.png"));
		btRem.setUI(new BasicButtonUI() {
			@Override
			public Dimension getPreferredSize(JComponent arg0) {
				return new Dimension(30, 30);
			}
		});
		btRem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				arvore.getSelecionado().remover();
			}
		});
		barraSup.add(btRem);
		btRem.setToolTipText("Remover");
		btRem.setEnabled(false);

		btImpLeo = new JButton("imp leo");
		btImpLeo.setToolTipText("Importa Projeto Leo");
		btImpLeo.setEnabled(true);
		btImpLeo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doLeo();
			}
		});

		barraSup.add(btImpLeo);

		add(barraSup, BorderLayout.NORTH);

		// DesktopPane
		conteudo = new JDesktopPane();

		split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, conteudo);
		// split1.setPreferredSize(new Dimension(0, 500));
		add(split1);

		// Mensagens
		mensagens.setRows(5);
		JPanel pm = new JPanel(new BorderLayout());
		pm.add(new JScrollPane(mensagens));
		pm.setBorder(new TitledBorder("Mensagens"));
		add(pm, BorderLayout.SOUTH);

		// JSplitPane split2 =
		// new JSplitPane(JSplitPane.VERTICAL_SPLIT, split1, pm);
		// add(split2);

		// Ajustes Finais
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void doLeo() {

		String title = "Novo Projeto";
		int mType = JOptionPane.QUESTION_MESSAGE;
		String nome = JOptionPane.showInputDialog(GUI.instancia,
				"Nome do Projeto: ", title, mType);
		if (nome != null) {

			LeoLoader loader = new LeoLoader();
			loader.loadJar();

			// Modelo
			Projeto p = new Projeto(nome);
			Ambiente.inserirProjeto(p);
			// GUI
			NodeProjeto np = new NodeProjeto(arvore, p);
			MutableTreeNode root = (MutableTreeNode) arvore.getModel()
					.getRoot();
			arvore.getModel().insertNodeInto(np, root, root.getChildCount());
			// Bug?! o primeiro projeto não aparece na tela...
			if (root.getChildCount() == 1) {
				arvore.getModel().setRoot(root);
			}
			np.abrir();

			Classe tmpClasse = null;
			Aspecto tmpAsp = null;
			boolean isClasse = false;
			boolean isAspecto = false;
			String tPacote = null;
			String tClasse = null;
			String tMetodo = null;
			String tMetodoDesc = null;

			ArrayList<Unidade> lista = loader.getLista();
			for (int cont = 0; cont < lista.size(); cont++) {
				isClasse = false;
				isAspecto = false;
				try {
					tmpClasse = AnaliseEstatica.lerClasse(lista.get(cont).getURL());
					for (Metodo m : tmpClasse.getMetodos()) {
						tPacote = lista.get(cont).getPacote();
						tClasse = lista.get(cont).getNome();
						tMetodo = m.getNome();
						tMetodoDesc = m.getDesc();
						TMRoot.addPackage(tPacote).addClass(tClasse, true).addNode(tMetodo, tMetodoDesc, true);	
					}
					isClasse = true;
				} catch (ExAnaliseEstatica eclass) {
					try {
						tmpAsp = AnaliseEstatica.lerAspecto(lista.get(cont)
								.getURL());
						for (Metodo m : tmpAsp.getMetodos()) {
							tPacote = lista.get(cont).getPacote();
							tClasse = lista.get(cont).getNome();
							tMetodo = m.getNome();
							tMetodoDesc = m.getDesc();
							TMRoot.addPackage(tPacote).addClass(tClasse, false).addNode(tMetodo, tMetodoDesc, true);	
						}
						for (Adendo a : tmpAsp.getAdendos()) {
							tPacote = lista.get(cont).getPacote();
							tClasse = lista.get(cont).getNome();
							tMetodo = a.getNome();
							tMetodoDesc = a.getDesc();
							TMRoot.addPackage(tPacote).addClass(tClasse, false).addNode(tMetodo, tMetodoDesc, false);	
						}
						isAspecto = true;
					} catch (ExAnaliseEstatica easp) {
						easp.printStackTrace();
					}
				}
				try {
					if (isClasse) {
						np.getUserObject().inserir(tmpClasse);
					} else if (isAspecto) {
						np.getUserObject().inserir(tmpAsp);
					} else {
						System.out.println("erro ao inserir classe aos objetos do NP");
					}

				} catch (ExProjeto e) {
					e.printStackTrace();
				}
				// GUI
				if (isClasse) {
					np.inserirNodeClasse(tmpClasse);
				} else if (isAspecto) {
					np.inserirNodeAspecto(tmpAsp);
				} else {
					System.out.println("erro ao inserir nodo no NP");
				}

			}
			np.expandir();
			TMViewer tViewer = new TMViewer(nome, TMRoot);
			conteudo.add(tViewer);
			tViewer.setVisible(true);
		}

	}

	public void atualizarBarra(Object sel) {
		btAbProj.setEnabled(false);
		btFeProj.setEnabled(false);
		btExProj.setEnabled(false);
		btNoCla.setEnabled(false);
		btNoAsp.setEnabled(false);
		btNoTeste.setEnabled(false);
		btNoUni.setEnabled(false);
		btVis.setEnabled(false);
		btExec.setEnabled(false);
		btRem.setEnabled(false);
		if (sel instanceof Projeto) {
			btExProj.setEnabled(true);
			btRem.setEnabled(true);
			Projeto proj = (Projeto) sel;
			if (proj.isAberto()) {
				btFeProj.setEnabled(true);
				btNoCla.setEnabled(true);
				btNoAsp.setEnabled(true);
				btNoTeste.setEnabled(true);
			} else {
				btAbProj.setEnabled(true);
			}
		}
		if (sel instanceof Classe) {
			btNoUni.setEnabled(true);
			btRem.setEnabled(true);
		}
		if (sel instanceof Metodo || sel instanceof Adendo) {
			btVis.setEnabled(true);
			btRem.setEnabled(true);
		}
		if (sel instanceof TesteEstrutural) {
			btVis.setEnabled(true);
			btRem.setEnabled(true);
			TesteEstrutural t = (TesteEstrutural) sel;
			if (t.isAtivo()) {
				btExec.setEnabled(true);
			}
		}
	}

	void executarTeste(TesteEstrutural t) {
		t.executarTeste();
	}

	void removerProjeto(Projeto proj) {
		Ambiente.removerProjeto(proj);
	}

	//	
	// void exibirSelecionado() {
	// Object obj = getSelecionado();
	// JPanel panel = panels.get(obj);
	// if (panel != null) {
	// String nome = panel.getName();
	// if (tabPane.indexOfTab(nome) == -1) {
	// tabPane.addTab(nome, panel);
	// }
	// }
	// }
	//	

	public void exibirMensagem(String mensagem) {
		String titulo = "Atenção!";
		int tipo = JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
	}

	public void exibirMensagemErro(Exception e) {
		String titulo = "Erro!";
		String mensagem = e.getMessage();
		int tipo = JOptionPane.ERROR_MESSAGE;
		JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
	}

	public static void main(String[] args) throws Exception {
		Ambiente.iniciar();
		new GUI().iniciar();
	}

	void exportarProjeto() {
		// Projeto proj = (Projeto) getSelecionado();
		// JFileChooser jfc = new JFileChooser(".");
		// jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// jfc.setDialogTitle("Selecione o diretório");
		// jfc.showDialog(this, "Selecionar");
		// String path = jfc.getSelectedFile().getAbsolutePath();
		// try {
		// PersistenciaXML.exportarProjeto(proj, path, true);
		// }
		// catch (ExcecaoExportarProjeto e) {
		// gui.exibirMensagemErro("erro", e);
		// }
	}

	public static void registrar(String m) {
		mensagens.append(m + "\n");
	}

	public void atualizarArvore() {
		for (Projeto p : Ambiente.getProjetos()) {
			boolean existe = false;
			MutableTreeNode root = (MutableTreeNode) arvore.getModel()
					.getRoot();
			for (int i = 0; i < arvore.getModel().getChildCount(root); i++) {
				if (arvore.getModel().getChild(root, i) instanceof NodeProjeto) {
					JOptionPane.showMessageDialog(null, "pinga!");
					NodeProjeto np = (NodeProjeto) arvore.getModel().getChild(
							root, i);
					if (np.getUserObject().getNome().equals(p.getNome())) {
						existe = true;
					}
				}
			}
			if (!existe) {
				// GUI
				NodeProjeto np = new NodeProjeto(arvore, p);
				arvore.getModel()
						.insertNodeInto(np, root, root.getChildCount());
				// Bug?! o primeiro projeto não aparece na tela...
				if (root.getChildCount() == 1) {
					arvore.getModel().setRoot(root);
				}
			}
		}
	}

}
