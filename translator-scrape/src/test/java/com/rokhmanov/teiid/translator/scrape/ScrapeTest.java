package com.rokhmanov.teiid.translator.scrape;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.jdbc.TeiidDriver;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;

public class ScrapeTest 
{

    private static EmbeddedServer server;
    private TeiidDriver teiidDriver;
    private Connection connection;
    private static final Logger logger = Logger.getLogger(ScrapeTest.class);
    static
    {
        final PatternLayout layout = new PatternLayout();
        logger.addAppender(new ConsoleAppender(layout));
    }
    
    
    @BeforeClass
    public static void init() throws Exception
    {
        EmbeddedConfiguration ec = new EmbeddedConfiguration();
        ec.setUseDisk(true);                
        server = new EmbeddedServer();
        server.start(ec);
        server.addTranslator(new ScrapeExecutionFactory());

        final ModelMetaData scrapeModel = new ModelMetaData();
        scrapeModel.setName("scrapedata");
        scrapeModel.setSchemaSourceType("native");
        scrapeModel.addSourceMapping("dummy", "web-scrape", null);

        server.deployVDB("test", scrapeModel);
    }
  
    
    
    @AfterClass
    public static void tearDown()
    {
        server.stop();
    }   
    
    
    @Before
    public void prepare() throws Exception
    {
        teiidDriver = server.getDriver();
        connection = teiidDriver.connect("jdbc:teiid:test", null);       
    }
    
    
    @After
    public void release() throws Exception
    {
        if (null != connection && !connection.isClosed())
        {
            connection.close();
        }
    }

    
    @Test
    public void testBingScrape() throws Exception
    {
        final String sql = "select id, tagname, text, attributes,classnames, script_data, inner_html from ("
                + "call scrapedata.scrape('http://www.bing.com/search?q=jboss+teiid','a[href]')"
                + ") as S where upper(text) like '%TEIID%'";     
        
        List<String> results = execute(sql);
        logger.debug("Returned results:" + Arrays.toString(results.toArray()));
        assertTrue("Search results returned from Bing", results.size() > 0);
    }      
    
    
    private List<String> execute(String sql) throws Exception
    {
        List<String> resultStore = new ArrayList<String>();
        Statement statement = this.connection.createStatement();
        boolean hasResults = statement.execute(sql);
        if (hasResults)
        {
            ResultSet results = statement.getResultSet();
            ResultSetMetaData metadata = results.getMetaData();
            int columns = metadata.getColumnCount();
            while(results.next())
            {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < columns; i++)
                {                    
                    sb.append("|").append(results.getString(i + 1));
                }
                resultStore.add(sb.toString());
            }
            results.close();
        }
        statement.close();
        return resultStore;
    }
}

