package bank.sockets.commands;

import java.io.IOException;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.sockets.Command;

@SuppressWarnings("serial")
public class WithdrawCmd implements Command {

	private String number;
	private double amount;
	private Exception retval = null;
	
	@Override
	public void execute(Bank b) throws IOException {
		try {
			b.getAccount(number).withdraw(amount);
		} catch (IllegalArgumentException e) {
			retval = e;
		} catch (InactiveException e) {
			retval = e;
		} catch (OverdrawException e) {
			retval = e;
		}
	}

	public WithdrawCmd(String number, double amount){
		this.number = number;
		this.amount = amount;
	}
	
	@Override
	public Object getRetval() {
		return retval;
	}

}
