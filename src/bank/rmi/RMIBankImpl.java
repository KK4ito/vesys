package bank.rmi;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;
import bank.local.Driver;
import bank.local.Driver.Bank;

public final class RMIBankImpl extends UnicastRemoteObject implements RMIBank {
	private Bank bank;
	
	
	public RMIBankImpl() throws RemoteException{
		super();
		Driver d = new bank.local.Driver();
		d.connect(null);
		bank = d.getBank();
	}
	@Override
	public String createAccount(String owner) throws IOException {
		return bank.createAccount(owner);
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		return bank.closeAccount(number);
	}

	@Override
	public Set<String> getAccountNumbers() throws IOException {
		return bank.getAccountNumbers();
	}

	@Override
	public Account getAccount(String number) throws IOException {
		return bank.getAccount(number);
	}

	@Override
	public void transfer(Account a, Account b, double amount)
			throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		bank.transfer(a, b, amount);

	}

}
