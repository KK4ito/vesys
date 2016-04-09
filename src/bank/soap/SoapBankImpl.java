package bank.soap;

import java.io.IOException;
import java.util.Set;
import bank.InactiveException;
import bank.OverdrawException;
import bank.*;
import bank.local.Driver;
import javax.jws.*;

@WebService
public class SoapBankImpl implements SoapBank {

	private Bank localBank;
	
	public SoapBankImpl() {
		localBank = new Driver().getBank();
	}
	
	
	@Override
	public String getAccount(@WebParam(name="number")String number)  throws IOException {
		Account ac = localBank.getAccount(number); 
		if(ac != null)
			return ac.getNumber();
		return null;
		
	}
	
	@Override
	public String createAccount(@WebParam(name="owner")String owner) throws IOException {
		String newAccountNumber = localBank.createAccount(owner);
		return newAccountNumber;
	}

	
	@Override
	public Object[] getAccountNumbers() throws IOException {
		Set<String> accountNumbers = localBank.getAccountNumbers();
		return accountNumbers.toArray();
	}

	@Override
	public boolean closeAccount(@WebParam(name="number")String number) throws IOException {
		boolean accountClosed = localBank.closeAccount(number);
		return accountClosed;
	}

	@Override
	public String getOwner(@WebParam(name="number")String number) throws IOException {
		String owner = localBank.getAccount(number).getOwner();
		return owner;
	}

	@Override
	public boolean isActive(@WebParam(name="number")String number) throws IOException {
		boolean accountActive = localBank.getAccount(number).isActive();
		return accountActive;
	}

	@Override
	public double getBalance(@WebParam(name="number")String number) throws IOException {
		double balance = localBank.getAccount(number).getBalance();
		return balance;
	}

	@Override
	public void deposit(@WebParam(name="number")String number, @WebParam(name="amount")double amount) throws IllegalArgumentException, InactiveException, IOException {
		localBank.getAccount(number).deposit(amount);
	}

	@Override
	public void withdraw(@WebParam(name="number")String number, @WebParam(name="amount")double amount)
			throws IllegalArgumentException, InactiveException, IOException, OverdrawException {
		localBank.getAccount(number).withdraw(amount);
	}

	@Override
	public void transfer(@WebParam(name="accountA")String accountA, @WebParam(name="accountB")String accountB, @WebParam(name="amount")double amount)
			throws InactiveException, IOException, OverdrawException {
		localBank.transfer(localBank.getAccount(accountA), localBank.getAccount(accountB), amount);
	}



}

