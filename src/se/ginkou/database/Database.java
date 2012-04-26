package se.ginkou.database;

import java.sql.SQLException;
import java.util.Iterator;

import se.ginkou.Transaction;

interface Database {
	public void addTransaction(Transaction t) throws SQLException;
	public Iterator<Transaction> getTransactions(String searchString) throws SQLException;
}