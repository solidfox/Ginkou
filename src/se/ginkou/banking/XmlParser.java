package se.ginkou.banking;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.variables.Variable;
import se.ginkou.Account;
import se.ginkou.Transaction;

/**
 * @author Frans Tegelmark
 */
public class XmlParser {
	private String xml; // the .xml file with login and scraping rules  
	private String[] keys; //login credentials
	
	/**
	 * Creates a XmlParser with sufficient data for execution with run().
	 * @param xmlFile	The .xml file that will be executed with web-harvest.
	 * @param keys		Array of Strings for credentials. How the Strings are used depends on the .xml file entirely.
	 */
	public XmlParser(String xmlFile, String[] keys) {
		this.xml = xmlFile;
		this.keys = keys;
	}
	
	 /**
	  * Executes the .xml
	  * The keys are provided to the .xml script, named "key_#" with value keys[#]
	  *  where # is the argument number (starting with 0).
	  * (Actual behaviour depending on the .xml file)  
	  * @return null if provided with non-functional keys, else a List of Transactions.
	  */
	public List<Transaction> run(){
		ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 
		ScraperConfiguration config;
		try {
			config = new ScraperConfiguration(xml);
			Scraper scraper = new Scraper(config, ".");
			
			//scraper.getHttpClientManager().setHttpProxy("localhost", 8888); //for debugging with fiddler2
			for(int i = 0; i<keys.length; ++i)
				scraper.addVariableToContext("key_"+i, keys[i]);		
			scraper.execute();				
			// takes variable created during execution			
			ScraperContext context = scraper.getContext();		
			
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
	
	/**
	 * Demo.
	 * @param args	Not used.
	 */
	public static void main(String[] args) {
		String[] s = {"8702190011","ingetINGET5"};
		XmlParser parser = new XmlParser("rules/SEB.xml", s);
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