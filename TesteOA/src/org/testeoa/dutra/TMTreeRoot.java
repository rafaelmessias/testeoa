package org.testeoa.dutra;

import java.util.Enumeration;
import java.util.Vector;

import treemap.TMNode;
import treemap.TMUpdater;

//import net.bouthier.treemapSwing.TMNode;
//import net.bouthier.treemapSwing.TMUpdater;

public class TMTreeRoot implements TMNode {

	Vector<TMTreePackage> children;
	String nome;

	public TMTreeRoot(String nome) {
		children = new Vector<TMTreePackage>();
		this.nome = nome;
	}

	@Override
	public Enumeration children() {
		return this.children.elements();
	}

	@Override
	public boolean isLeaf() {
		return false; // Afinal, é o root
	}

	@Override
	public void setUpdater(TMUpdater arg0) {
		// TODO criar o Updater do TMTreeRoot

	}

	public TMTreePackage addPackage(String nome) {
		for (TMTreePackage tPackage : this.children) {
			if (tPackage.getNome().equals(nome)) {
				return tPackage;
			}
		}
		TMTreePackage tPackage = new TMTreePackage(nome);
		this.children.add(tPackage);
		return tPackage;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

}
