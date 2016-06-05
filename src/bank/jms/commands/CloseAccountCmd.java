package bank.jms.commands;

import java.io.IOException;


import bank.Bank;


@SuppressWarnings("serial")
public class CloseAccountCmd implements Command {
	
	private String number;
	private Boolean retval;

	@Override
	public void execute(Bank b) throws IOException {
		retval = b.closeAccount(number);
	}

	@Override
	public Boolean getRetval() {
		return retval;
	}
	
	public CloseAccountCmd(String number){
		this.number = number;
	}

}
