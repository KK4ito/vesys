package bank.rmi;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

public class RMIAccountImpl extends UnicastRemoteObject implements RMIAccount {
	private Account account;
	
	public RMIAccountImpl(Account a) throws RemoteException{
		super();
		this.account = a;
	}
	@Override
	public String getNumber() throws IOException {
		return account.getNumber();
	}

	@Override
	public String getOwner() throws IOException {
		return account.getOwner();
	}

	@Override
	public boolean isActive() throws IOException {
		return account.isActive();
	}

	@Override
	public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
		account.deposit(amount);
		
	}

	@Override
	public void withdraw(double amount)
			throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		account.withdraw(amount);
		
	}

	@Override
	public double getBalance() throws IOException {
		// TODO Auto-generated method stub
		return account.getBalance();
	}

}
