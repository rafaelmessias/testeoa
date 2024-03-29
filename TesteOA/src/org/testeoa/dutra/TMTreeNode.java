package org.testeoa.dutra;

import java.util.Enumeration;
import java.util.Vector;

import treemap.TMNode;
import treemap.TMUpdater;

//import net.bouthier.treemapSwing.TMNode;
//import net.bouthier.treemapSwing.TMUpdater;

public class TMTreeNode implements TMNode {

	String nome;
	String desc;
	boolean isMetodo;
	float size;

	public TMTreeNode(String nome, String desc, boolean isMetodo) {
		this.nome = nome;
		this.desc = desc;
		this.isMetodo = isMetodo;
		this.size = 1;
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
	
	public void setSize(float size) {
		this.size = size;
	}
	
	public float getSize() {
		return size;
	}

	public void incSize() {
		this.size++;
	}

}
