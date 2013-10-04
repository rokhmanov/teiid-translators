translator-scrape
=================

This translator allows to reference an arbitrary html pages as relational resources in Teiid federated database.

The translator provides a stored procedure with two input varchar parameters:
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

select 
   id, tagname, text, attributes,classnames, script_data, inner_html 
from 
   (call scrapedata.scrap('http://www.bing.com/search?q=jboss+teiid','a[href]')) as S 
where 
   upper(text) like '%TEIID%'

See the result by running a complete functional ScrapeTest.java


