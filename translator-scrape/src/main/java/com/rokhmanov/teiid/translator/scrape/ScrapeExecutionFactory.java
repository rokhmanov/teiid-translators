package com.rokhmanov.teiid.translator.scrape;

import java.sql.Types;

import org.teiid.language.Call;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Procedure;
import org.teiid.metadata.ProcedureParameter.Type;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.ProcedureExecution;
import org.teiid.translator.Translator;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.TypeFacility;

@Translator(name = "web-scrape")
public class ScrapeExecutionFactory
    extends ExecutionFactory<Object, Object>
{

    @Override
    public boolean isSourceRequired()
    {
        return false;
    }

    @Override
    public boolean isSourceRequiredForMetadata()
    {
        return false;
    }

    @Override
    public ProcedureExecution createProcedureExecution(Call command,
                                                       ExecutionContext executionContext,
                                                       RuntimeMetadata metadata,
                                                       Object connection) throws TranslatorException
    {
        if (command.getProcedureName().equalsIgnoreCase("scrape"))
        {
            return new ScrapeExecution(command);
        }
        if (command.getProcedureName().equalsIgnoreCase("scrapeWS"))
        {
            return new ScrapeWSExecution(command);
        }
        throw new TranslatorException(command.toString());
    }

    @Override
    public void getMetadata(MetadataFactory metadataFactory, Object conn) throws TranslatorException
    {
        final Procedure scrapResults = metadataFactory.addProcedure("scrape");
        metadataFactory.addProcedureParameter("url", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), Type.In, scrapResults);
        metadataFactory.addProcedureParameter("selector", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), Type.In, scrapResults);
        metadataFactory.addProcedureResultSetColumn("id", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResults);
        metadataFactory.addProcedureResultSetColumn("tagname", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResults);
        metadataFactory.addProcedureResultSetColumn("text", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResults);
        metadataFactory.addProcedureResultSetColumn("attributes", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResults);
        metadataFactory.addProcedureResultSetColumn("classnames", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResults);
        metadataFactory.addProcedureResultSetColumn("script_data", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResults);
        metadataFactory.addProcedureResultSetColumn("inner_html", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResults);

        final Procedure scrapResultsWS = metadataFactory.addProcedure("scrapeWS");
        metadataFactory.addProcedureParameter("content", TypeFacility.getDataTypeNameFromSQLType(Types.CLOB), Type.In, scrapResultsWS);
        metadataFactory.addProcedureParameter("selector", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), Type.In, scrapResultsWS);
        metadataFactory.addProcedureResultSetColumn("id", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResultsWS);
        metadataFactory.addProcedureResultSetColumn("tagname", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResultsWS);
        metadataFactory.addProcedureResultSetColumn("text", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResultsWS);
        metadataFactory.addProcedureResultSetColumn("attributes", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResultsWS);
        metadataFactory.addProcedureResultSetColumn("classnames", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResultsWS);
        metadataFactory.addProcedureResultSetColumn("script_data", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResultsWS);
        metadataFactory.addProcedureResultSetColumn("inner_html", TypeFacility.getDataTypeNameFromSQLType(Types.VARCHAR), scrapResultsWS);
    }

}
