package u01_OLD;

import java.net.Socket;
import java.io.*;

import bank.local.Driver.Bank;

public class Session implements Runnable {

	private Socket socket;
	private Bank bank;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public Session(Socket socket, Bank bank) throws IOException {
		this.socket = socket;
		this.bank = bank;
		oos = new ObjectOutputStream(this.socket.getOutputStream());
		ois = new ObjectInputStream(this.socket.getInputStream());
	}

	@Override
	public void run() {
		try {
			SocketCommands sc = (SocketCommands) ois.readObject();
			
			switch(sc.com){
			case "getAccountNumbers":
				oos.writeObject(bank.getAccountNumbers()); break;
			case "getAccount":
				oos.writeObject(bank.getAccount(sc.args.get(0))); break;
			case "createAccount":
				oos.writeObject(bank.createAccount(sc.args.get(0))); break;
			case "existAccount": 
				oos.writeObject(bank.getAccount(sc.args.get(0)) != null); break;
			case "closeAccount":
				oos.writeObject(bank.closeAccount(sc.args.get(0))); break;
			case "withdraw":
				try {
					bank.getAccount(sc.args.get(0)).withdraw(Double.parseDouble(sc.args.get(1)));
					oos.writeObject(true);
				} catch (Exception e){
					oos.writeObject(false);
				}
				break;
			case "deposit":
				try {
					bank.getAccount(sc.args.get(0)).deposit(Double.parseDouble(sc.args.get(1)));
					oos.writeObject(true);
				} catch (Exception e){
					oos.writeObject(false);
				}
				break;
			case "isActive":
				oos.writeObject(bank.getAccount(sc.args.get(0)).isActive()); break;
			case "getBalance":
				oos.writeObject(bank.getAccount(sc.args.get(0)).getBalance()); break;
			case "getOwner":
				oos.writeObject(bank.getAccount(sc.args.get(0)).getOwner()); break;
			default: 
				System.out.println("Something went wrong - defaults switch");
				throw new Exception();
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} 
		
		try {
			oos.close();
			ois.close();
			socket.close();
		} catch(IOException e){
			e.printStackTrace();
		}

	}

}
