package bank.rmi;


import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;


import bank.Bank;
import bank.BankDriver;


public class Driver implements BankDriver{
	private Bank bank;

	@Override
	public void connect(String[] args) throws IOException {
		try {
			LocateRegistry.createRegistry(1234);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
