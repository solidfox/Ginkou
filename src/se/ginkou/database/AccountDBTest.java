package se.ginkou.database;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import se.ginkou.Account;

public class AccountDBTest {

	Account a;
	
	@Before
	public void setUp() throws Exception {
		a = new Account(338762, "Test");
	}
	
	@Test
	public void add() {
		Database db = SQLiteDB.getDB("test.db");
		db.clear();
		AccountDB acc = new AccountDB(db);
		acc.add(a);
		assertEquals(a, acc.get(a.getNumber()));
		assertEquals(a, db.getAccounts().get(0));
	}

}
