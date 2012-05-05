package se.ginkou.database;

import se.ginkou.Account;
import se.ginkou.Transaction;

public interface Database {
	
	public void addTransaction(Transaction t);
	boolean addTransactions(Transaction[] ts);
	
	public Transaction[] getTransactions(String searchString);
	
	public Account[] getAccounts();
	
}