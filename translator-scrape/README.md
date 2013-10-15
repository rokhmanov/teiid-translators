translator-scrape
=================

This translator allows to reference an arbitrary html pages as relational resources in Teiid federated database.
Two stored procedures provided: "scrape" and "scrapeWS".

### "scrape" stored procedure

Accepts two input varchar parameters:
* "url" - accepts a full URL to a desired page (http/https supported);
* "selector" - a CSS3 selector syntax string;

Output resultset parameters represent the processed attributes (empty if element is not present in attribute):
* "id" - get the id attribute of the html element;
* "tagname" - the name of html tag (e.g. "div");
* "text" - text value of the element, unescaped and no html;
* "attributes" - string of all element attributes, separated by single space;
* "classnames" - string of all element style classnames, separated by space;
* "script-data" - content from inside of "script" tag;
* "inner-html" - string of element's inner html;

The html-manipulation logic based on jsoup.org parser, see jsoup manual for the selector syntax details and a list of supported html processing features.

Example SQL statement (retrieves 10 hyperlinks from Bing search result page after searching for "jboss teiid"):

```
SELECT 
   id, tagname, text, attributes,classnames, script_data, inner_html 
FROM 
   (call scrapedata.scrap('http://www.bing.com/search?q=jboss+teiid','a[href]')) as S 
WHERE 
   upper(text) like '%TEIID%'
```


### "scrapeWS" stored procedure

Accepts two input parameters:
* "content" - CLOB with html document to process;
* "selector" - a CSS3 selector syntax string;

Output resultset is the same as with "scrape" stored procedure above.
* "id" - get the id attribute of the html element;
* "tagname" - the name of html tag (e.g. "div");
* "text" - text value of the element, unescaped and no html;
* "attributes" - string of all element attributes, separated by single space;
* "classnames" - string of all element style classnames, separated by space;
* "script-data" - content from inside of "script" tag;
* "inner-html" - string of element's inner html;

This stored procedure still uses jsoup.org parser, but the actual html content retrieved by built-in Teiid WebService Translator. 
Example SQL statement (retrieves the same 10 hyperlinks as in previous example):

```
SELECT
	h.id, h.tagname, h.text, h.attributes, h.classnames, h.script_data, h.inner_html
FROM 
	(call wsdata.invokeHttp(action=>'GET',endpoint=>'http://www.bing.com/search?q=jboss+teiid')) x,
TABLE
	(call scrapedata.scrapeWS(to_chars(x.result, 'UTF-8'), 'a[href]')) h   
WHERE 
	upper(h.text) like '%TEIID%' 
```

See the query results for both translators by running a unit tests in ScrapeTest.java (powered by Embedded Teiid).


