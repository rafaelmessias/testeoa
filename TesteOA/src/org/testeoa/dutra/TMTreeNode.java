package org.testeoa.dutra;

import java.util.Enumeration;
import java.util.Vector;

import treemap.TMNode;
import treemap.TMUpdater;

public class TMTreeNode implements TMNode {

	String nome;
	String desc;
	boolean isMetodo;
	
	public TMTreeNode(String nome, String desc, boolean isMetodo) {
		this.nome = nome;
		this.desc = desc;
		this.isMetodo = isMetodo;
	}
	
	@Override
	public Enumeration children() {
		return new Vector<TMNode>().elements();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public void setUpdater(TMUpdater arg0) {
		// TODO criar o Updater do TMTreeNode
		
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
	
		
}
