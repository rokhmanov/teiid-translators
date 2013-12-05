package com.rokhmanov.teiid.translator.ebay;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.resource.cci.ConnectionFactory;

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

import com.rokhmanov.teiid.resource.adapter.ebay.EBayManagedConnectionFactory;

public class EbayTest 
{
    private static String DEVELOPER_KEY = "PUT-YOUR-DEVELOPER-KEY-HERE";
    private static EmbeddedServer server;
    private TeiidDriver teiidDriver;
    private Connection connection;
    private static final Logger logger = Logger.getLogger(EbayTest.class);
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

        EBayManagedConnectionFactory emcf = new EBayManagedConnectionFactory();
        emcf.setDeveloperKey(DEVELOPER_KEY);
        final ConnectionFactory cf = emcf.createConnectionFactory();
        server.addConnectionFactoryProvider("source-ebay", new EmbeddedServer.SimpleConnectionFactoryProvider<ConnectionFactory>(cf));
        server.addTranslator(EBayExecutionFactory.class);
        final ModelMetaData testModel = new ModelMetaData();
        testModel.setName("ebaydata");
        testModel.setSchemaSourceType("native");
        testModel.addSourceMapping("ebay-connector", "ebay", "source-ebay");
        
        server.deployVDB("test", testModel);
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
    public void testEBayTranslator() throws Exception
    {
        final String sql = "call ebaydata.findByKeyword('harry potter phoenix')";     
        
        List<String> results = execute(sql);
        logger.debug("Returned results:" + Arrays.toString(results.toArray()));
        assertTrue("Search results returned from stored procedure", results.size() > 0);
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

