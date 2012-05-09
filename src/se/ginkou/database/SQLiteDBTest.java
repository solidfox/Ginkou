package se.ginkou.database;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import se.ginkou.Account;
import se.ginkou.Transaction;

public class SQLiteDBTest {
	
	private SQLiteDB db;
	private Transaction[] randomT;
	private Account account;
	private Account account2;

	@Before
	public void setUp() {
		db =  SQLiteDB.getDB("test.db");
		db.clear();
		randomT = randomTransactions();
		account = new Account(26762537, "Testaccount");
		account2 = new Account(26762578, "Testaccount2");
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
	
	@Test
	public void clearAllTransactionsFrom() {
		DateTime before = new DateTime("2012-04-23");
		DateTime after = new DateTime("2012-04-30");
		DateTime onDate = new DateTime("2012-04-27");
		db.addTransaction(new Transaction(
				account, 
				before,
				"before transaction",
				(double)(100)));
		db.addTransaction(new Transaction(
				account, 
				onDate,
				"ondate transaction",
				(double)(100)));
		db.addTransaction(new Transaction(
				account, 
				after,
				"after transaction",
				(double)(100)));
		db.addTransaction(new Transaction(
				account2, 
				before,
				"before transaction",
				(double)(100)));
		db.addTransaction(new Transaction(
				account2, 
				onDate,
				"ondate transaction",
				(double)(100)));
		db.addTransaction(new Transaction(
				account2, 
				after,
				"after transaction",
				(double)(100)));
		assertEquals(6, db.sizeTransactions());
		db.clearAllTransactionsFrom(new DateTime(), account);
		assertEquals(6, db.sizeTransactions());
		db.clearAllTransactionsFrom(after, account);
		assertEquals(5, db.sizeTransactions());
		db.clearAllTransactionsFrom(onDate, account);
		assertEquals(4, db.sizeTransactions());
		db.clearAllTransactionsFrom(before, account2);
		assertEquals(1, db.sizeTransactions());
		db.clearAllTransactionsFrom(before, account);
		assertEquals(0, db.sizeTransactions());
		
		
		//db.addTransactions(dummyTransactions());
		
	}
	
	@Test
	public void sizeOfResult() {
		db.addTransactions(randomTransactions());
		assertEquals(10, db.sizeTransactions());
		assertEquals(10, db.sizeOfResult("SELECT * FROM transactions"));
	}
	
	private Transaction[] randomTransactions() {
		Transaction[] transactions = new Transaction[10];
		
		for (int i = 0; i < 10; i++) {
			transactions[i] = new Transaction( 
					new Account(51232897892L, null), 
					new DateTime(), 
					"Test transaction " + i, 
					(double)(i*2000 + i*37));
		}
		
		return transactions;
	}
}
