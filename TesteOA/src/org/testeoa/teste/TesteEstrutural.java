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

package org.testeoa.teste;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.runner.JUnitCore;
import org.testeoa.criterios.Criterio;
import org.testeoa.dinamica.AnaliseDinamica;
import org.testeoa.grafo.AODU;
import org.testeoa.modelo.Projeto;

public abstract class TesteEstrutural {
	
	Projeto projeto;
	
	private Set<Criterio> criterios = new LinkedHashSet<Criterio>();
	
	ResultE resultE = new ResultE();
	
	boolean executado = false;
	
	boolean ativo = true;
	
	private CasoTeste caso;
	
	public TesteEstrutural(CasoTeste c) {
		caso = c;
	}
	
	public CasoTeste getCasoTeste() {
		return caso;
	}
	
	@Override
	public String toString() {
		return caso.getNomeCurto();
	}
	
	public Projeto getProjeto() {
		return projeto;
	}
	
	public void setProjeto(Projeto p) {
		projeto = p;
	}

	public void executarTeste() {
		if (ativo) {
			reiniciar();
			AnaliseDinamica.reiniciar();
			registrarUnidades();
			JUnitCore runner = new JUnitCore();
			runner.addListener(getResultado().createListener());
			runner.run(caso.getCodigo());
			resultE.setCaminho(AnaliseDinamica.getCaminho());
			verificarCriterios();
			executado = true;
		}
	}
	
	// Grafo
	
	public abstract AODU getGrafo();
	
	// Critérios de Abrangência
	
	public Criterio[] getCriterios() {
		return criterios.toArray(new Criterio[0]);
	}
	
	void inserir(Criterio c) {
		criterios.add(c);
		resultE.novoResultado(c, 0.0f);
	}
	
	abstract void registrarUnidades();
	
	private void verificarCriterios() {
		for (Criterio c : getCriterios()) {
			resultE.novoResultado(c, c.verificar(resultE.getCaminho()));
		}
	}
	
	// Resultado
	
	public ResultE getResultado() {
		return resultE;
	}
	
	public abstract String getTipo();
	
	public boolean isExecutado() {
		return executado;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
	
	public void setAtivo(boolean a) {
		ativo = a;
		if (!a) {
			reiniciar();
		}
	}
	
	void reiniciar() {		
		resultE = new ResultE();
		for (Criterio c : criterios) {
			resultE.novoResultado(c, 0.0f);
		}
		executado = false;
	}

}
