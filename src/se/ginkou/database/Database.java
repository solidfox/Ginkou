package se.ginkou.database;

import java.util.List;

import org.joda.time.DateTime;

import se.ginkou.Account;
import se.ginkou.Transaction;

public interface Database {
	
	public void 				clear();
	public void 				clearAllTransactions();
	public void 				clearAllAccounts();
	
	
	/**
	 * Clears all transactions that are in the given account and 
	 * have a date greater or equal to the given date.
	 * Returns true if the action succeeded, false otherwise.
	 * @param date the date from which we should clear stuff.
	 * @param fromAccount the account from which the Transactions are to be cleared.
	 * @return true if the transactions were cleared.
	 */
	public boolean				clearAllTransactionsFrom(DateTime date, Account fromAccount);
	
	public boolean 				addTransaction(Transaction t);
	public boolean 				addTransactions(Transaction[] ts);
	public boolean 				addTransactions(List<Transaction> ts);
	public List<Transaction> 	getTransactions(String searchString);
	public int 					sizeTransactions();
	
	public int 					sizeOfResult(String searchString);
	
	public boolean 				addAccount(Account a); 
	public List<Account> 		getAccounts();
	
}