package bank.rmi;

import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;


public final class RMIBankImpl extends UnicastRemoteObject implements RMIBank {
	
	private final Map<String, RMIAccount> accounts = new HashMap<>();
	
	
	public RMIBankImpl() throws RemoteException{
		super();
		
	}
	@Override
	public String createAccount(String owner) throws IOException {
		UUID u = UUID.randomUUID();
		while(accounts.containsKey(u)) u = UUID.randomUUID();
		
		RMIAccount acc = new RMIAccountImpl(owner, u.toString());
		accounts.put(acc.getNumber(), acc);
		return acc.getNumber();
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		if(this.accounts.get(number) == null) return false;
		else if(this.accounts.get(number).getBalance() != 0 || !this.accounts.get(number).isActive()) return false;
		RMIAccount a = this.accounts.get(number);
		((RMIAccountImpl) a).deactivate();
		return true;
	}

	@Override
	public Set<String> getAccountNumbers() throws IOException {
		Set<String> lstAccountNumbers = new HashSet<>();
		for(RMIAccount a : this.accounts.values()){
			if(a.isActive()){
				lstAccountNumbers.add(a.getNumber());
			}
		}
		return lstAccountNumbers;
	}

	@Override
	public Account getAccount(String number) throws IOException {
		return accounts.get(number);
	}

	@Override
	public void transfer(Account from, Account to, double amount)
			throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
		if(!from.isActive() || !to.isActive()) throw new InactiveException();
		if(from.getBalance() < amount) throw new OverdrawException();
		from.withdraw(amount);
		to.deposit(amount);

	}
	

}
