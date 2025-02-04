package bank.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

import bank.InactiveException;
import bank.OverdrawException;
import bank.sockets.commands.CloseAccountCmd;
import bank.sockets.commands.CreateAccountCmd;
import bank.sockets.commands.DepositCmd;
import bank.sockets.commands.GetAccountCmd;
import bank.sockets.commands.GetBalanceCmd;
import bank.sockets.commands.GetNumbersCmd;
import bank.sockets.commands.IsActiveCmd;
import bank.sockets.commands.SetInactiveCmd;
import bank.sockets.commands.TransferCmd;
import bank.sockets.commands.WithdrawCmd;
import bank.sockets.Command;

public class Driver implements bank.BankDriver, Serializable {
	private Bank bank = new Bank();
	private transient Socket sock = null;
	private transient ObjectOutputStream outputstream;
	private transient ObjectInputStream inputstream;

	@Override
	public void connect(String[] args) throws UnknownHostException, IOException {
		sock = new Socket(args[0], Integer.parseInt(args[1]));
		System.out.println("Listen on: " + sock.getLocalPort());
		outputstream = new ObjectOutputStream(sock.getOutputStream());
		inputstream = new ObjectInputStream(sock.getInputStream());
		System.out.println("connected on port " + args[1]);
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
		sock.close();
		System.out.println("disconnected...");
	}

	@Override
	public bank.Bank getBank() {
		return bank;
	}

	class Bank implements bank.Bank, Serializable {

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			Command cmd = new GetNumbersCmd();
			try {
				outputstream.writeObject(cmd);
				return (Set<String>) inputstream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public String createAccount(String owner) throws IOException {
			Command cmd = new CreateAccountCmd(owner);
			try {
				outputstream.writeObject(cmd);
				return (String) inputstream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			Command cmd = new CloseAccountCmd(number);
			outputstream.writeObject(cmd);
			try {
				return (Boolean) inputstream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public bank.Account getAccount(String number) throws IOException{
			Command cmd = new GetAccountCmd(number);
			try {
				outputstream.writeObject(cmd);
				bank.Account acc = (bank.Account) inputstream.readObject();
				if(acc != null){
					Account myacc = new Account(acc.getOwner(), acc.getNumber());
					return myacc;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount) throws IOException, InactiveException, OverdrawException{
			Command cmd = new TransferCmd(from, to, amount);
			try {
				outputstream.writeObject(cmd);
				Exception result = (Exception) inputstream.readObject();
				if(result != null){
					if(result.getClass() == InactiveException.class){
						throw (InactiveException)result;
					}
					else if(result.getClass() == OverdrawException.class){
						throw (OverdrawException)result;
					}
					else if(result.getClass() == IllegalArgumentException.class){
						throw (IllegalArgumentException)result;
					}
					else{
						result.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	class Account implements bank.Account, Serializable{
		private final String number;
		private final String owner;

		Account(String owner, String number) {
			this.owner = owner;
			this.number = number;
		}

		@Override
		public double getBalance() throws IOException {
			Command cmd = new GetBalanceCmd(number);
			try {
				outputstream.writeObject(cmd);
				return (Double) inputstream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return -1;
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
		public boolean isActive() throws IOException {
			Command cmd = new IsActiveCmd(number);
			try {
				outputstream.writeObject(cmd);
				return (Boolean) inputstream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public void deposit(double amount) throws InactiveException, IOException {
			Command cmd = new DepositCmd(number, amount);
			try {
				outputstream.writeObject(cmd);
				Exception result = (Exception) inputstream.readObject();
				
				if(result != null){
					if(result.getClass() == InactiveException.class){
						throw (InactiveException)result;
					}
					else if(result.getClass() == IllegalArgumentException.class){
						throw (IllegalArgumentException)result;
					}
					else{
						result.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException, IOException {
			Command cmd = new WithdrawCmd(number, amount);
			try {
				outputstream.writeObject(cmd);
				Exception result = (Exception) inputstream.readObject();
				
				if(result != null){
					if(result.getClass() == InactiveException.class){
						throw (InactiveException)result;
					}
					else if(result.getClass() == OverdrawException.class){
						throw (OverdrawException)result;
					}
					else if(result.getClass() == IllegalArgumentException.class){
						throw (IllegalArgumentException)result;
					}
					else{
						result.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
		}

		public boolean setInactive() throws IOException {
			Command cmd = new SetInactiveCmd(number);
			try {
				outputstream.writeObject(cmd);
				return (Boolean) inputstream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return false;
		}

	}

}