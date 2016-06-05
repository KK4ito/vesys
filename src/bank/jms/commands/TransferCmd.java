package bank.jms.commands;

import java.io.IOException;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;


@SuppressWarnings("serial")
public class TransferCmd implements Command {

	public Account from;
	public Account to;
	public double amount;
	public Exception retval = null;
	
	@Override
	public void execute(Bank b) throws IOException {
		try {
			b.transfer(b.getAccount(from.getNumber()), b.getAccount(to.getNumber()), amount);
		} catch (IllegalArgumentException e) {
			retval = e;
		} catch (OverdrawException e) {
			retval = e;
		} catch (InactiveException e) {
			retval = e;
		}
	}

	public TransferCmd(Account from, Account to, double amount){
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	
	@Override
	public Object getRetval() {
		return retval;
	}

}
