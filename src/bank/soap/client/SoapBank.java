package bank.soap.client;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;
import bank.soap.jaxws.client.*;

final public class SoapBank implements bank.Bank {

	private SoapBankImpl port;
	
	public SoapBank() {
		SoapBankImplService service = new SoapBankImplService();
		port = service.getSoapBankImplPort();
	}
	
	@Override
	public String createAccount(String owner) throws IOException {
		try {
			return port.createAccount(owner);
		} catch (IOException_Exception e) {
			throw new IOException();
		}
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		try {
			return port.closeAccount(number);
		} catch (IOException_Exception e) {
			throw new IOException();
		}
	}

	@Override
	public Set<String> getAccountNumbers() throws IOException {
		Set<String> accountNumbers = new HashSet<String>();
		try {
			for(Object number : port.getAccountNumbers())
				accountNumbers.add((String)number);
		} catch (IOException_Exception e) {
			throw new IOException();
		}
		return accountNumbers;
	}

	@Override
	public Account getAccount(String number) throws IOException {
		String nr = "";
		try{
			nr = port.getAccount(number);
		} catch(Exception e) {
			throw new IOException();
		}
		
		if(nr == null || nr.isEmpty()) return null;
		else return new SoapAccount(port, number);
		
	}

	@Override
	public void transfer(Account a, Account b, double amount)
			throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		if(amount < 0)
			throw new IllegalArgumentException();
		
		try {
			port.transfer(a.getNumber(), b.getNumber(), amount);
		} catch (IOException_Exception e) {
			throw new IOException();
		} catch (InactiveException_Exception e) {
			throw new InactiveException();
		} catch (OverdrawException_Exception e) {
			throw new OverdrawException();
		}
	}
	
}

