package u01_OLD;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

import bank.local.Driver;
import bank.local.Driver.Bank;


public class SocketServer {
	private static int port = 769;

	
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(port);
		ExecutorService es = Executors.newCachedThreadPool();
		
		Bank bank = new Driver().getBank();
		if(bank != null){
			System.out.println("Server listening on port " + port);
			while(true) {
				Socket s = socket.accept();
				es.execute(new Session(s, bank));
			}
		}
		else {
			System.out.println("failed bank");
			throw new IOException();
		}
		
	}
	

}
