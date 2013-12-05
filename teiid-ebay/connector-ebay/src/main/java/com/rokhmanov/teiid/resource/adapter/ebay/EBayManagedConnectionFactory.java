package com.rokhmanov.teiid.resource.adapter.ebay;

import javax.resource.ResourceException;

import org.teiid.resource.spi.BasicConnectionFactory;
import org.teiid.resource.spi.BasicManagedConnectionFactory;

import com.ebay.services.client.ClientConfig;

public class EBayManagedConnectionFactory extends BasicManagedConnectionFactory{

    private static final long serialVersionUID = 3224234616856602706L;    
    private String developerKey;
    
    @Override
    public BasicConnectionFactory<EBayConnectionImpl> createConnectionFactory() throws ResourceException
    {
        return new BasicConnectionFactory<EBayConnectionImpl>()
        {
            private static final long serialVersionUID = 2400168490337016706L;

            @Override
            public EBayConnectionImpl getConnection() throws ResourceException
            {
                return new EBayConnectionImpl(EBayManagedConnectionFactory.this);
            }
            
        };
    }

    public String getDeveloperKey()
    {
        return developerKey;
    }

    public void setDeveloperKey(String developerKey)
    {
        this.developerKey = developerKey;
    }

    public ClientConfig getClientConfig()
    {
        ClientConfig config = new ClientConfig();
        config.setApplicationId(getDeveloperKey());
        return config;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime
                 * result
                 + ((developerKey == null) ? 0 : developerKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EBayManagedConnectionFactory other = (EBayManagedConnectionFactory) obj;
        if (developerKey == null)
        {
            if (other.developerKey != null)
                return false;
        } else if (!developerKey.equals(other.developerKey))
            return false;
        return true;
    }

    
}