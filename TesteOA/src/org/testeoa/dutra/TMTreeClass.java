package org.testeoa.dutra;

import java.util.Enumeration;
import java.util.Vector;

import treemap.TMNode;
import treemap.TMUpdater;

//import net.bouthier.treemapSwing.TMNode;
//import net.bouthier.treemapSwing.TMUpdater;

public class TMTreeClass implements TMNode {

	Vector<TMTreeNode> children;
	String nome;
	boolean isClass;
	
	public TMTreeClass(String nome, boolean isClass) {
		children = new Vector<TMTreeNode>();
		this.isClass = isClass;
		this.nome = nome;
	}
	
	@Override
	public Enumeration children() {
		return this.children.elements();
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public void setUpdater(TMUpdater arg0) {
		// TODO criar o Updater do TMTreeClass
		
	}
	
	public TMTreeNode addNode(String nome, String desc, boolean isMetodo) {
		for (TMTreeNode tNode : this.children) {
			if (tNode.getNome().equals(nome) && tNode.getDesc().equals(desc)) {
				return tNode;
			}
		}
		TMTreeNode tNode = new TMTreeNode(nome, desc, isMetodo);
		this.children.add(tNode);
		return tNode;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescription() {
		return "Classe: " + nome.substring(0, nome.indexOf(".class"));
	}

}
