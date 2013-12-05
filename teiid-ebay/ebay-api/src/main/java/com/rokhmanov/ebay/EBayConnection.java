package com.rokhmanov.ebay;

import javax.resource.cci.Connection;

import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;

public interface EBayConnection extends Connection {
	public FindItemsByKeywordsResponse findItemsByKeywords(FindItemsByKeywordsRequest request);
}
