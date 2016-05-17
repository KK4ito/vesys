package bank.rmi;

import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;

public class RMIServer {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		try {
			LocateRegistry.createRegistry(2111);
		} catch (RemoteException e) {
			System.out.println("Creating registry failed");
		}
		RMIBank rmibank = new RMIBankImpl();
		Naming.rebind("BankService", rmibank);

	}

}
