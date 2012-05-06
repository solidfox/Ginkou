package se.ginkou.database;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import se.ginkou.Account;
import se.ginkou.Transaction;

import junit.framework.TestCase;

public class SQLiteDBTest {
	
	private SQLiteDB db;
	private Transaction[] randomT;
	private Account account;

	@Before
	public void setUp() {
		db =  SQLiteDB.getDB("test.db");
		db.clear();
		randomT = randomTransactions();
		account = new Account(26762537, "Testaccount");
	}
	
	@Test
	public void addTransactions() throws SQLException, ClassNotFoundException {
		db.addTransactions(randomT);
		List<Transaction> result = db.getTransactions("select * from transactions");
		
		assertEquals(randomT.length, result.size());
		
		for (int i = 0; i < randomT.length; i++) {
			assertEquals(randomT[i], result.get(i));
		}
		
		assertEquals(randomT.length, db.sizeTransactions());
		
		db.clearAllTransactions();
		int numberOfTransactions = db.getTransactions("SELECT * FROM transactions").size();
		assertEquals(0, numberOfTransactions);
	}
	
	@Test
	public void addAccount() {
		db.addAccount(account);
		List<Account> result = db.getAccounts();
		assertEquals(result.size(), 1);
		assertEquals(account, result.get(0));
	}
	
	private Transaction[] randomTransactions() {
		Transaction[] transactions = new Transaction[10];
		
		for (int i = 0; i < 10; i++) {
			transactions[i] = new Transaction(i, 
					new Account(51232897892L, null), 
					new DateTime(), 
					"Test transaction " + i, 
					(double)(i*2000 + i*37));
		}
		
		return transactions;
	}
}
