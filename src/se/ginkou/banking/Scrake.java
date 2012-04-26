    package scrape;  
      
    import java.io.*;  
    import java.util.*;  
      
    import org.apache.commons.logging.*;  
    import org.webharvest.definition.ScraperConfiguration;  
    import org.webharvest.runtime.*;  
    import org.webharvest.runtime.variables.Variable;  
      
    /** 
     * Quick hackable web scraping class. 
     * @author Tom Godber 
     */  
    public abstract class QuickScraper  
    {  
        /** Logging object. */  
        protected final Log LOG = LogFactory.getLog(getClass());  
        /** Prefix for any variable scraped which defines a URL. It will be followed by a counter. */  
        public static final String SCRAPED_URL_VARIABLE_PREFIX = "url.";  
        /** A variable name which holds the initial URL to scrape. */  
        public static final String START_URL_VARIABLE = "url";  
      
        /** A temporary working folder. */  
        private File working = new File("temp");  
      
        /** Ensures temp folder exists.` */  
        public QuickScraper()  
        {  
            working.mkdirs();  
        }  
      
        /** 
         * Scrapes a list of URLs which are automatically derived from a page. 
         * The initial URL must be set in the actual URL list config XML. 
         * @param urlConfigXml Path of an XML describing how to scrape the URL list. 
         * @param pageConfigXml Path of an XML describing how to scrape the individual pages found.# 
         * @return The number of URLs processed, or -1 if the config could not be loaded. 
         */  
        protected int scrapeUrlList(String urlConfigXml, String pageConfigXml)  
        {  
            return scrapeUrlList(new HashMap(), urlConfigXml, pageConfigXml);  
        }  
      
        /** 
         * Scrapes a list of URLs which are automatically derived from a page. 
         * @param setup Optional configuration for the script 
         * @param urlConfigXml Path of an XML describing how to scrape the URL list. 
         * @param pageConfigXml Path of an XML describing how to scrape the individual pages found.# 
         * @return The number of URLs processed, or -1 if the config could not be loaded. 
         */  
        protected int scrapeUrlList(Map setup, String urlConfigXml, String pageConfigXml)  
        {  
            return scrapeUrlList(setup, new File(urlConfigXml), new File(pageConfigXml));  
        }  
      
        /** 
         * Scrapes a list of URLs which are automatically derived from a page. 
         * The initial URL must be set in the actual URL list config XML. 
         * @param urlConfigXml XML describing how to scrape the URL list. 
         * @param pageConfigXml XML describing how to scrape the individual pages found.# 
         * @return The number of URLs processed, or -1 if the config could not be loaded. 
         */  
        protected int scrapeUrlList(File urlConfigXml, File pageConfigXml)  
        {  
            return scrapeUrlList(new HashMap(), urlConfigXml, pageConfigXml);  
        }  
      
        /** 
         * Scrapes a list of URLs which are automatically derived from a page. 
         * @param setup Optional configuration for the script 
         * @param urlConfigXml XML describing how to scrape the URL list. 
         * @param pageConfigXml XML describing how to scrape the individual pages found. 
         * @return The number of URLs processed, or -1 if the config could not be loaded. 
         * @throws NullPointerException If the setup map is null. 
         */  
        protected int scrapeUrlList(Map setup, File urlConfigXml, File pageConfigXml)  
        {  
            try  
            {  
                if (LOG.isDebugEnabled())   LOG.debug("Starting scrape with temp folder "+working.getAbsolutePath()+"...");  
                // generate a one-off scraper based on preloaded configuration  
                ScraperConfiguration config = new ScraperConfiguration(urlConfigXml);  
                Scraper scraper = new Scraper(config, working.getAbsolutePath());  
                // initialise any config  
                setupScraperContext(setup, scraper);  
                // run the script  
                scraper.execute();  
      
                // rip the URL list out of the scraped content  
                ScraperContext context = scraper.getContext();  
                int i=1;  
                Variable scrapedUrl;  
                if (LOG.isDebugEnabled())   LOG.debug("Scraping performed, pulling URLs '"+SCRAPED_URL_VARIABLE_PREFIX+"n' from "+context.size()+" variables, starting with "+i+"...");  
                while ((scrapedUrl = (Variable) context.get(SCRAPED_URL_VARIABLE_PREFIX+i))  != null)  
                {  
                    if (LOG.isTraceEnabled())   LOG.trace("Found "+SCRAPED_URL_VARIABLE_PREFIX+i+": "+scrapedUrl.toString());  
                    // parse this URL  
                    setup.put(START_URL_VARIABLE, scrapedUrl.toString());  
                    scrapeUrl(setup, pageConfigXml);  
                    // move on  
                    i++;  
                }  
                if (LOG.isDebugEnabled())   LOG.debug("No more URLs found.");  
                return i;  
            }  
            catch (FileNotFoundException e)  
            {  
                if (LOG.isErrorEnabled())   LOG.error("Could not find config file '"+urlConfigXml.getAbsolutePath()+"' - no scraping was done for this WebHarvest XML.", e);  
                return -1;  
            }  
            finally  
            {  
                working.delete();  
            }  
        }  
      
        /** 
         * Scrapes an individual page, and passed the results on for processing. 
         * The script must contain a hardcoded URL. 
         * @param configXml XML describing how to scrape an individual page. 
         */  
        protected void scrapeUrl(File configXml)  
        {  
            scrapeUrl((String)null, configXml);  
        }  
      
        /** 
         * Scrapes an individual page, and passed the results on for processing. 
         * @param url The URL to scrape. If null, the URL must be set in the config itself. 
         * @param configXml XML describing how to scrape an individual page. 
         */  
        protected void scrapeUrl(String url, File configXml)  
        {  
            Map setup = new HashMap();  
            if (url!=null)  setup.put(START_URL_VARIABLE, url);  
            scrapeUrl(setup, configXml);  
        }  
      
        /** 
         * Scrapes an individual page, and passed the results on for processing. 
         * @param setup Optional configuration for the script 
         * @param configXml XML describing how to scrape an individual page. 
         */  
        protected void scrapeUrl(Map setup, File configXml)  
        {  
            try  
            {  
                if (LOG.isDebugEnabled())   LOG.debug("Starting scrape with temp folder "+working.getAbsolutePath()+"...");  
                // generate a one-off scraper based on preloaded configuration  
                ScraperConfiguration config = new ScraperConfiguration(configXml);  
                Scraper scraper = new Scraper(config, working.getAbsolutePath());  
                setupScraperContext(setup, scraper);  
                scraper.execute();  
      
                // handle contents in some way  
                pageScraped((String)setup.get(START_URL_VARIABLE), scraper.getContext());  
      
                if (LOG.isDebugEnabled())   LOG.debug("Page scraping complete.");  
            }  
            catch (FileNotFoundException e)  
            {  
                if (LOG.isErrorEnabled())   LOG.error("Could not find config file '"+configXml.getAbsolutePath()+"' - no scraping was done for this WebHarvest XML.", e);  
      
            }  
            finally  
            {  
                working.delete();  
            }  
        }  
      
        /** 
         * @param setup Any variables to be set before the script runs. 
         * @param scraper The object which does the scraping. 
         */  
        private void setupScraperContext(Map<string,object> setup, Scraper scraper)  
        {  
            if (setup!=null)  
                for (String key : setup.keySet())  
                    scraper.getContext().setVar(key, setup.get(key));  
        }  
      
        /** 
         * Process a page that was scraped. 
         * @param url The URL that was scraped. 
         * @param context The contents of the scraped page. 
         */  
        public abstract void pageScraped(String url, ScraperContext context);  
    }  
    </string,object>  