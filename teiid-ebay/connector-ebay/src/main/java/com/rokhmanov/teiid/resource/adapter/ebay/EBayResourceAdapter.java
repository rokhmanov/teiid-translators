package com.rokhmanov.teiid.resource.adapter.ebay;

import org.teiid.resource.spi.BasicResourceAdapter;

public class EBayResourceAdapter
    extends BasicResourceAdapter
{

    @Override
    public int hashCode()
    {
        return super.hashCode();
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
        return true;
    }

}