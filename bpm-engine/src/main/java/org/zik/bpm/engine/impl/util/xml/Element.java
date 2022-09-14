// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.xml;

import java.util.Set;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import java.util.List;
import java.util.Map;

public class Element
{
    protected String uri;
    protected String tagName;
    protected Map<String, Attribute> attributeMap;
    protected int line;
    protected int column;
    protected StringBuilder text;
    protected List<Element> elements;
    
    public Element(final String uri, final String localName, final String qName, final Attributes attributes, final Locator locator) {
        this.attributeMap = new HashMap<String, Attribute>();
        this.text = new StringBuilder();
        this.elements = new ArrayList<Element>();
        this.uri = uri;
        this.tagName = ((uri == null || uri.equals("")) ? qName : localName);
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); ++i) {
                final String attributeUri = attributes.getURI(i);
                final String name = (attributeUri == null || attributeUri.equals("")) ? attributes.getQName(i) : attributes.getLocalName(i);
                final String value = attributes.getValue(i);
                this.attributeMap.put(this.composeMapKey(attributeUri, name), new Attribute(name, value, attributeUri));
            }
        }
        if (locator != null) {
            this.line = locator.getLineNumber();
            this.column = locator.getColumnNumber();
        }
    }
    
    public List<Element> elements(final String tagName) {
        return this.elementsNS((String)null, tagName);
    }
    
    public List<Element> elementsNS(final Namespace nameSpace, final String tagName) {
        List<Element> elementsNS = this.elementsNS(nameSpace.getNamespaceUri(), tagName);
        if (elementsNS.isEmpty() && nameSpace.hasAlternativeUri()) {
            elementsNS = this.elementsNS(nameSpace.getAlternativeUri(), tagName);
        }
        return elementsNS;
    }
    
    protected List<Element> elementsNS(final String nameSpaceUri, final String tagName) {
        final List<Element> selectedElements = new ArrayList<Element>();
        for (final Element element : this.elements) {
            if (tagName.equals(element.getTagName()) && (nameSpaceUri == null || (nameSpaceUri != null && nameSpaceUri.equals(element.getUri())))) {
                selectedElements.add(element);
            }
        }
        return selectedElements;
    }
    
    public Element element(final String tagName) {
        return this.elementNS(new Namespace(null), tagName);
    }
    
    public Element elementNS(final Namespace nameSpace, final String tagName) {
        List<Element> elements = this.elementsNS(nameSpace.getNamespaceUri(), tagName);
        if (elements.size() == 0 && nameSpace.hasAlternativeUri()) {
            elements = this.elementsNS(nameSpace.getAlternativeUri(), tagName);
        }
        if (elements.size() == 0) {
            return null;
        }
        if (elements.size() > 1) {
            throw new ProcessEngineException("Parsing exception: multiple elements with tag name " + tagName + " found");
        }
        return elements.get(0);
    }
    
    public void add(final Element element) {
        this.elements.add(element);
    }
    
    public String attribute(final String name) {
        if (this.attributeMap.containsKey(name)) {
            return this.attributeMap.get(name).getValue();
        }
        return null;
    }
    
    public Set<String> attributes() {
        return this.attributeMap.keySet();
    }
    
    public String attributeNS(final Namespace namespace, final String name) {
        String attribute = this.attribute(this.composeMapKey(namespace.getNamespaceUri(), name));
        if (attribute == null && namespace.hasAlternativeUri()) {
            attribute = this.attribute(this.composeMapKey(namespace.getAlternativeUri(), name));
        }
        return attribute;
    }
    
    public String attribute(final String name, final String defaultValue) {
        if (this.attributeMap.containsKey(name)) {
            return this.attributeMap.get(name).getValue();
        }
        return defaultValue;
    }
    
    public String attributeNS(final Namespace namespace, final String name, final String defaultValue) {
        String attribute = this.attribute(this.composeMapKey(namespace.getNamespaceUri(), name));
        if (attribute == null && namespace.hasAlternativeUri()) {
            attribute = this.attribute(this.composeMapKey(namespace.getAlternativeUri(), name));
        }
        if (attribute == null) {
            return defaultValue;
        }
        return attribute;
    }
    
    protected String composeMapKey(final String attributeUri, final String attributeName) {
        final StringBuilder strb = new StringBuilder();
        if (attributeUri != null && !attributeUri.equals("")) {
            strb.append(attributeUri);
            strb.append(":");
        }
        strb.append(attributeName);
        return strb.toString();
    }
    
    public List<Element> elements() {
        return this.elements;
    }
    
    @Override
    public String toString() {
        return "<" + this.tagName + "...";
    }
    
    public String getUri() {
        return this.uri;
    }
    
    public String getTagName() {
        return this.tagName;
    }
    
    public int getLine() {
        return this.line;
    }
    
    public int getColumn() {
        return this.column;
    }
    
    public void appendText(final String text) {
        this.text.append(text);
    }
    
    public String getText() {
        return this.text.toString();
    }
    
    public void collectIds(final List<String> ids) {
        ids.add(this.attribute("id"));
        for (final Element child : this.elements) {
            child.collectIds(ids);
        }
    }
}
