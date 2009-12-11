package org.testeoa.dutra;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import treemap.TMView;
import treemap.TreeMap;

//import net.bouthier.treemapSwing.TMView;
//import net.bouthier.treemapSwing.TreeMap;

public class TMViewer extends JInternalFrame {
	
	private String nome;
	private TMTreeRoot root;
	private TreeMap tMap;
	private TMView tView;
	private JTabbedPane tabbedPane;
	
	public TMViewer(String nome, TMTreeRoot root) {
		super ("", true, true, true, true);
		setSize(500,500);
		this.root = root;
		this.nome = nome;
		
		setTitle(nome);
		
		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		
		
		tMap = new TreeMap(this.root);
		tView = tMap.getView(new TMSize(), new TMDraw());
		tView.setAlgorithm("SQUARIFIED");		
		
		tView.getAlgorithm().setBorderSize(13);
		tView.getAlgorithm().setBorderOnCushion(true);
		tView.getAlgorithm().setCushion(true);
		
		tabbedPane.addTab("TreeMap", new JScrollPane(tView));
		tabbedPane.addTab("Configurações", new JScrollPane(tView.getAlgorithm().getConfiguringView()));
		
		getContentPane().add(tabbedPane);
		
		
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public TMTreeRoot getRoot() {
		return root;
	}
	
	public void setRoot(TMTreeRoot root) {
		this.root = root;
	}
	

}
