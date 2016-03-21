package bank.internet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import bank.Bank;
import bank.local.Driver;
import bank.sockets.Command;

public class Servlet {
	
	private static ObjectInputStream in = null;
	private static ObjectOutputStream out = null;

	public static void main(String args[]) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
		Driver d = new bank.local.Driver();
		d.connect(null);
		Bank bank = d.getBank();

		server.createContext("/command", (HttpExchange t) ->{
			in = new ObjectInputStream(t.getRequestBody());
			t.sendResponseHeaders(200, 0);
			out =  new ObjectOutputStream(t.getResponseBody());
			
			try{
				Command read = (Command) in.readObject();
				read.execute(bank);
				out.writeObject(read.getRetval());
			
			}
			catch(EOFException e){
				System.out.println("Client disconnect.");
			}
				
			catch(Exception e){
				e.printStackTrace();
			}
		});
		
		server.setExecutor(null);
		server.start();

	}
}
