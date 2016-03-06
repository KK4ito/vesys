package bank.sockets.commands;

import java.io.IOException;

import bank.Account;
import bank.Bank;
import bank.sockets.Command;

@SuppressWarnings("serial")
public class GetAccountCmd implements Command {

	public Account retval;
	
	@Override
	public void execute(Bank b) throws IOException {
		retval = b.getAccount(number);
	}

	private String number;
	
	public GetAccountCmd(String number){
		this.number = number;
	}

	@Override
	public Account getRetval() {

		return retval;
	}
}
