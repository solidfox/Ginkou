package se.ginkou.database;

import java.util.List;

import org.joda.time.DateTime;

import se.ginkou.Account;
import se.ginkou.Transaction;

/**
 * Provides an SQLdatabase interface taylored for 
 * storing Transaction and Account objects.
 * @author Daniel Schlaug
 */
public interface Database {
	
	/**
	 * Clears the entire database!!!
	 */
	public void 				clear();
	
	/**
	 * Clears all transactions from the database.
	 */
	public void 				clearAllTransactions();
	
	/**
	 * Clears all accounts from the database
	 */
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
	
	/**
	 * Adds a single transaction to the database.
	 * This is slow for adding many transactions.
	 * Consider using addTransactions() instead.
	 * @param t the transaction to add.
	 * @return true if the transaction was successfully added.
	 */
	public boolean 				addTransaction(Transaction t);
	
	/**
	 * Adds an array of transactions to the database.
	 * Writes all transactions in one batch => fast.
	 * @param ts the transactions to add.
	 * @return true if the transactions was successfully added.
	 */
	public boolean 				addTransactions(Transaction[] ts);
	
	/**
	 * Adds a List of transactions to the database.
	 * Writes all transactions in one batch => fast.
	 * @param ts the transactions to add.
	 * @return true if the transactions was successfully added.
	 */
	public boolean 				addTransactions(List<Transaction> ts);
	
	/**
	 * Gets transactions from the database. This function takes a raw
	 * sql statement and returns any resulting transactions.
	 * @param searchString a raw SQL string that results in transactions.
	 * @return a List with the found transactions.
	 */
	public List<Transaction> 	getTransactions(String searchString);
	
	/**
	 * Returns the total number of transactions in the database.
	 * @return the total number of transactions in the database.
	 */
	public int 					sizeTransactions();
	
	/**
	 * Returns the number of rows returned by the given sql statement.
	 * @param searchString a raw sql statement.
	 * @return the number of rows that the sql statement resulted in.
	 */
	public int 					sizeOfResult(String searchString);
	
	/**
	 * Adds a single account to the database.
	 * @param a the account to add.
	 * @return true if the account was successfully added.
	 */
	public boolean 				addAccount(Account a); 
	
	/**
	 * Returns all accounts in the database.
	 * @return all accounts in the database.
	 */
	public List<Account> 		getAccounts();
	
}