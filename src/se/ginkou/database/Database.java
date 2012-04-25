package se.ginkou.database;

interface Database {
	public void addTransaction(Transaction t);
	public Transaction[] getTransactions(String searchString);
}