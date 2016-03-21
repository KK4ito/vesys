package bank.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import bank.Account;
import bank.Bank;
import bank.OverdrawException;
import bank.sockets.Command;

import bank.local.Driver;

public class HTTPServer {

	public static void main(String args[]) throws IOException {

		Driver driver = new Driver();
		driver.connect(null);
		Bank bank = driver.getBank();

		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/bank", new MyHandler(bank));
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server listening on port 8080");

	}

	static class MyHandler implements HttpHandler {

		Bank bank;
		ObjectOutputStream oos;
		ObjectInputStream ois;

		public MyHandler(Bank b){
			this.bank = b;
		}


		@Override
		public void handle(HttpExchange exchange) throws IOException {

			ois = new ObjectInputStream(exchange.getRequestBody());
			System.out.println("entering handle-Method");



			try {
				Command request;
				Object response = null;
				while ( (request = (Command)ois.readObject())!= null) {
					request.execute(bank);
					response = request.getRetval();

					try{

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(bos);
						os.writeObject(response);
						os.flush();
						os.close();
						long size = bos.toByteArray().length;

						// send response
						exchange.sendResponseHeaders(200, size);
						oos = new ObjectOutputStream(exchange.getResponseBody());
						
						
						oos.writeObject(response);
					}catch (IOException e){
						e.printStackTrace();
					}

				}

			} catch (ClassNotFoundException | IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("done serving ");

		}
	}
}

