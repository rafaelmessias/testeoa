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


/**
 * @author  Rafael
 */
public class Conta {
	
	/**
	 * @uml.property  name="titular"
	 */
	private String titular;
	
	/**
	 * @uml.property  name="numero"
	 */
	private int numero;
	
	/**
	 * @uml.property  name="saldo"
	 */
	public float saldo = 0.0f;
	
	public Conta(String nomeCliente, int numero) {
		this.titular = nomeCliente;
		this.numero = numero;
	}
	
	/**
	 * @return  the titular
	 * @uml.property  name="titular"
	 */
	public String getTitular() {
		return titular;
	}
	
	/**
	 * @return  the numero
	 * @uml.property  name="numero"
	 */
	public int getNumero() {
		return numero;
	}
	
	/**
	 * @return  the saldo
	 * @uml.property  name="saldo"
	 */
	public float getSaldo() {
		return saldo;
	}
	
	public void saque(float valor) {
		saldo -= valor;
	}
	
	public void deposito(float valor) {
		saldo += valor;
	}

}
