package bank.rmi;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

public class RMIAccountImpl extends UnicastRemoteObject implements RMIAccount {
	private String number;
	private String owner;
	private double balance;
	public boolean active;
	
	public RMIAccountImpl(String o, String n) throws RemoteException{
		super();
		this.owner = o;
		this.number = n;
		this.active = true;
	}
	@Override
	public String getNumber() throws IOException {
		return this.number;
		
	}

	@Override
	public String getOwner() throws IOException {
		return this.owner;
	}

	@Override
	public boolean isActive() throws IOException {
		return this.active;
	}

	@Override
	public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
		if(!isActive()) throw new InactiveException();
		if(amount < 0) throw new IllegalArgumentException();
		this.balance+= amount;
		
	}

	@Override
	public void withdraw(double amount)	throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		if(!isActive()) throw new InactiveException();
		if(amount < 0) throw new IllegalArgumentException();
		if(getBalance() < amount) throw new OverdrawException();
		this.balance-= amount;
		
	}

	@Override
	public double getBalance() throws IOException {
		return this.balance;
	}

	public boolean deactivate() throws IOException {
		if (balance == 0 && isActive()) {
			active = false;
			return true;
		}
		return false;
	}
}
