package com.rokhmanov.teiid.translator.ebay;

import javax.resource.cci.ConnectionFactory;

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

import com.rokhmanov.ebay.EBayConnection;

@Translator(name="ebay", description="EBay Translator")
public class EBayExecutionFactory extends ExecutionFactory<ConnectionFactory, EBayConnection> 
{

    public EBayExecutionFactory()
    {
        setSourceRequiredForMetadata(false);
    }
    
    @Override
    public ProcedureExecution createProcedureExecution(Call command,
                                                       ExecutionContext executionContext,
                                                       RuntimeMetadata metadata,
                                                       EBayConnection connection) throws TranslatorException
    {
        return new EBayExecution(command, connection, metadata, executionContext);
    }

    @Override
    public void getMetadata(MetadataFactory metadataFactory, EBayConnection conn) throws TranslatorException
    {
        Procedure findProc = metadataFactory.addProcedure("findByKeyword");
        metadataFactory.addProcedureParameter("keyword", TypeFacility.RUNTIME_NAMES.STRING, Type.In, findProc);        
        metadataFactory.addProcedureResultSetColumn("itemId", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("title", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("globalId", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("subtitle", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("primaryCategory", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("secondaryCategory", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("galleryURL", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("viewItemURL", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("charityId", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("productId", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("paymentMethod", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("autoPay", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("postalCode", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("location", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("country", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("storeInfo", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("sellerInfo", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("shippingInfo", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("sellingStatus", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("listingInfo", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("returnsAccepted", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("galleryPlusPictureURL", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("compatibility", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("distance", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("condition", TypeFacility.RUNTIME_NAMES.STRING, findProc);
        metadataFactory.addProcedureResultSetColumn("delimiter", TypeFacility.RUNTIME_NAMES.STRING, findProc);
    }

}
