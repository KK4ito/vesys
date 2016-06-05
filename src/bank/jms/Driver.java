package bank.jms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.jms.commands.*;
import bank.soap.jaxws.Withdraw;

public class Driver implements bank.BankDriver2, Serializable{
	
	private bank.Bank bank;
	private JMSContext context;
	private JMSProducer operationsProducer;
	private JMSConsumer updatesConsumer;
	private JMSConsumer responseConsumer;
	private Queue queue;
	private Topic topic;
	private TemporaryQueue response;

	@Override
	public void connect(String[] args) throws IOException {
		try {
			final Context jndiContext = new InitialContext();
			final ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
			context = factory.createContext();
						
			queue = (Queue) jndiContext.lookup("BANK");
			topic = (Topic) jndiContext.lookup("BANK.LISTENER");
			response = context.createTemporaryQueue();
			
			responseConsumer = context.createConsumer(response);
			
			context = factory.createContext();
			updatesConsumer = context.createConsumer(topic);
			
			context = factory.createContext();
			operationsProducer = context.createProducer().setJMSReplyTo(response);
			
			System.out.println("Client initialized.");
		} catch (NamingException e) {
			System.err.println("Cannot bind JMS-resources.");
		}
		bank = new JMSBank();
	}

	@Override
	public void disconnect() throws IOException {
		context.close();
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		updatesConsumer.setMessageListener(new UpdateListener(handler));	
	}
	
	public class JMSBank implements bank.Bank, Serializable {

		@Override
		public String createAccount(String owner) throws IOException {
			operationsProducer.send(queue, new CreateAccountCmd(owner));
			return (String) getResponse();
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			operationsProducer.send(queue, new CloseAccountCmd(number));
			return (boolean) getResponse();
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			operationsProducer.send(queue, new GetNumbersCmd());
			return (Set<String>) getResponse();
		}

		@Override
		public Account getAccount(String number) throws IOException {
			operationsProducer.send(queue, new GetAccountCmd(number));
			Account acc = (Account) getResponse();
			if (acc == null)
				return null;
			else
				return new JMSAccount(acc.getNumber());
		}

		@Override
		public void transfer(Account a, Account b, double amount)
				throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
			operationsProducer.send(queue, new TransferCmd(a, b, amount));
		
			Object o = getResponse();
			if (o != null)
				if (o instanceof IOException)
					throw new IOException(((IOException) o).getMessage());
				else if (o instanceof IllegalArgumentException)
					throw new IllegalArgumentException(((IllegalArgumentException) o).getMessage());
				else if (o instanceof OverdrawException)
					throw new OverdrawException(((OverdrawException) o).getLocalizedMessage());
				else if (o instanceof InactiveException)
					throw new InactiveException(((InactiveException) o).getMessage());
		}
		
		private Object getResponse() {
			Message message = responseConsumer.receive(10000);	// 10s until timeout
			if (message == null) 
				System.err.println("Response timed out.");
			Object ret = null;
			try {
				ret = ((ObjectMessage)message).getObject();
			} catch (JMSException e) {
				System.err.println("Not a valid object.");
			}
			return ret;
		}
		
		class JMSAccount implements bank.Account, Serializable {
			
			private String number;
			
			public JMSAccount(String num) {
				number = num;
			}

			@Override
			public String getNumber() {
				return number;
			}

			@Override
			public String getOwner() throws IOException {
				operationsProducer.send(queue, new GetAccountCmd(number));
				return ((Account) getResponse()).getOwner();
			}

			@Override
			public boolean isActive() throws IOException {
				operationsProducer.send(queue, new IsActiveCmd(number));
				return (boolean) getResponse();
			}

			@Override
			public void deposit(double amount)
					throws IOException, IllegalArgumentException, InactiveException {
				operationsProducer.send(queue, new DepositCmd(number, amount));
				Object o = getResponse();
				if (o != null) {
					if (o instanceof IllegalArgumentException)
						throw new IllegalArgumentException();
					else if (o instanceof InactiveException)
						throw new InactiveException();
				}
			}

			@Override
			public void withdraw(double amount)
					throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
				operationsProducer.send(queue, new WithdrawCmd(number, amount));
				Object o = getResponse();
				if (o != null) {
					if (o instanceof IllegalArgumentException)
						throw new IllegalArgumentException();
					else if (o instanceof InactiveException)
						throw new InactiveException();
					else if (o instanceof OverdrawException)
						throw new OverdrawException();
				}
			}

			@Override
			public double getBalance() throws IOException {
				operationsProducer.send(queue, new GetBalanceCmd(number));
				return (double) getResponse();
			}
		}
	}
}