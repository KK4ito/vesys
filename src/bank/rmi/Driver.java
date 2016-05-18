package bank.rmi;


import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;


import bank.Bank;
import bank.BankDriver;


public class Driver implements BankDriver{
	private Bank bank;

	@Override
	public void connect(String[] args) throws IOException {
		try {
			bank = (Bank) Naming.lookup("BankService");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
		
	}

	@Override
	public Bank getBank() {
		return this.bank;
	}


}
