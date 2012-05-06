package se.ginkou.database;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import se.ginkou.Account;
import se.ginkou.Transaction;

import junit.framework.TestCase;

public class SQLiteDBTest extends TestCase {
	
	private static SQLiteDB db;

	@Test
	public static void test() throws SQLException, ClassNotFoundException {
		db =  SQLiteDB.getDB("test.db");
		Transaction[] testTransactions = uniqueDummyTransactions();
		
		db.clearAllTransactions();
		db.addTransactions(testTransactions);
		List<Transaction> result = db.getTransactions("select * from transactions");
		
		assertEquals(testTransactions.length, result.size());
		
		for (int i = 0; i < testTransactions.length; i++) {
			assertEquals(testTransactions[i], result.get(i));
		}
		
		assertEquals(testTransactions.length, db.sizeTransactions());
		
		db.clearAllTransactions();
		int numberOfTransactions = db.getTransactions("SELECT * FROM transactions").size();
		assertEquals(0, numberOfTransactions);
	}
	
	private static Transaction[] uniqueDummyTransactions() {
		Transaction[] transactions = new Transaction[10];
		
		for (int i = 0; i < 10; i++) {
			transactions[i] = new Transaction(i, 
					db.accounts.add(new Account(51232897892L, null)), 
					new DateTime(), 
					"Test transaction " + i, 
					(double)(i*2000 + i*37));
		}
		
		return transactions;
	}
}
