package bank.jms.commands;

import java.io.IOException;

import bank.Bank;


public class IsActiveCmd implements Command {

	private boolean retval;
	private String number;
	
	@Override
	public Object getRetval() {
		return retval;
	}

	@Override
	public void execute(Bank b) throws IOException {
		retval = b.getAccount(number).isActive();
	}

	public IsActiveCmd(String number){
		this.number = number;
	}
}
