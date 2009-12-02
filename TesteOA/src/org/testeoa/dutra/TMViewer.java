package org.testeoa.dutra;

import javax.swing.JInternalFrame;

import treemap.TMView;
import treemap.TreeMap;

public class TMViewer extends JInternalFrame {
	
	private String nome;
	private TMTreeRoot root;
	private TreeMap tMap;
	private TMView tView;
	
	public TMViewer(String nome, TMTreeRoot root) {
		super ("", true, true, true, true);
		setSize(500,500);
		this.root = root;
		this.nome = nome;
		
		tMap = new TreeMap(this.root);
		tView = tMap.getView(new TMSize(), new TMDraw());
		setContentPane(tView);
		pack();
		tView.setVisible(true);
		tView.setAlgorithm("SQUARIFIED");
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
