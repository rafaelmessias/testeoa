package org.testeoa.dutra;

import java.util.Enumeration;
import java.util.Vector;

import treemap.TMNode;
import treemap.TMUpdater;

//import net.bouthier.treemapSwing.TMNode;
//import net.bouthier.treemapSwing.TMUpdater;

public class TMTreePackage implements TMNode {

	Vector<TMTreeClass> children;
	String nome;

	public TMTreePackage(String nome) {
		children = new Vector<TMTreeClass>();
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
		// TODO criar o Updater do TMTreePackage

	}

	public TMTreeClass addClass(String nome, boolean isClass) {
		for (TMTreeClass tClass : this.children) {
			if (tClass.getNome().equals(nome)) {
				return tClass;
			}
		}
		TMTreeClass tClass = new TMTreeClass(nome, isClass);
		this.children.add(tClass);
		return tClass;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescription() {
		return "Pacote: " + nome.replace('/', '.').substring(0, nome.length()-1);
	}

}
