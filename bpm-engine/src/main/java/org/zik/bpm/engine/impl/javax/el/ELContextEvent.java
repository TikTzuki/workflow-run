// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.util.EventObject;

public class ELContextEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    
    public ELContextEvent(final ELContext source) {
        super(source);
    }
    
    public ELContext getELContext() {
        return (ELContext)this.getSource();
    }
}
