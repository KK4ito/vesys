/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.local;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.InactiveException;
import bank.OverdrawException;

public class Driver implements bank.BankDriver, Serializable{
	private Bank bank = null;

	@Override
	public void connect(String[] args) {
		bank = new Bank();
		System.out.println("connected...");
	}

	@Override
	public void disconnect() {
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		if(bank == null) bank = new Bank();
		return bank;
	}

	public static class Bank implements bank.Bank, Serializable {

		private final Map<String, Account> accounts = new HashMap<>();
		static int accNumber = 10000;
		@Override
		public Set<String> getAccountNumbers() {
			Set<String> lstAccountNumbers = new HashSet<>();
			for(Account a : this.accounts.values()){
				if(a.isActive()){
					lstAccountNumbers.add(a.number);
				}
			}
			return lstAccountNumbers;
		}

		@Override
		public String createAccount(String owner) {
			Account a = new Account(owner, String.valueOf(accNumber));
			accNumber++;
			
			this.accounts.put(a.number, a);
			return a.number;
		}

		@Override
		public boolean closeAccount(String number) {
			if(this.accounts.get(number) == null){
				return false;
			}
			else if(this.accounts.get(number).getBalance() != 0 || !this.accounts.get(number).isActive()){
				return false;
			}
			Account a = this.accounts.get(number);
			a.active = false;
			return true;
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException {
			if(from.isActive() == false || to.isActive()== false ) throw new InactiveException();
			if(from.getBalance() < amount || amount < 0)throw new OverdrawException();
			
			from.withdraw(amount);
			to.deposit(amount);
		}

	}

	static class Account implements bank.Account, Serializable{
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		Account(String owner, String number) {
			this.owner = owner;
			this.number = number;
		}

		@Override
		public double getBalance() {
			return balance;
		}

		@Override
		public String getOwner() {
			return owner;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			if(this.active == false || amount < 0) throw new InactiveException();
		
			this.balance += amount;
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {
			if(this.active == false || amount < 0) throw new InactiveException();
			if(this.balance < amount)throw new OverdrawException();
			this.balance -= amount;
		}

	}

}