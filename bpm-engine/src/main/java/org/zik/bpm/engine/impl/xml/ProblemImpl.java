// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.xml;

import org.zik.bpm.engine.BpmnParseException;
import org.zik.bpm.engine.impl.util.xml.Element;
import java.util.ArrayList;
import org.xml.sax.SAXParseException;
import java.util.List;
import org.zik.bpm.engine.Problem;

public class ProblemImpl implements Problem
{
    protected String message;
    protected int line;
    protected int column;
    protected String mainElementId;
    protected List<String> elementIds;
    
    public ProblemImpl(final SAXParseException e) {
        this.elementIds = new ArrayList<String>();
        this.concatenateErrorMessages(e);
        this.line = e.getLineNumber();
        this.column = e.getColumnNumber();
    }
    
    public ProblemImpl(final String errorMessage, final Element element) {
        this.elementIds = new ArrayList<String>();
        this.message = errorMessage;
        this.extractElementDetails(element);
    }
    
    public ProblemImpl(final String errorMessage, final Element element, final String... elementIds) {
        this(errorMessage, element);
        this.mainElementId = elementIds[0];
        for (final String elementId : elementIds) {
            if (elementId != null && elementId.length() > 0) {
                this.elementIds.add(elementId);
            }
        }
    }
    
    public ProblemImpl(final BpmnParseException exception) {
        this.elementIds = new ArrayList<String>();
        this.concatenateErrorMessages(exception);
        this.extractElementDetails(exception.getElement());
    }
    
    public ProblemImpl(final BpmnParseException exception, final String elementId) {
        this(exception);
        this.mainElementId = elementId;
        if (elementId != null && elementId.length() > 0) {
            this.elementIds.add(elementId);
        }
    }
    
    protected void concatenateErrorMessages(Throwable throwable) {
        while (throwable != null) {
            if (this.message == null) {
                this.message = throwable.getMessage();
            }
            else {
                this.message = this.message + ": " + throwable.getMessage();
            }
            throwable = throwable.getCause();
        }
    }
    
    protected void extractElementDetails(final Element element) {
        if (element != null) {
            this.line = element.getLine();
            this.column = element.getColumn();
            final String id = element.attribute("id");
            if (id != null && id.length() > 0) {
                this.mainElementId = id;
                this.elementIds.add(id);
            }
        }
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public int getLine() {
        return this.line;
    }
    
    @Override
    public int getColumn() {
        return this.column;
    }
    
    @Override
    public String getMainElementId() {
        return this.mainElementId;
    }
    
    @Override
    public List<String> getElementIds() {
        return this.elementIds;
    }
    
    @Override
    public String toString() {
        final StringBuilder string = new StringBuilder();
        if (this.line > 0) {
            string.append(" | line " + this.line);
        }
        if (this.column > 0) {
            string.append(" | column " + this.column);
        }
        return string.toString();
    }
}
