package u01_OLD;

import java.io.*;
import java.net.Socket;
import java.util.Set;

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
		this.host = "localhost";
		this.port = Integer.parseInt(args[0]);
		this.socket = new Socket(host, port);
		this.objectinputstream = new ObjectInputStream(this.socket.getInputStream());
		this.objectoutputstream = new ObjectOutputStream(this.socket.getOutputStream());
		bank = new Bank(this.objectinputstream, this.objectoutputstream);
		System.out.println("connected... at port " + this.port);
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
		
		private ObjectInputStream objectinputstream;
		private ObjectOutputStream objectoutputstream;
		
		public Bank(ObjectInputStream is, ObjectOutputStream os){
			this.objectinputstream = is;
			this.objectoutputstream = os;
		}
		
		@Override
		public Set<String> getAccountNumbers() throws IOException{

			SocketCommands cmd = new SocketCommands(SocketCommands.Command.getAccountNumbers, "");
			objectoutputstream.writeObject(cmd);

			try {
				return (Set<String>)objectinputstream.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
			
		}

		@Override
		public String createAccount(String owner) throws IOException{
			
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.createAccount, owner);
			objectoutputstream.writeObject(cmd);
			
			try {
				return (String)objectinputstream.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
			
		}

		@Override
		public boolean closeAccount(String number) throws IOException{
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.closeAccount, number);
			objectoutputstream.writeObject(cmd);
			
			try {
				return (boolean)objectinputstream.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
		}

		@Override
		public bank.Account getAccount(String number) throws IOException{
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.getAccount, number);
			objectoutputstream.writeObject(cmd);
			
			try {
				return (Account)objectinputstream.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
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
		private ObjectOutputStream ooStream;
		private ObjectInputStream oiStream;

		Account(String number, ObjectOutputStream oo, ObjectInputStream oi) {
			this.ooStream = oo;
			this.oiStream = oi;
			this.number = number;
		}

		@Override
		public double getBalance() throws IOException {
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.getBalance, this.number);
			ooStream.writeObject(cmd);
			
			try {
				return (double)oiStream.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
		}

		@Override
		public String getOwner() throws IOException{
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.getOwner, this.number);
			ooStream.writeObject(cmd);
			
			try {
				return (String)oiStream.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() throws IOException{
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.isActive, this.number);
			ooStream.writeObject(cmd);
			
			try {
				return (boolean)oiStream.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
		}

		@Override
		public void deposit(double amount) throws InactiveException, IOException {
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.deposit, amount+"");
			ooStream.writeObject(cmd);
			
			try {
				if(!((boolean)oiStream.readObject())){
					throw new IOException();
				}
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
		}

		@Override
		public void withdraw(double amount) throws IOException, InactiveException, OverdrawException {
			SocketCommands cmd = new SocketCommands(SocketCommands.Command.withdraw, amount+"");
			ooStream.writeObject(cmd);
			
			try {
				if(!((boolean)oiStream.readObject())){
					throw new IOException();
				}
			} catch (ClassNotFoundException e) {
				throw new IOException();
			}
		}

	}

}
