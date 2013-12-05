package com.rokhmanov.teiid.translator.ebay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.teiid.language.Call;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ProcedureExecution;
import org.teiid.translator.TranslatorException;

import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.rokhmanov.ebay.EBayConnection;

public class EBayExecution
    implements
        ProcedureExecution
{
    private Iterator<SearchItem> iterator;

    private Call                 command;

    private EBayConnection       connection;

    public EBayExecution(Call command,
                         EBayConnection connection,
                         RuntimeMetadata metadata,
                         ExecutionContext executionContext)
    {
        this.connection = connection;
        this.command = command;
    }

    public void execute() throws TranslatorException
    {
        String keywords = (String) command.getArguments()
                                          .get(0)
                                          .getArgumentValue()
                                          .getValue();
        FindItemsByKeywordsRequest request = new FindItemsByKeywordsRequest();
        request.setKeywords(keywords);
        PaginationInput pi = new PaginationInput();
        pi.setEntriesPerPage(2);
        request.setPaginationInput(pi);
        FindItemsByKeywordsResponse result = this.connection.findItemsByKeywords(request);
        this.iterator = result.getSearchResult().getItem().iterator();
    }

    public List<?> next() throws TranslatorException, DataNotAvailableException
    {
        if (null == this.iterator)
            return null;
        while (this.iterator.hasNext())
        {
            return getRow(this.iterator.next());
        }
        return null;
    }

    private List<?> getRow(SearchItem item)
    {
        List<String> result = new ArrayList<String>();
        result.add(item.getItemId());
        result.add(item.getTitle());
        result.add(item.getGlobalId());
        result.add(item.getSubtitle());
        result.add((null == item.getPrimaryCategory())
                                                      ? ""
                                                      : item.getPrimaryCategory()
                                                            .getCategoryName());
        result.add((null == item.getSecondaryCategory())
                                                        ? ""
                                                        : item.getSecondaryCategory()
                                                              .getCategoryName());
        result.add(item.getGalleryURL());
        result.add(item.getViewItemURL());
        result.add(item.getCharityId());
        result.add((null == item.getProductId()) ? "" : item.getProductId()
                                                            .getValue());
        result.add(processCollection(item.getPaymentMethod()));
        result.add(item.isAutoPay() + "");
        result.add(item.getPostalCode());
        result.add(item.getLocation());
        result.add(item.getCountry());
        result.add((null == item.getStoreInfo()) ? "" : item.getStoreInfo()
                                                            .getStoreName());
        result.add((null == item.getSellerInfo())
                                                 ? ""
                                                 : item.getSellerInfo()
                                                       .getSellerUserName());
        result.add(getShippingInfo(item));
        result.add((null == item.getSellingStatus())
                                                    ? ""
                                                    : item.getSellingStatus()
                                                          .getSellingState());
        result.add((null == item.getListingInfo()) ? "" : item.getListingInfo()
                                                              .getListingType());
        result.add(item.isReturnsAccepted() + "");
        result.add(processCollection(item.getGalleryPlusPictureURL()));
        result.add(item.getCompatibility());
        result.add(getDistance(item));
        result.add((null == item.getCondition())
                                                ? ""
                                                : item.getCondition()
                                                      .getConditionDisplayName());
        result.add(item.getDelimiter());
        return result;
    }

    private String getDistance(SearchItem item)
    {
        if (null != item.getDistance())
        {
            return item.getDistance().getValue()
                    + " "
                    + item.getDistance().getUnit();
        }
        return "";
    }

    private String getShippingInfo(SearchItem item)
    {
        if (null != item.getShippingInfo())
        {
            if (null != item.getShippingInfo().getShippingServiceCost())
            {
                return item.getShippingInfo().getShippingServiceCost().getValue()                
                        + " " 
                        + item.getShippingInfo().getShippingServiceCost().getCurrencyId();
            }
        }
        return "";
    }

    private String processCollection(List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        for (String str : list)
        {
            sb.append(str).append(" ");
        }
        return sb.toString().trim();
    }

    public void close()
    {
    }

    public void cancel() throws TranslatorException
    {
    }

    public List<?> getOutputParameterValues() throws TranslatorException
    {
        return Collections.emptyList();
    }

}
