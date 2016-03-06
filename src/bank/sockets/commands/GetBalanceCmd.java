package bank.sockets.commands;

import java.io.IOException;

import bank.Bank;
import bank.sockets.Command;

public class GetBalanceCmd implements Command {

	private double retval;
	private String number;
	
	@Override
	public Double getRetval() {
		return retval;
	}

	@Override
	public void execute(Bank b) throws IOException {
		retval = b.getAccount(number).getBalance();
	}

	public GetBalanceCmd(String number){
		this.number = number;
	}
}
