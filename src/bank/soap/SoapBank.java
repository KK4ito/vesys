package bank.soap;

import java.io.IOException;

import javax.jws.*;

import bank.InactiveException;
import bank.OverdrawException;

@WebService
public interface SoapBank {
	String createAccount(@WebParam(name="owner") String owner) throws IOException;
	String getAccount(@WebParam(name="number")String number) throws IOException; 
	Object[] getAccountNumbers() throws IOException;
	boolean closeAccount(@WebParam(name="number")String number) throws IOException;
	String getOwner(@WebParam(name="number")String number) throws IOException;
	boolean isActive(@WebParam(name="number")String number) throws IOException;
	double getBalance(@WebParam(name="number")String number) throws IOException;
	void deposit(@WebParam(name="number")String number, @WebParam(name="amount")double amount) throws IllegalArgumentException, InactiveException, IOException;
	void withdraw(@WebParam(name="number")String number, @WebParam(name="amount")double amount) throws IllegalArgumentException, InactiveException, IOException, OverdrawException;
	void transfer(@WebParam(name="accountA")String accountA, @WebParam(name="accauntB")String accountB, @WebParam(name="amount")double amount) throws InactiveException, IOException, OverdrawException;
}