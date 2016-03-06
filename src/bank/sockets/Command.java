package bank.sockets;

import java.io.IOException;
import java.io.Serializable;
import bank.Bank;

public abstract interface Command extends Serializable{
	
	public Object retval = null;
	public Object getRetval();
	public void execute (Bank b) throws IOException;

}
