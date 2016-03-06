package bank.sockets.commands;

import java.io.IOException;

import bank.Bank;
import bank.sockets.Command;

public class SetInactiveCmd implements Command {

	private boolean retval = false;
	private String number;
	
	@Override
	public Object getRetval() {
		return retval;
	}

	@Override
	public void execute(Bank b) throws IOException {
		retval = b.closeAccount(number);
	}

	
	public SetInactiveCmd(String number){
		this.number = number;
	}
}
