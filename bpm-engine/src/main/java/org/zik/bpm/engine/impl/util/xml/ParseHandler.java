// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.xml;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import java.util.ArrayDeque;
import java.util.Deque;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

public class ParseHandler extends DefaultHandler
{
    protected String defaultNamespace;
    protected Parse parse;
    protected Locator locator;
    protected Deque<Element> elementStack;
    
    public ParseHandler(final Parse parse) {
        this.elementStack = new ArrayDeque<Element>();
        this.parse = parse;
    }
    
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        final Element element = new Element(uri, localName, qName, attributes, this.locator);
        if (this.elementStack.isEmpty()) {
            this.parse.rootElement = element;
        }
        else {
            this.elementStack.peek().add(element);
        }
        this.elementStack.push(element);
    }
    
    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        this.elementStack.peek().appendText(String.valueOf(ch, start, length));
    }
    
    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        this.elementStack.pop();
    }
    
    @Override
    public void error(final SAXParseException e) {
        this.parse.addError(e);
    }
    
    @Override
    public void fatalError(final SAXParseException e) {
        this.parse.addError(e);
    }
    
    @Override
    public void warning(final SAXParseException e) {
        this.parse.addWarning(e);
    }
    
    @Override
    public void setDocumentLocator(final Locator locator) {
        this.locator = locator;
    }
    
    public void setDefaultNamespace(final String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }
}
