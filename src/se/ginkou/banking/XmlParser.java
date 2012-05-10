package se.ginkou.banking;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

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
import se.ginkou.database.Database;
import se.ginkou.database.SQLiteDB;
public class XmlParser {
	private String xml;
	private String[] keys;
	
	public XmlParser(String xmlFile, String[] keys) {
		this.xml = xmlFile;
		this.keys = keys;
	}
	
	 /**
	  * 
	  * @return null if provided with non-functional keys, else a List of Transactions.
	  */
	public List<Transaction> run(){
		// register external plugins if there are any
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		ScraperConfiguration config;
		try {
			config = new ScraperConfiguration(xml);
			Scraper scraper = new Scraper(config, ".");
			
			//scraper.getHttpClientManager().setHttpProxy("localhost", 8888); //Fiddling ;)
			for(int i = 0; i<keys.length; ++i)
				scraper.addVariableToContext("key_"+i, keys[i]);
			
			//scraper.setDebug(true);
			scraper.execute();	
			
			// takes variable created during execution			
			ScraperContext context = scraper.getContext();
			//System.out.println(context.size());
			//System.out.println(context);			
			
			String accessGranted = ((Variable) context.get("accessGranted")).toString();
			if(!accessGranted.equals("true"))
				return null;
			Variable account, date, notice, amount;
			int q = 1;
			while((account = (Variable) context.get("account."+q))  != null){

				int i=1;  
				while ((date = (Variable) context.get("date."+q+"."+i))  != null && 
						(notice = (Variable) context.get("notice."+q+"."+i))  != null &&
						(amount = (Variable) context.get("amount."+q+"."+i))  != null){
					transactions.add(new Transaction(
							new Account(Long.parseLong(account.toString().replaceAll("[\\-\\s]", "")), ""), 
							DateTime.parse(date.toString().trim(), DateTimeFormat.forPattern("yyMMdd")), 
							notice.toString(), 
							Double.parseDouble(amount.toString().replace(".", "").replace(",", "."))));
					i++;  
				}
				++q;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return transactions;
	}

	public static void main(String[] args) {
		String[] s = {"8702190011","ingetINGET5"};
		XmlParser parser = new XmlParser("rules/SEB.xml", s);
		//XmlParser parser = new XmlParser("rules/dummybank.xml", s2);
		List<Transaction> trans = parser.run();
		if(trans==null){
			System.err.println("trans==null");
			return;
		}
		for(Transaction t: trans)
			System.out.println(t);
		System.out.println(trans.size());
	}
}