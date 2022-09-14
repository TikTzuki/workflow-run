// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.zik.bpm.engine.impl.persistence.entity.Nameable;

public interface ValueFields extends Nameable
{
    String getTextValue();
    
    void setTextValue(final String p0);
    
    String getTextValue2();
    
    void setTextValue2(final String p0);
    
    Long getLongValue();
    
    void setLongValue(final Long p0);
    
    Double getDoubleValue();
    
    void setDoubleValue(final Double p0);
    
    byte[] getByteArrayValue();
    
    void setByteArrayValue(final byte[] p0);
}
