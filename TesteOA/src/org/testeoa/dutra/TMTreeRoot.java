package org.testeoa.dutra;

import java.util.Enumeration;
import java.util.Vector;

import treemap.TMNode;
import treemap.TMUpdater;

public class TMTreeRoot implements TMNode {

	Vector<TMTreePackage> children;

	public TMTreeRoot() {
		children = new Vector<TMTreePackage>();
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

}
