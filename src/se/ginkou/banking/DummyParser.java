package se.ginkou.banking;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimePrinter;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.variables.Variable;

import se.ginkou.Account;
import se.ginkou.Transaction;
import se.ginkou.database.SQLiteDB;
public class DummyParser {
	public static void main(String[] args) {
	
		// register external plugins if there are any

		ScraperConfiguration config;
		try {
			config = new ScraperConfiguration("sebscrapper.xml");
			Scraper scraper = new Scraper(config, ".");
			//scraper.setDebug(true);
			scraper.execute();	
			
			// takes variable created during execution			
			ScraperContext context = scraper.getContext();
			System.out.println(context.size());
			//System.out.println(context);			
			
			//Variable login1 = (Variable) context.get("login1");
			//System.out.println(login1.toString());
			//Variable accounts = (Variable) context.get("accounts");
			//System.out.println(accounts.toString());
			
			ArrayList<Transaction> transactions = new ArrayList<Transaction>();
			int i=1;  
			Variable account, date, notice, amount;
			if((account = (Variable) context.get("account"))  != null){

				while ((date = (Variable) context.get("date."+i))  != null && 
						(notice = (Variable) context.get("notice."+i))  != null &&
						(amount = (Variable) context.get("amount."+i))  != null){
					transactions.add(new Transaction(
							new Account(account.toString()), 
							DateTime.parse(date.toString().trim(), DateTimeFormat.forPattern("yyMMdd")), 
							notice.toString(), 
							Double.parseDouble(amount.toString().replace(".", "").replace(",", "."))));
					i++;  
				}
			}
            i = 0;
            for(Transaction t: al){
            	//System.out.println(i +": "+t.toString());
            	//SQLiteDB.getDB().addTransaction(t);
            	++i;
            }
            
            //SQLiteDB.getDB().addTransactions(al.toArray());
            
			// do something with articles...
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}