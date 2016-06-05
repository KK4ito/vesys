package bank.jms.commands;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import bank.Bank;


@SuppressWarnings("serial")
public class GetNumbersCmd implements Command{

	public Set<String> retval = null;
	
	@Override
	public void execute(Bank b) throws IOException {
		retval = b.getAccountNumbers();
	}

	@Override
	public Set<String> getRetval() {
		return retval;
	}

}
