package bank.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import bank.Bank;
import bank.local.Driver;

@SuppressWarnings("serial")
public class Server {
	
	private Set<ClientHandler> connections = new HashSet<ClientHandler>();
	private static ServerSocket sock = null;


	public static void main(String[] args) {
		Driver d = new bank.local.Driver();
		d.connect(null);
		Bank bank = d.getBank();
		
		try {
			sock = new ServerSocket(1234);
		} catch (Exception e) {
			System.out.println("Could not listen on port 1234");
			System.exit(-1);
		}
		try {
			while (true) {
				Socket clientSocket = sock.accept();
				ClientHandler c = new ClientHandler(bank, clientSocket);
				//connections.add(c);
				new Thread(c).start();
			}
		} catch (IOException e) {
			System.out.println("Could not accept Client Socket:");
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
