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

package exemplo;

import java.util.HashSet;
import java.util.Set;

import exemplo.excecoes.ExcecaoContaInexistente;
import exemplo.excecoes.ExcecaoSaldoInsuficiente;


public class Terminal {
	
	private Set<Conta> contas;
	
	public Terminal() {
		contas = new HashSet<Conta>();
	}
	
	public void transferencia(int numOrigem, int numDestino, float valor) {
		try {
			Conta contaOrigem = getConta(numOrigem);
			Conta contaDestino = getConta(numDestino);
			if (contaOrigem.getSaldo() >= valor) {
				contaOrigem.saque(valor);
				contaDestino.deposito(valor);
			}
			else {
				throw new ExcecaoSaldoInsuficiente(
						contaOrigem.getNumero(), valor);
			}
		}
		catch (ExcecaoContaInexistente e) {
			System.out.println(e.getMessage());
		}
		catch (ExcecaoSaldoInsuficiente e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void inserirConta(Conta conta) {
		contas.add(conta);
	}
	
	public void removerConta(int numero) {
		try {
			contas.remove(getConta(numero));
		}
		catch (ExcecaoContaInexistente e) {
			// Nada acontece.
		}
	}
	
	public Conta getConta(int numero) throws ExcecaoContaInexistente {
		for(Conta conta : contas) {
			if (conta.getNumero() == numero) {
				return conta;
			}
		}
		throw new ExcecaoContaInexistente(numero);
	}

}
