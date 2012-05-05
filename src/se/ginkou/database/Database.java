package se.ginkou.database;

import java.util.List;

import se.ginkou.Account;
import se.ginkou.Transaction;

public interface Database {
	
	public boolean addTransaction(Transaction t);
	public boolean addTransactions(Transaction[] ts);
	
	public List<Transaction> getTransactions(String searchString);
	
	public boolean addAccount(Account a); 
	public List<Account> getAccounts();
	
}