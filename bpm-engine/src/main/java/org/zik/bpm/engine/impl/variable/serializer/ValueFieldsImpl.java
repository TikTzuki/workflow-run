// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

public class ValueFieldsImpl implements ValueFields
{
    protected String text;
    protected String text2;
    protected Long longValue;
    protected Double doubleValue;
    protected byte[] byteArrayValue;
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public String getTextValue() {
        return this.text;
    }
    
    @Override
    public void setTextValue(final String textValue) {
        this.text = textValue;
    }
    
    @Override
    public String getTextValue2() {
        return this.text2;
    }
    
    @Override
    public void setTextValue2(final String textValue2) {
        this.text2 = textValue2;
    }
    
    @Override
    public Long getLongValue() {
        return this.longValue;
    }
    
    @Override
    public void setLongValue(final Long longValue) {
        this.longValue = longValue;
    }
    
    @Override
    public Double getDoubleValue() {
        return this.doubleValue;
    }
    
    @Override
    public void setDoubleValue(final Double doubleValue) {
        this.doubleValue = doubleValue;
    }
    
    @Override
    public byte[] getByteArrayValue() {
        return this.byteArrayValue;
    }
    
    @Override
    public void setByteArrayValue(final byte[] bytes) {
        this.byteArrayValue = bytes;
    }
}
