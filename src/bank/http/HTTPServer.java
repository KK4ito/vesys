package bank.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


import bank.Bank;
import bank.sockets.Command;

import bank.local.Driver;

//opens server and handles requests
//@author Kevin & David

public class HTTPServer {

	public static void main(String args[]) throws IOException {

		Driver driver = new Driver();
		driver.connect(null);
		Bank bank = driver.getBank();

		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/bank", new Handler(bank));
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server listening on port 8080");

	}

	static class Handler implements HttpHandler {

		Bank bank;
		ObjectOutputStream outputstream;
		ObjectInputStream inputstream;

		public Handler (Bank b){
			this.bank = b;
		}


		@Override
		public void handle(HttpExchange channel) throws IOException {

			inputstream = new ObjectInputStream(channel.getRequestBody());
			System.out.println("entering handle-Method");

			try {
				Command request;
				Object response = null;
				while ( (request = (Command)inputstream.readObject())!= null) {
					request.execute(bank);
					response = request.getRetval();
					try{
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ObjectOutputStream tmpOut = new ObjectOutputStream(bos);
						tmpOut.writeObject(response);
						tmpOut.flush();
						tmpOut.close();
						long size = bos.toByteArray().length;

						channel.sendResponseHeaders(200, size);
						outputstream = new ObjectOutputStream(channel.getResponseBody());
						
						outputstream.writeObject(response);
					}catch (IOException e){
						e.printStackTrace();
					}

				}

			} catch (ClassNotFoundException | IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Done");

		}
	}
}

