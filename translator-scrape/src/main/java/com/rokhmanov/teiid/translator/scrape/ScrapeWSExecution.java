package com.rokhmanov.teiid.translator.scrape;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.teiid.core.types.ClobType;
import org.teiid.language.Call;
import org.teiid.translator.DataNotAvailableException;
import org.teiid.translator.ProcedureExecution;
import org.teiid.translator.TranslatorException;

public class ScrapeWSExecution
    implements
        ProcedureExecution
{
    private Iterator<Element> iterator;
    private Call command;

    public ScrapeWSExecution(Call command)
    {
        this.command = command;
    }

    public void execute() throws TranslatorException
    {
        ClobType clobContent = (ClobType)command.getArguments().get(0).getArgumentValue().getValue();
        String content;
        try
        {
            content = clobContent.getSubString(1L, (int)clobContent.length());
        } catch (SQLException e)
        {
            throw new TranslatorException(e);
        }
        String selector = (String)command.getArguments().get(1).getArgumentValue().getValue();
        Document doc = Jsoup.parse(content);
        this.iterator = doc.select(selector).listIterator();
    }

    public List<?> next() throws TranslatorException, DataNotAvailableException
    {       
        if (null == this.iterator) return null;                
        while(this.iterator.hasNext())
        {
            return getRow(this.iterator.next());
        }         
        return null;
    }

    
    private List<?> getRow(Element element)
    {
        List<String> result = new ArrayList<String>();
        result.add(element.id());
        result.add(element.tagName());
        result.add(element.text());
        result.add(element.attributes().html());
        result.add(processClassNames(element.classNames()));
        result.add(element.data());
        result.add(element.html());        
        return result;
    }
    
    private String processClassNames(Set<String> classes)
    {
        StringBuilder sb = new StringBuilder();
        for (String str : classes)
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
