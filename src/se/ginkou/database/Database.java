package se.ginkou.database;

import java.sql.SQLException;
import java.util.ArrayList;
import se.ginkou.Transaction;

interface Database {
	public void addTransaction(Transaction t) throws SQLException;
	public ArrayList<Transaction> getTransactions(String searchString) throws SQLException;
	void addTransactions(Transaction[] ts) throws SQLException;
}