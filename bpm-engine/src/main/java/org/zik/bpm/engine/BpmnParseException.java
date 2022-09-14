// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.xml.Element;

public class BpmnParseException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    protected Element element;
    
    public BpmnParseException(final String message, final Element element) {
        super(message);
        this.element = element;
    }
    
    public BpmnParseException(final String message, final Element element, final Throwable cause) {
        super(message, cause);
        this.element = element;
    }
    
    public Element getElement() {
        return this.element;
    }
}
