// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.telemetry.Product;
import org.zik.bpm.engine.impl.util.JsonUtil;
import org.zik.bpm.engine.telemetry.TelemetryData;

public class TelemetryDataImpl implements TelemetryData
{
    protected String installation;
    protected ProductImpl product;
    
    public TelemetryDataImpl(final String installation, final ProductImpl product) {
        this.installation = installation;
        this.product = product;
    }
    
    public TelemetryDataImpl(final TelemetryDataImpl other) {
        this(other.installation, new ProductImpl(other.product));
    }
    
    @Override
    public String getInstallation() {
        return this.installation;
    }
    
    public void setInstallation(final String installation) {
        this.installation = installation;
    }
    
    @Override
    public ProductImpl getProduct() {
        return this.product;
    }
    
    public void setProduct(final ProductImpl product) {
        this.product = product;
    }
    
    public void mergeInternals(final InternalsImpl other) {
        this.product.getInternals().mergeDynamicData(other);
    }
    
    @Override
    public String toString() {
        return JsonUtil.asString(this);
    }
}
