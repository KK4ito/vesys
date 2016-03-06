package u01;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.Bank;
import bank.BankDriver;

import bank.InactiveException;
import bank.OverdrawException;



public class SocketDriver implements BankDriver{
	
	private String host;
	private int port;
	private Socket socket;
	private ObjectInputStream objectinputstream;
	private ObjectOutputStream objectoutputstream;
	private Bank bank;
		

	@Override
	public void connect(String[] args) throws IOException {
		this.host = args[0];
		this.port = Integer.parseInt(args[1]);
		this.socket = new Socket(host, port);
		this.objectinputstream = new ObjectInputStream(this.socket.getInputStream());
		this.objectoutputstream = new ObjectOutputStream(this.socket.getOutputStream());
		
		System.out.println("connected...");
	}

	@Override
	public void disconnect() throws IOException {
		this.socket.close();
		bank = null;

		System.out.println("discconnected...");
	}

	@Override
	public Bank getBank() {
		return this.bank;
	}
	
	static class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();
		static int accNumber = 10000;
		
		private ObjectInputStream objectinputstream;
		private ObjectOutputStream objectoutputstream;
		
		public Bank(ObjectInputStream is, ObjectOutputStream os){
			this.objectinputstream = is;
			this.objectoutputstream = os;
		}
		
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
			if(from.isActive() == false && to.isActive()== false ) throw new InactiveException();
			if(from.getBalance() < amount || amount < 0)throw new OverdrawException();
			
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
