package com.rokhmanov.teiid.resource.adapter.ebay;

import javax.resource.ResourceException;

import org.teiid.resource.spi.BasicConnection;

import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.rokhmanov.ebay.EBayConnection;

public class EBayConnectionImpl extends BasicConnection implements EBayConnection
{
    FindingServicePortType serviceClient;
    
    public EBayConnectionImpl(EBayManagedConnectionFactory config)
    {
        serviceClient = FindingServiceClientFactory.getServiceClient(config.getClientConfig());
    }

    public FindItemsByKeywordsResponse findItemsByKeywords(FindItemsByKeywordsRequest request)
    {
        return serviceClient.findItemsByKeywords(request);
    }

    public void close() throws ResourceException
    {        

    }

}
