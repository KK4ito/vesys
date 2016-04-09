package bank.soap;

import javax.xml.ws.Endpoint;

public class Publisher {
	private static final int port = 9001;
	
	public static void main(String[] args){
		Endpoint.publish("http://localhost:" + port + "/SoapBank",  new SoapBankImpl());
		System.out.println("Service published!");
	}
}
