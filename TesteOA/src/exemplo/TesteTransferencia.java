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

import javax.swing.JOptionPane;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import exemplo.excecoes.ExcecaoContaInexistente;

/**
 * @author    Rafael
 */
public class TesteTransferencia {
	
	Terminal terminal;
	Conta conta1;
	Conta conta2;
	
	@Before
	public void preparacao() throws ExcecaoContaInexistente {
		try {
		terminal = new Terminal();
		conta1 = new Conta("Rafael M. Martins", 123);		
		conta1.saldo = 130.0f;
		terminal.inserirConta(conta1);
		conta2 = new Conta("Fulano da Silva", 456);
		conta2.saldo = 12.0f;
		terminal.inserirConta(conta2);
		}
		catch(Throwable t) {
			JOptionPane.showMessageDialog(null, t);
		}
	}
	
	@Test
	public void transferenciaCorreta() {
		
		float valor = 80.0f;
		terminal.transferencia(conta1.getNumero(), conta2.getNumero(), valor);
		// verifica se os valores foram modificados corretamente
		Assert.assertEquals(conta1.getSaldo(), 50.0f, 0.0f);
		Assert.assertEquals(conta2.getSaldo(), 92.0f, 0.0f);
	}
	
	@Test
	public void transferenciaSaldoInsuficiente() {
		// valor a ser transferido e maior que o saldo da conta de origem
		float valor = 150.0f;
		terminal.transferencia(conta1.getNumero(), conta2.getNumero(), valor);
		// verifica se os valores continuam os mesmos
		Assert.assertEquals(conta1.getSaldo(), 130.0f, 0.0f);
		Assert.assertEquals(conta2.getSaldo(), 12.0f, 0.0f);
	}
	
	@Test
	public void transferenciaContaOrigemInexistente() {
		float valor = 100.0f;
		int numConta1 = 789;
		terminal.transferencia(numConta1, conta2.getNumero(), valor);
		// verifica se o saldo da conta destino nao se modificou
		Assert.assertEquals(conta2.getSaldo(), 12.0f, 0.0f);
	}
	
	@Test
	public void transferenciaContaDestinoInexistente() {
		float valor = 100.0f;
		int numConta2 = 789;
		terminal.transferencia(conta1.getNumero(), numConta2, valor);
		// verifica se o saldo da conta de origem n�o se modificou
		Assert.assertEquals(conta1.getSaldo(), 130.0f, 0.0f);
	}
	
	@Test
	public void transferenciaValorNegativo() {
		float valor = -10.0f;
		terminal.transferencia(conta1.getNumero(), conta2.getNumero(), valor);
		// verifica se os valores foram modificados corretamente
		Assert.assertEquals(conta1.getSaldo(), 130.0f, 0.0f);
		Assert.assertEquals(conta2.getSaldo(), 12.0f, 0.0f);
	}

}
