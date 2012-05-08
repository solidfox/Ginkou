package se.ginkou.banking;
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
	private String userid;
	private String password;
	
	public XmlParser(String xmlFile, String userid, String password) {
		this.xml = xmlFile;
		this.userid = userid;
		this.password = password;
	}
	
	public List<Transaction> run(){
		// register external plugins if there are any
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		ScraperConfiguration config;
		try {
			config = new ScraperConfiguration(xml);
			Scraper scraper = new Scraper(config, ".");
			
			scraper.addVariableToContext("userid", userid);
			scraper.addVariableToContext("passwd", password);
			
			//scraper.setDebug(true);
			scraper.execute();	
			
			// takes variable created during execution			
			ScraperContext context = scraper.getContext();
			//System.out.println(context.size());
			//System.out.println(context);			
			
			Variable account, date, notice, amount;
			if((account = (Variable) context.get("account"))  != null){

				int i=1;  
				while ((date = (Variable) context.get("date."+i))  != null && 
						(notice = (Variable) context.get("notice."+i))  != null &&
						(amount = (Variable) context.get("amount."+i))  != null){
					transactions.add(new Transaction(
							new Account(Long.parseLong(account.toString().replaceAll("[\\-\\s]", "")), ""), 
							DateTime.parse(date.toString().trim(), DateTimeFormat.forPattern("yyMMdd")), 
							notice.toString(), 
							Double.parseDouble(amount.toString().replace(".", "").replace(",", "."))));
					i++;  
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transactions;
	}

	public static void main(String[] args) {
		XmlParser parser = new XmlParser("dummyscraper.xml", "8702190011", "ingetINGET5");
		System.out.println(parser.run());
	}
}