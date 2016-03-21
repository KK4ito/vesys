package bank.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.*;
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

/**
 * exact copy from sockets.Driver with several modifications
 * @author Kevin
 *
 */
public class HTTPDriver implements bank.BankDriver, Serializable{
	private Bank bank = new Bank();
	private transient URL url;
	private transient HttpURLConnection httpCon;
	private transient ObjectOutputStream outputstream;
	private transient ObjectInputStream inputstream;
	

	@Override
	public void connect(String[] args) throws UnknownHostException, IOException {
		 url = new URL("http://localhost:8080/bank");
		System.out.println("connected on host " + url);
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
		httpCon.disconnect();
		System.out.println("disconnected...");
	}
	
	public Object sendRequest(Command cmd){
		Object response = null;
		try{
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setRequestMethod("POST");
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true); 
			httpCon.connect();

			outputstream = new ObjectOutputStream(httpCon.getOutputStream());
			outputstream.writeObject(cmd);
			outputstream.close();

			inputstream = new ObjectInputStream(httpCon.getInputStream());
			response = inputstream.readObject();
			outputstream.close();
		} catch(ClassNotFoundException | IOException e){
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public bank.Bank getBank() {
		return bank;
	}

	class Bank implements bank.Bank, Serializable {

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			Command cmd = new GetNumbersCmd();
			return (Set<String>) sendRequest(cmd);
			
		}

		@Override
		public String createAccount(String owner) throws IOException {
			Command cmd = new CreateAccountCmd(owner);
			return (String) sendRequest(cmd);
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			Command cmd = new CloseAccountCmd(number);
			return (Boolean) sendRequest(cmd);
		}

		@Override
		public bank.Account getAccount(String number) throws IOException{
			Command cmd = new GetAccountCmd(number);
			bank.Account acc = (bank.Account) sendRequest(cmd);
			if(acc != null){
					Account myacc = new Account(acc.getOwner(), acc.getNumber());
					return myacc;
			}
			return null;
				
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount) throws IOException, InactiveException, OverdrawException{
			Command cmd = new TransferCmd(from, to, amount);
			Exception result = (Exception) sendRequest(cmd);
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

		}

	}

	class Account implements bank.Account, Serializable {
		private final String number;
		private final String owner;

		Account(String owner, String number) {
			this.owner = owner;
			this.number = number;
		}

		@Override
		public double getBalance() throws IOException {
			Command cmd = new GetBalanceCmd(number);
			return (Double) sendRequest(cmd);
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
			return (Boolean) sendRequest(cmd);
		}

		@Override
		public void deposit(double amount) throws InactiveException, IOException {
			Command cmd = new DepositCmd(number, amount);
			Exception result = (Exception) sendRequest(cmd);
				
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
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException, IOException {
			Command cmd = new WithdrawCmd(number, amount);
			Exception result = (Exception) sendRequest(cmd);
				
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
		}

		public boolean setInactive() throws IOException {
			Command cmd = new SetInactiveCmd(number);
			return (Boolean) sendRequest(cmd);
		}

	}

}