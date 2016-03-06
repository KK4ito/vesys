package bank.sockets.commands;

import java.io.IOException;

import bank.Bank;
import bank.InactiveException;
import bank.sockets.Command;

@SuppressWarnings("serial")
public class DepositCmd implements Command {

	private String number;
	private double amount;
	private Exception retval = null;
	
	@Override
	public void execute(Bank b) throws IOException {
		try {
			b.getAccount(number).deposit(amount);
		} catch (IllegalArgumentException e) {
			retval = e;
		} catch (InactiveException e) {
			retval = e;
		}
	}

	public DepositCmd(String number, double amount){
		this.number = number;
		this.amount = amount;
	}
	
	@Override
	public Object getRetval() {
		return retval;
	}

}
