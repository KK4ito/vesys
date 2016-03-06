package bank.sockets.commands;

import java.io.IOException;

import bank.Account;
import bank.Bank;
import bank.sockets.Command;

@SuppressWarnings("serial")
public class CreateAccountCmd implements Command {

	public String retval = null;

	@Override
	public void execute(Bank b) throws IOException {
		retval = b.createAccount(owner);
	}

	private String owner;

	public CreateAccountCmd(String s) {
		owner = s;
	}

	@Override
	public String getRetval() {
		return retval;
	}

}
