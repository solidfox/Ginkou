package se.ginkou.database;

import java.util.HashMap;
import java.util.List;

import se.ginkou.Account;

public class AccountDB {

	// Cache of accounts loaded from database 
	private HashMap<Long, Account> cache = new HashMap<Long, Account>();
	private Database db;

	public AccountDB(Database db) {
		this.db = db;
		updateCache();
	}
	
	/**
	 * Adds newAccount to the database if it's not already there.
	 * If the database already has an account with the same number as newAccount
	 * newAccount is not added and the existing account is returned. In this case 
	 * the name of newAccount is discarded.
	 * @param newAccount the account object to add.
	 * @return the added account or, if it existed already, the existing account.
	 */
	public Account add(Account newAccount) {
		Account acc = get(newAccount.getNumber());
		if (acc != null) {return acc;}
		
		db.addAccount(newAccount);
		cache.put(newAccount.getNumber(), newAccount);
		
		return newAccount;
	}
	
	/**
	 * Returns the sought account object if it exists in the database, 
	 * otherwise returns null.
	 * @param accountNumber the account number of the sought account.
	 * @return the sought account object if it exists in the database, 
	 * otherwise null.
	 */
	public Account get(long accountNumber) {
		if (cache.containsKey(accountNumber)) {
			return cache.get(accountNumber);
		}
		updateCache();
		return cache.get(accountNumber);
	}

	private void updateCache() {
		List<Account> fromDB = db.getAccounts();
		for (Account acc : fromDB) {
			cache.put(acc.getNumber(), acc);
		}
	}
}
