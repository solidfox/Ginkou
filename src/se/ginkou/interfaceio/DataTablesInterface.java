package se.ginkou.interfaceio;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import se.ginkou.Transaction;
import se.ginkou.database.Database;
import se.ginkou.database.SQLiteDB;

public class DataTablesInterface {
	
	TreeMap<String, String> commands;
	Database db;
	
	public DataTablesInterface(final String inString) {
		String decodedString = "";
		try {
			decodedString = URLDecoder.decode(inString, "utf-8");
		} catch (UnsupportedEncodingException e) {throw new IllegalStateException("The URLDecoder could not handle utf-8");}
		String[] rawCommands = decodedString.split("&");
		commands = new TreeMap<String,String>();
		for (String aCommand : rawCommands) {
			String[] commandParts = aCommand.split("=");
			assert(commandParts.length == 2);
			commands.put(commandParts[0], (commandParts.length > 1 ? commandParts[1] : null));
		}
		
		db = SQLiteDB.getDB();
		System.out.println(commands);
	}
	
	public String getResponse() {
		final String[] columns = {"accountID", "date", "notice", "amount"};
		final String sqlTable = "transactions";
		
		String query = "";
		
		/* 
		 * Paging
		 */
		String sqlLimit = "";
		if ( commands.containsKey("iDisplayStart") && !commands.get("iDisplayLength").equals("-1") )
		{
			sqlLimit = " LIMIT " + commands.get("iDisplayStart") + ", " + commands.get("iDisplayLength");
		}
		
		/*
		 * Ordering
		 */
		String sqlOrder = "";
		if ( commands.containsKey("iSortCol_0") ) {
			sqlOrder = "ORDER BY  ";
			for ( int i = 0 ; i < Integer.parseInt(commands.get("iSortingCols")) ; i++ )
			{
				if ( commands.get( "bSortable_" + Integer.parseInt(commands.get("iSortCol_" + i)) ).equals("true") ) {
					sqlOrder += "`" + columns[ Integer.parseInt( commands.get( "iSortCol_" + i) ) ] + "` " + commands.get("sSortDir_" + i) + ", ";
				}
			}
			
			sqlOrder = sqlOrder.substring(0, sqlOrder.length() - 2);
			if ( sqlOrder.equals("ORDER BY ") ) {
				sqlOrder = "";
			}
		}
		
		/* 
		 * Filtering
		 * NOTE this does not match the built-in DataTables filtering which does it
		 * word by word on any field. It's possible to do here, but concerned about efficiency
		 * on very large tables, and MySQL's regex functionality is very limited
		 */
		String sqlWhere = "";
		if ( commands.containsKey("sSearch") && commands.get("sSearch") != null )
		{
			sqlWhere = "WHERE (";
			for ( int i = 0 ; i < columns.length ; i++ ) {
				sqlWhere += "`" + columns[i] + "` LIKE '%" + commands.get("sSearch") + "%' OR ";
			}
			sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 4);
			sqlWhere += ")";
		}
		
		/* Individual column filtering */
		for ( int i = 0 ; i < columns.length ; i++ ){
			if ( commands.containsKey("bSearchable_" +  i) && commands.get("bSearchable_" + i).equals("true") && commands.get("sSearch_" + i) != null ) {
				if ( sqlWhere == "" ) {
					sqlWhere = "WHERE ";
				} else {
					sqlWhere += " AND ";
				}
				sqlWhere += "`" + columns[i] + "` LIKE '%" + commands.get("sSearch_" + i) + "%' ";
			}
		}
		

		/*
		 * SQL queries
		 * Get data to display
		 */
		query = "SELECT *" + 
				" FROM " + sqlTable + " " + sqlWhere + sqlOrder + sqlLimit;
		
		List<Transaction> transactions = db.getTransactions(query);
		
		/* Data set length after filtering */
		int iFilteredTotal = transactions.size();
		

		JsonParser jParser = new JsonParser();
		JsonArray jsonTransactions = new JsonArray();
		
		for (Transaction t : transactions) {
			jsonTransactions.add(jParser.parse(t.toJSON()));
		}
		
		JsonObject output = new JsonObject();
		output.addProperty("sEcho", commands.get("sEcho"));
		output.addProperty("iTotalRecords", db.sizeTransactions());
		output.addProperty("iTotalDisplayRecords", iFilteredTotal);
		output.add("aaData", jsonTransactions);
		
		return output.toString();
	}
	
	private String implode(String delimiter, String[] strings) {
		String out = "";
		for (String aString : strings) {
			out += aString + delimiter;
		}
		out = out.substring(0, out.length() - delimiter.length());
		return out;
	}

	public static void main(String[] args) {
		DataTablesInterface DT = new DataTablesInterface("sEcho=1&iColumns=4&sColumns=&iDisplayStart=0&iDisplayLength=10&mDataProp_0=account&mDataProp_1=date&mDataProp_2=notice&mDataProp_3=sum&sSearch=&bRegex=false&sSearch_0=&bRegex_0=false&bSearchable_0=true&sSearch_1=&bRegex_1=false&bSearchable_1=true&sSearch_2=&bRegex_2=false&bSearchable_2=true&sSearch_3=&bRegex_3=false&bSearchable_3=true&iSortCol_0=0&sSortDir_0=asc&iSortingCols=1&bSortable_0=true&bSortable_1=true&bSortable_2=true&bSortable_3=true");
		System.out.println(DT.getResponse());
	}
}
