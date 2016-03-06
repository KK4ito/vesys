package bank.sockets;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bank.Bank;

public class ClientHandler implements Runnable {

	private Bank bank;
	private Socket clientsocket;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	@Override
	public void run() {
		Boolean run = true;
		while (run) {
			try{
				Command read = (Command) in.readObject();
				read.execute(bank);
				out.writeObject(read.getRetval());
			}
			catch(EOFException e){
				run = false;
				System.out.println("Client disconnect.");
			}
			catch (Exception e){
				run = false;
				e.printStackTrace();
			}

		}

	}

	public ClientHandler(Bank bank, Socket client) throws IOException {
		this.bank = bank;
		this.clientsocket = client;
		out = new ObjectOutputStream(clientsocket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(clientsocket.getInputStream());
	}

}
