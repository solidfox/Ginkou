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
import org.joda.time.DateTime;

import se.ginkou.Account;
import se.ginkou.Transaction;

/**
 * @author spike
 *
 */
public class SQLiteDB implements Database {
	
	static HashMap<String,SQLiteDB> dbs = new HashMap<String,SQLiteDB>();
	Connection conn;
	
	/**
	 * Factory method to get a database object.
	 * @return a SQLiteDB object.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static SQLiteDB getDB(String dbPath) throws ClassNotFoundException, SQLException {
		SQLiteDB db = dbs.get(dbPath);
		if (db == null) {
			db = new SQLiteDB(dbPath);
			dbs.put(dbPath, db);
		}
		return db;
	}
	
	public static SQLiteDB getDB() throws ClassNotFoundException, SQLException {
		return getDB("ginkou.db");
	}
	
	private SQLiteDB(String dbPath) throws SQLException {
		assertConnection(dbPath);
		assertTransactionTable();
	}
	
	/**
	 * Asserts that this object has a database connection. This is the only method
	 * that should be used to achieve such a connection.
	 * @throws SQLException
	 */
	private void assertConnection(String dbPath) throws SQLException {
		if (conn != null && conn.isValid(2)) {return;}
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
		conn.setAutoCommit(false);
	}
	
	private void assertTransactionTable() throws SQLException {
		Statement stat = conn.createStatement();
		stat.executeUpdate(
				"CREATE TABLE IF NOT EXISTS " +
				"transactions(" +
					"id 		INTEGER 	PRIMARY KEY, " +
					"accountID 	NUMERIC 	NOT NULL, " +
					"date 		INTEGER 	NOT NULL, " +
					"notice 	TEXT 		NOT NULL, " +
					"amount 	NUMERIC 	NOT NULL, " +
					"categoryID INTEGER 	DEFAULT NULL)");
	}

	@Override
	public void addTransaction(Transaction t) throws SQLException {
		InsertTransactionStatement statement = new InsertTransactionStatement();
		statement.addTransaction(t);
		statement.executeBatch();
		conn.commit();
	}
	
	@Override
	public void addTransactions(Transaction[] ts) throws SQLException {
		InsertTransactionStatement statement = new InsertTransactionStatement();
		for (Transaction t : ts) {
			statement.addTransaction(t);
		}
		statement.executeBatch();
		conn.commit();
	}

	@Override
	public ArrayList<Transaction> getTransactions(String searchString) throws SQLException {
		ResultSet rs = conn.createStatement().executeQuery(searchString);
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		while (rs.next()) {
			transactions.add(transactionFromResult(rs));
		}
		return transactions;
	}
	
	public void clearTransactions() throws SQLException {
		conn.createStatement().execute("DROP TABLE transactions");
		conn.commit();
		assertTransactionTable();
	}
	
	private Transaction transactionFromResult(ResultSet SQLResults) throws SQLException {
		int id = SQLResults.getInt("id");          
		Account account = new Account(SQLResults.getInt("accountID")); 
		DateTime date = new DateTime(SQLResults.getDate("date"));
		String notice = SQLResults.getString("notice");
		double amount = SQLResults.getDouble("amount");
		return new Transaction(id, account, date, notice, amount);
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
					"(id, 	accountID, 	date, 	notice, 	amount, 	categoryID) " +
					"values " +
					"(?, 	?, 			?, 		?,			?, 			?);");
		}
		
		public void addTransaction(Transaction t) throws SQLException {
			prep.setInt(1, t.getId());
			prep.setInt(2, t.getAccount().getID());
			prep.setDate(3, new Date(t.getDate().getMillis()));
			prep.setString(4, t.getNotice());
			prep.setDouble(5, t.getAmount());
			prep.setNull(6, Types.INTEGER);
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
