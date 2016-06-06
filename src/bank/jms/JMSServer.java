package bank.jms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.jms.commands.*;
import bank.local.Driver;


public class JMSServer {
	
	private static Bank bank;
	private static JMSProducer updateProducer;
	private static JMSConsumer operationsConsumer;
	private static JMSContext context;
	private static Queue queue;
	private static Topic topic;

	public static void main(String[] args) throws NamingException {
	
		final Context jndiContext = new InitialContext();
		final ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
		queue = (Queue) jndiContext.lookup("BANK");
		topic = (Topic) jndiContext.lookup("BANK.LISTENER");
		
		System.out.println("JMS-Resources bound.");
		
		bank = new JMSBank();
		
		System.out.println("Bank initialized.");
		
		context = factory.createContext();
		updateProducer = context.createProducer();
		operationsConsumer = context.createConsumer(queue);
		
		System.out.println("Server started");
		
		ObjectMessage message, response;
		Command cmd;
		
		while (true) {
			message = (ObjectMessage) operationsConsumer.receive();
			try {
				cmd = (Command) message.getObject();
				cmd.execute(bank);
				Serializable o = (Serializable) cmd.getRetval();
				response = context.createObjectMessage(o);
				updateProducer.send(message.getJMSReplyTo(), response);
			} catch (JMSException e) {
				System.err.println("Not a valid command received.");
			} catch (IOException e) {
				System.err.println("Command could not be executed.");
			}
		}
		
	}
	
	public static class JMSBank implements Bank, Serializable {
		
		private Bank bank;
		
		public JMSBank() {
			bank = new Driver().getBank(); 
		}

		@Override
		public String createAccount(String owner) throws IOException {
			System.out.println("Create account for " + owner);
			String accNumber = bank.createAccount(owner);
			if (accNumber != null)
				sendUpdates(accNumber);
			return accNumber;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			System.out.println("Close account #" + number);
			boolean success = bank.closeAccount(number);
			if (success)
				sendUpdates(number);
			return success;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			System.out.println("Get account numbers");
			return bank.getAccountNumbers();
		}

		@Override
		public Account getAccount(String number) throws IOException {
			Account acc = bank.getAccount(number);
			if (acc != null)
				return new JmsAccount(acc);
			else
				return null;
		}

		@Override
		public void transfer(Account a, Account b, double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			System.out.println("Transfer " + amount + " from " + a.getNumber() + " to " + b.getNumber());
			bank.transfer(a, b, amount);
		}
		
		public void sendUpdates(String number) {
			TextMessage m = context.createTextMessage(number);
			updateProducer.send(topic, m);
		}
		
		class JmsAccount implements bank.Account, Serializable{
			
			private Account account;
			
			public JmsAccount(Account acc) {
				account = acc;
			}

			@Override
			public String getNumber() throws IOException {
				return account.getNumber();
			}

			@Override
			public String getOwner() throws IOException {
				return account.getOwner();
			}

			@Override
			public boolean isActive() throws IOException {
				return account.isActive();
			}

			@Override
			public void deposit(double amount) throws IOException, IllegalArgumentException,
					InactiveException {
				account.deposit(amount);
				sendUpdates(account.getNumber());
			}

			@Override
			public void withdraw(double amount) throws IOException, IllegalArgumentException,
					OverdrawException, InactiveException {
				account.withdraw(amount);
				sendUpdates(account.getNumber());
			}

			@Override
			public double getBalance() throws IOException {
				return account.getBalance();
			}
		}
	}
}