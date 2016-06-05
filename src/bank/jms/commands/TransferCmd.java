package bank.jms.commands;

import java.io.IOException;
import java.io.Serializable;


import bank.*;
import bank.InactiveException;
import bank.OverdrawException;


@SuppressWarnings("serial")
public class TransferCmd implements Command{

	private String from;
	private String to;
	private double amount;
	private Object retval = null;
	
	@Override
	public void execute(Bank b) throws IOException {
		try {
			b.transfer(b.getAccount(from), b.getAccount(to), amount);
		} catch (IllegalArgumentException e) {
			retval = e;
		} catch (OverdrawException e) {
			retval = e;
		} catch (InactiveException e) {
			retval = e;
		}
	}

	public TransferCmd(String from, String to, double amount){
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	
	@Override
	public Object getRetval() {
		return retval;
	}

}
