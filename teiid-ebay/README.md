teiid-ebay
=================

A very basic translator for EBay Finding API ( http://developer.ebay.com/DevZone/Finding/ ).
Implemented only findByKeyword API call, but this is enough to issue a query against 
EBay database of active auctions and retrieve result.

### Prerequisites

You should have an active EBay Develpers License, which you can get for free at http://developer.ebay.com.
Also you have to download an actual EBay FindingKitJava archive from EBay Developer website, and extract the finding.jar from lib folder.
I used version 1.12.0 of finding API jar (Built-Date: 2011-04-28 14:10:35), let me know if you have problems with other versions.
For convenience you can put this jar into your local maven repository, see the "version.ebay-finding-java-driver" parameter in parent pom.xml for the reference.

Put your API key to EbayTest.java (see DEVELOPER_KEY static field) and run this class as a unit test to make sure your API key is working.

If everything is fine, you should get a similar result:

```
Returned results:[
|271336881138|Harry Potter and the Order of the Phoenix  (Xbox 360, 2007)|EBAY-US|null|Video Games||http://thumbs3.ebaystatic.com/m/m49aytufjFn4dN70RkI4QMA/140.jpg|http://www.ebay.com/itm/Harry-Potter-and-Order-Phoenix-Xbox-360-2007-/271336881138?pt=Video_Games_Games|null|56274325|PayPal|false|91911|Chula Vista,CA,USA|US||||Active|Auction|false||null||Good|null, 
|171183853715|Harry Potter and the Order of the Phoenix Figures Harry, Hermione, Ron and Map|EBAY-US|null|TV, Movie & Video Games||http://thumbs4.ebaystatic.com/m/maptT7UNML82z5uv64Abg2A/140.jpg|http://www.ebay.com/itm/Harry-Potter-and-Order-Phoenix-Figures-Harry-Hermione-Ron-and-Map-/171183853715?pt=US_Action_Figures|null|152040891|PayPal|false|34119|Naples,FL,USA|US|||9.8 USD|Active|Auction|false||null||New|null
]

```

### Implementation

* translator-ebay - the actual Teiid translator code;
* ebay-api- a wrapper for EBayConnection interface;
* connector-ebay - a JBoss resource adapter. It supposed to get a EBay Developer key as a configuration parameter if translator will be executed in real (non-embedded) Teiid instance.

The EBay Teiid translator is a simple stored procedure, which accepts a keywords separated by space as a single input parameter.
The stored procedure input parameter name is obviously "keywords".

Example:
```
call ebaydata.findByKeyword('harry potter phoenix');

```

Output resultset has several important fields provided by Ebay Finding API:

* "itemId"
* "title"
* "globalId"
* "subtitle"
* "primaryCategory"
* "secondaryCategory"
* "galleryURL"
* "viewItemURL"
* "charityId"
* "productId"
* "paymentMethod"
* "autoPay"
* "postalCode"
* "location"
* "country"
* "storeInfo"
* "sellerInfo"
* "shippingInfo"
* "sellingStatus"
* "listingInfo"
* "returnsAccepted"
* "galleryPlusPictureURL"
* "compatibility"
* "distance"
* "condition"
* "delimiter"

The underlying EBay Finding API is a SOAP webservice, see the FindingService.wsdl from downloaded FindingKitJava.zip for description of each of the returned fields.

