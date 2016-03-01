/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.local;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.InactiveException;
import bank.OverdrawException;

public class Driver implements bank.BankDriver {
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
		return bank;
	}

	static class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();

		@Override
		public Set<String> getAccountNumbers() {
			Set<String> lstAccountNumbers = new HashSet<>();
			for(Account a : this.accounts.values()){
				lstAccountNumbers.add(a.number);
			}
			return lstAccountNumbers;
		}

		@Override
		public String createAccount(String owner) {
			//TODO how to get account number for new accounts?
			Account a = new Account(owner);
			this.accounts.put(owner, a);
			return owner;
		}

		@Override
		public boolean closeAccount(String number) {
			if(this.accounts.get(number) == null){
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
			if(from.isActive() == false && to.isActive()== false ) throw new InactiveException();
			if(from.getBalance() < amount)throw new OverdrawException();
			
			from.withdraw(amount);
			to.deposit(amount);
		}

	}

	static class Account implements bank.Account {
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
			if(this.active == false) throw new InactiveException();
			this.balance += amount;
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException {
			if(this.active == false) throw new InactiveException();
			if(this.balance < amount)throw new OverdrawException();
			this.balance -= amount;
		}

	}

}