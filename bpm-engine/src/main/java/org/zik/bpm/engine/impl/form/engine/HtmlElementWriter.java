// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.engine;

import java.util.Iterator;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class HtmlElementWriter
{
    protected String tagName;
    protected boolean isSelfClosing;
    protected String textContent;
    protected Map<String, String> attributes;
    
    public HtmlElementWriter(final String tagName) {
        this.attributes = new LinkedHashMap<String, String>();
        this.tagName = tagName;
        this.isSelfClosing = false;
    }
    
    public HtmlElementWriter(final String tagName, final boolean isSelfClosing) {
        this.attributes = new LinkedHashMap<String, String>();
        this.tagName = tagName;
        this.isSelfClosing = isSelfClosing;
    }
    
    public void writeStartTag(final HtmlDocumentBuilder.HtmlWriteContext context) {
        this.writeLeadingWhitespace(context);
        this.writeStartTagOpen(context);
        this.writeAttributes(context);
        this.writeStartTagClose(context);
        this.writeEndLine(context);
    }
    
    public void writeContent(final HtmlDocumentBuilder.HtmlWriteContext context) {
        if (this.textContent != null) {
            this.writeLeadingWhitespace(context);
            this.writeTextContent(context);
            this.writeEndLine(context);
        }
    }
    
    public void writeEndTag(final HtmlDocumentBuilder.HtmlWriteContext context) {
        if (!this.isSelfClosing) {
            this.writeLeadingWhitespace(context);
            this.writeEndTagElement(context);
            this.writeEndLine(context);
        }
    }
    
    protected void writeEndTagElement(final HtmlDocumentBuilder.HtmlWriteContext context) {
        final StringWriter writer = context.getWriter();
        writer.write("</");
        writer.write(this.tagName);
        writer.write(">");
    }
    
    protected void writeTextContent(final HtmlDocumentBuilder.HtmlWriteContext context) {
        final StringWriter writer = context.getWriter();
        writer.write("  ");
        writer.write(this.textContent);
    }
    
    protected void writeStartTagOpen(final HtmlDocumentBuilder.HtmlWriteContext context) {
        final StringWriter writer = context.getWriter();
        writer.write("<");
        writer.write(this.tagName);
    }
    
    protected void writeAttributes(final HtmlDocumentBuilder.HtmlWriteContext context) {
        final StringWriter writer = context.getWriter();
        for (final Map.Entry<String, String> attribute : this.attributes.entrySet()) {
            writer.write(" ");
            writer.write(attribute.getKey());
            if (attribute.getValue() != null) {
                writer.write("=\"");
                final String attributeValue = this.escapeQuotes(attribute.getValue());
                writer.write(attributeValue);
                writer.write("\"");
            }
        }
    }
    
    protected String escapeQuotes(final String attributeValue) {
        final String escapedHtmlQuote = "&quot;";
        final String escapedJavaQuote = "\"";
        return attributeValue.replaceAll(escapedJavaQuote, escapedHtmlQuote);
    }
    
    protected void writeEndLine(final HtmlDocumentBuilder.HtmlWriteContext context) {
        final StringWriter writer = context.getWriter();
        writer.write("\n");
    }
    
    protected void writeStartTagClose(final HtmlDocumentBuilder.HtmlWriteContext context) {
        final StringWriter writer = context.getWriter();
        if (this.isSelfClosing) {
            writer.write(" /");
        }
        writer.write(">");
    }
    
    protected void writeLeadingWhitespace(final HtmlDocumentBuilder.HtmlWriteContext context) {
        final int stackSize = context.getElementStackSize();
        final StringWriter writer = context.getWriter();
        for (int i = 0; i < stackSize; ++i) {
            writer.write("  ");
        }
    }
    
    public HtmlElementWriter attribute(final String name, final String value) {
        this.attributes.put(name, value);
        return this;
    }
    
    public HtmlElementWriter textContent(final String text) {
        if (this.isSelfClosing) {
            throw new IllegalStateException("Self-closing element cannot have text content.");
        }
        this.textContent = text;
        return this;
    }
}
