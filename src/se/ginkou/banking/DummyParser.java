package se.ginkou.banking;
import java.io.FileNotFoundException;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
public class DummyParser {
	public static void main(String[] args) {
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
			scraper.getContext()

			// do something with articles...
			//System.out.println(articles.isEmpty());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}