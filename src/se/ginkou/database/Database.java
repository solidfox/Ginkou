package se.ginkou.database;

import java.util.List;

import se.ginkou.Account;
import se.ginkou.Transaction;

public interface Database {
	
	public void 				clear();
	public void 				clearAllTransactions();
	public void 				clearAllAccounts();
	
	public boolean 				addTransaction(Transaction t);
	public boolean 				addTransactions(Transaction[] ts);
	public boolean 				addTransactions(List<Transaction> ts);
	public List<Transaction> 	getTransactions(String searchString);
	public int 					sizeTransactions();
	
	public boolean 				addAccount(Account a); 
	public List<Account> 		getAccounts();
	
}