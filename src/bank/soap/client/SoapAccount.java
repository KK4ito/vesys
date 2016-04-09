package bank.soap.client;

import java.io.IOException;

import bank.InactiveException;
import bank.OverdrawException;
import bank.soap.jaxws.client.*;

public class SoapAccount implements bank.Account {

	private SoapBankImpl port;
	private String number;
	
	public SoapAccount(SoapBankImpl port, String number) {
		this.port = port;
		this.number = number;
	}
	
	@Override
	public String getNumber() throws IOException {
		return number;
	}

	@Override
	public String getOwner() throws IOException {
		try {
			return port.getOwner(number);
		} catch (IOException_Exception e) {
			throw new IOException();
		}
	}

	@Override
	public boolean isActive() throws IOException {
		try {
			return port.isActive(number);
		} catch (IOException_Exception e) {
			throw new IOException();
		}
	}

	@Override
	public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
		if(amount < 0)
			throw new IllegalArgumentException("amount must be positive");
		
		try {
			port.deposit(number, amount);
		} catch (IOException_Exception e) {
			throw new IOException();
		} catch (InactiveException_Exception e) {
			throw new InactiveException();
		}
	}

	@Override
	public void withdraw(double amount)
			throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		if(amount < 0)
			throw new IllegalArgumentException("amount must be positive");
		
		try {
			port.withdraw(number, amount);
		} catch (IOException_Exception e) {
			throw new IOException();
		} catch (InactiveException_Exception e) {
			throw new InactiveException();
		} catch (OverdrawException_Exception e) {
			throw new OverdrawException();
		}
	}

	@Override
	public double getBalance() throws IOException {
		try {
			return port.getBalance(number);
		} catch (IOException_Exception e) {
			throw new IOException();
		}
	}

}

