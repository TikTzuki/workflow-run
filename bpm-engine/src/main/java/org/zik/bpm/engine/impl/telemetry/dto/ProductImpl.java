// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.telemetry.Internals;
import org.zik.bpm.engine.telemetry.Product;

public class ProductImpl implements Product
{
    protected String name;
    protected String version;
    protected String edition;
    protected InternalsImpl internals;
    
    public ProductImpl(final String name, final String version, final String edition, final InternalsImpl internals) {
        this.name = name;
        this.version = version;
        this.edition = edition;
        this.internals = internals;
    }
    
    public ProductImpl(final ProductImpl other) {
        this(other.name, other.version, other.edition, new InternalsImpl(other.internals));
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
    
    @Override
    public String getEdition() {
        return this.edition;
    }
    
    public void setEdition(final String edition) {
        this.edition = edition;
    }
    
    @Override
    public InternalsImpl getInternals() {
        return this.internals;
    }
    
    public void setInternals(final InternalsImpl internals) {
        this.internals = internals;
    }
}
