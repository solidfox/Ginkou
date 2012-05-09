/**
 * 
 */
package se.ginkou.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import se.ginkou.Account;
import se.ginkou.Transaction;

/**
 * @author spike
 *
 */
public class SQLiteDB implements Database {
	
	static HashMap<String,SQLiteDB> dbs = new HashMap<String,SQLiteDB>();
	private Connection conn;
	public final AccountDB accounts;
	
	/**
	 * Factory method to get a database object.
	 * @param dbPath the path of the database.
	 * @return an SQLiteDB object.
	 */
	public static SQLiteDB getDB(String dbPath) {
		SQLiteDB db = dbs.get(dbPath);
		if (db == null) {
			db = new SQLiteDB(dbPath);
			dbs.put(dbPath, db);
		}
		return db;
	}
	
	public static Database getDB() {
		return getDB("ginkou.db");
	}
	
	private SQLiteDB(String dbPath) {
		assertConnection(dbPath);
		assertTransactionTable();
		assertAccountTable();
		accounts = new AccountDB(this);
	}
	
	/**
	 * Asserts that this object has a database connection. This is the only method
	 * that should be used to achieve such a connection.
	 * @throws SQLException
	 */
	private void assertConnection(String dbPath) {
		try {
			if (conn != null && conn.isValid(2)) {return;}
			Class.forName("org.sqlite.JDBC");
			this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void assertTransactionTable() {
		Statement stat;
		try {
			stat = conn.createStatement();
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS " +
					"transactions(" +
						"id 		INTEGER 	PRIMARY KEY, " +
						"accountID 	NUMERIC 	NOT NULL, " +
						"date 		INTEGER 	NOT NULL, " +
						"notice 	TEXT 		NOT NULL, " +
						"amount 	NUMERIC 	NOT NULL, " +
						"categoryID INTEGER 	DEFAULT NULL)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void assertAccountTable() {
		try {
			Statement stat = conn.createStatement();
			stat.executeUpdate(
					"CREATE TABLE IF NOT EXISTS " +
					"accounts(" +
						"id	 		NUMERIC 	NOT NULL, " +
						"name 		TEXT 		DEFAULT NULL)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int sizeTransactions() {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT Count(*) FROM transactions");
			rs.next();
			int size = rs.getInt(1);
			rs.close();
			return size;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int sizeOfResult(String searchString) {
		try {
			searchString = searchString.replace("SELECT\\s*(\\*|([\\w]*(,\\s*)?)*)", "SELECT count(*)");
			searchString.trim();
			if (!searchString.startsWith("SELECT")) {
				return -1;
			}
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(searchString);
			rs.next();
			int size = rs.getInt(1);
			rs.close();
			return size;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public boolean addAccount(Account a) {
		try {
			assertAccountTable();
			conn.createStatement().execute(
					"INSERT " +
					"INTO accounts (id, name) " +
					"VALUES (" + a.getNumber() + ",'" + a.getName() + "')");
			conn.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean addTransaction(Transaction t) {
		InsertTransactionStatement statement;
		try {
			statement = new InsertTransactionStatement();
			statement.addTransaction(t);
			statement.executeBatch();
			conn.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean addTransactions(List<Transaction> ts) {
		return addTransactions(ts.toArray(new Transaction[ts.size()]));
	}

	@Override
	public boolean addTransactions(Transaction[] ts) {
		InsertTransactionStatement statement;
		try {
			statement = new InsertTransactionStatement();
			for (Transaction t : ts) {
				statement.addTransaction(t);
			}
			statement.executeBatch();
			conn.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public List<Account> getAccounts() {
		assertAccountTable();
		ResultSet rs;
		ArrayList<Account> accounts = new ArrayList<Account>();
		try {
			rs = conn.createStatement().executeQuery("SELECT * FROM accounts");
			while (rs.next()) {
				accounts.add(accountFromResult(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accounts;
	}
	
	@Override
	public List<Transaction> getTransactions(String searchString) {
		ResultSet rs;
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		try {
			rs = conn.createStatement().executeQuery(searchString);
			while (rs.next()) {
				transactions.add(transactionFromResult(rs));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactions;
	}
	
	public void clear() {
		clearAllTransactions();
		clearAllAccounts();
	}

	public void clearAllTransactions() {
		try {
			conn.createStatement().execute("DROP TABLE transactions");
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTransactionTable();
	}
	
	public void clearAllAccounts() {
		try {
			conn.createStatement().execute("DROP TABLE accounts");
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTransactionTable();
	}
	
	@Override
	public boolean clearAllTransactionsFrom(DateTime date, Account fromAccount) {
		assertTransactionTable();
		try {
			String deleteStatement = 
					"DELETE FROM transactions " +
					"WHERE " +
					"	date(date/1000, 'unixepoch') >= date(" + date.getMillis()/1000 + ", 'unixepoch') " +
					"	AND " +
					"	accountID == " + fromAccount.getNumber();
			conn.createStatement().execute(deleteStatement);
			conn.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private Transaction transactionFromResult(ResultSet sqlResults) throws SQLException {
		int id = sqlResults.getInt("id");          
		Account account = accounts.get(sqlResults.getLong("accountID")); 
		DateTime date = new DateTime(sqlResults.getDate("date"));
		String notice = sqlResults.getString("notice");
		double amount = sqlResults.getDouble("amount");
		return new Transaction(id, account, date, notice, amount);
	}
	
	
	
	private Account accountFromResult(ResultSet sqlResults) throws SQLException {
		long number = sqlResults.getLong("id");
		String name = sqlResults.getString("name");
		return new Account(number, name);
	}



	class InsertTransactionStatement {
		PreparedStatement prep;
		
		/**
		 * Prepares a new an SQL statement that inserts values into the transactions
		 * table.
		 * @throws SQLException
		 */
		public InsertTransactionStatement() throws SQLException {
			assertTransactionTable();
			prep = conn.prepareStatement(
					"insert into transactions " +
					"(accountID, 	date, 	notice, 	amount, 	categoryID) " +
					"values " +
					"(?, 			?, 		?,			?, 			?);");
		}
		
		public void addTransaction(Transaction t) throws SQLException {
			accounts.add(t.getAccount());
			prep.setLong(1, t.getAccount().getNumber());
			prep.setDate(2, new Date(t.getDate().getMillis()));
			prep.setString(3, t.getNotice());
			prep.setDouble(4, t.getAmount());
			prep.setNull(5, Types.INTEGER);
			prep.addBatch();
		}
		
		/**
		 * Submits the collected batch of transactions to the database and if all 
		 * are inserted successfully, returns an array of update counts. 
		 * The int elements of the array that is returned are ordered to correspond 
		 * to the commands in the batch, which are ordered according to the order 
		 * in which they were added to the batch. The elements in the array 
		 * returned by the method executeBatch may be one of the following:
		 * 
		 * A number greater than or equal to zero -- indicates that the command was processed 
		 * successfully and is an update count giving the number of rows in the database that 
		 * were affected by the command's execution
		 * 
		 * A value of SUCCESS_NO_INFO -- indicates that the command was processed 
		 * successfully but that the number of rows affected is unknown
		 * If one of the commands in a batch update fails to execute properly, 
		 * this method throws a BatchUpdateException, and a JDBC driver may or may 
		 * not continue to process the remaining commands in the batch. However, 
		 * the driver's behavior must be consistent with a particular DBMS, either 
		 * always continuing to process commands or never continuing to process 
		 * commands. If the driver continues processing after a failure, the array 
		 * returned by the method BatchUpdateException.getUpdateCounts will contain 
		 * as many elements as there are commands in the batch, and at least one 
		 * of the elements will be the following:
		 * A value of EXECUTE_FAILED -- indicates that the command failed to execute 
		 * successfully and occurs only if a driver continues to process commands 
		 * after a command fails
		 * @throws SQLException - if a database access error occurs, this method 
		 * is called on a closed Statement or the driver does not support batch 
		 * statements. Throws BatchUpdateException (a subclass of SQLException) 
		 * if one of the commands sent to the database fails to execute properly 
		 * or attempts to return a result set.
		 * @return an array of update counts containing one element for each 
		 * command in the batch. The elements of the array are ordered according to 
		 * the order in which commands were added to the batch.
		 */
		public int[] executeBatch() throws SQLException {
			return prep.executeBatch();
		}
	}

}
