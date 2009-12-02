package org.testeoa.dutra;

public class Unidade {
	
	private boolean classe;
	private String pacote;
	private String nome;
	private String URL;
	
	public Unidade(String pacote, String nome, String URL) {
		this.pacote = pacote;
		this.nome = nome;
		this.URL = URL;
	}
	
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getURL() {
		return URL;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
	public void setPacote(String pacote) {
		this.pacote = pacote;
	}
	public String getPacote() {
		return pacote;
	}
	public void setClasse(boolean classe) {
		this.classe = classe;
	}
	public boolean isClasse() {
		return classe;
	}
	
	

}
