package bank.soap.client;

import java.io.IOException;

import bank.Bank;
import bank.BankDriver;

public class SoapDriver implements BankDriver {

	private Bank bank = null;
	@Override
	public void connect(String[] args) throws IOException {
		bank = new SoapBank();
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
	}

	@Override
	public Bank getBank() {
		return bank;
	}

}

