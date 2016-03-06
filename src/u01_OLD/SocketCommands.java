package u01_OLD;

import java.util.*;
import java.io.Serializable;

public class SocketCommands implements Serializable{

	public String com;
	public List<String> args;
	
	public enum Command{
		getAccountNumbers, getAccount, createAccount, existAccount, closeAccount,
		withdraw, deposit, isActive, getBalance, getOwner;
	}
	public SocketCommands(Command c, String... args){
		this.com = c.toString();
		this.args = Arrays.asList(args);
		
	}
}
