// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.engine;

import java.util.ArrayDeque;
import java.io.StringWriter;
import java.util.Deque;

public class HtmlDocumentBuilder
{
    protected HtmlWriteContext context;
    protected Deque<HtmlElementWriter> elements;
    protected StringWriter writer;
    
    public HtmlDocumentBuilder(final HtmlElementWriter documentElement) {
        this.context = new HtmlWriteContext();
        this.elements = new ArrayDeque<HtmlElementWriter>();
        this.writer = new StringWriter();
        this.startElement(documentElement);
    }
    
    public HtmlDocumentBuilder startElement(final HtmlElementWriter renderer) {
        renderer.writeStartTag(this.context);
        this.elements.push(renderer);
        return this;
    }
    
    public HtmlDocumentBuilder endElement() {
        final HtmlElementWriter renderer = this.elements.pop();
        renderer.writeContent(this.context);
        renderer.writeEndTag(this.context);
        return this;
    }
    
    public String getHtmlString() {
        return this.writer.toString();
    }
    
    public class HtmlWriteContext
    {
        public StringWriter getWriter() {
            return HtmlDocumentBuilder.this.writer;
        }
        
        public int getElementStackSize() {
            return HtmlDocumentBuilder.this.elements.size();
        }
    }
}
