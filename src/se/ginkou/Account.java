package se.ginkou;

import java.util.HashMap;

import se.ginkou.database.SQLiteDB;

/**
 * This class represents a bank account.
 * @author Daniel Schlaug
 * @version 0.1
 */
public class Account {
	
	private final int number; // Unique account number
	private String name; // Optional name of the account, can be null.
	
	// Cache of accounts loaded from database 
	private static HashMap<Integer, Account> cache = new HashMap<Integer, Account>();

	private Account(int accountNumber, String accountName) {
		this.number = accountNumber;
		this.name = accountName;
	}
	
	/**
	 * Returns an Account object with the given number.
	 * If there already exists transactions with the given account number
	 * the existing account is returned. There is no guarantee that the given
	 * account name will be used.
	 * @param accountNumber the unique number of the account.
	 * @param accountName used to name a new account if none already exists, 
	 * if null the account is given not given a name.
	 * @return an Account object with the given account number.
	 */
	public static Account newAccount(int accountNumber, String accountName) {
		Account acc = getAccount(accountNumber);
		if (acc != null) {return acc;}
		
		acc = new Account(accountNumber, accountName);
		Account.cache.put(acc.getNumber(), acc);
		return acc;
	}
	
	/**
	 * Returns the sought account object if it exists in the database, 
	 * otherwise returns null.
	 * @param accountNumber the account number of the sought account.
	 * @return the sought account object if it exists in the database, 
	 * otherwise null.
	 */
	public static Account getAccount(int accountNumber) {
		if (Account.cache.containsKey(accountNumber)) {
			return Account.cache.get(accountNumber);
		}
		return Account.loadFromDatabase(accountNumber);
	}

	private static Account loadFromDatabase(int accountNumber) {
		Account[] fromDB = SQLiteDB.getDB().getAccounts();
		for (Account acc : fromDB) {
			Account.cache.put(acc.getNumber(), acc);
		}
		return Account.cache.get(accountNumber);
	}

	/**
	 * Returns the account number.
	 * @return the account number.
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Returns the name of the account or the empty string if it has no name.
	 * @returnthe name of the account or the empty string if it has no name.
	 */
	public String getName() {
		return (name == null ? "" : name);
	}

	/**
	 * Returns a string containing information about the account.
	 */
	@Override
	public String toString() {
		return number + (name != null ? " " + name : "");
	}

	@Override
	public int hashCode() {
		return number;
	}

	/**
	 * Returns true if obj is an account with the same account number
	 * as this one. Otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (number != other.number)
			return false;
		return true;
	}
}
