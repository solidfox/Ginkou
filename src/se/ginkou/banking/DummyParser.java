package se.ginkou.banking;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.variables.Variable;
public class DummyParser {
	public static void main(String[] args) {
	    /** Prefix for any variable scraped which defines a URL. It will be followed by a counter. */  
	    final String SCRAPED_URL_VARIABLE_PREFIX = "url.";  
	    /** A variable name which holds the initial URL to scrape. */  
	    final String START_URL_VARIABLE = "url";  
		
		
		// register external plugins if there are any
		//DefinitionResolver.registerPlugin("com.my.MyPlugin1");

		ScraperConfiguration config;
		try {
			config = new ScraperConfiguration("test.xml");
			Scraper scraper = new Scraper(config, ".");
			scraper.addVariableToContext("username", "web-harvest");
			scraper.addVariableToContext("password", "web-harvest");

			//scraper.setDebug(true);

			scraper.execute();

			// takes variable created during execution			
			ScraperContext context = scraper.getContext();  
			System.out.println(context.size());
			System.out.println(context);
			
			ArrayList<String> al = new ArrayList<String>();
            int i=1;  
            Variable scrapedUrl;  
            while ((scrapedUrl = (Variable) context.get(SCRAPED_URL_VARIABLE_PREFIX+i))  != null)  
            {  
                // parse this URL  
                al.add(scrapedUrl.toString());  
                // move on  
                i++;  
            }  

			// do something with articles...
			System.out.println(al);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}