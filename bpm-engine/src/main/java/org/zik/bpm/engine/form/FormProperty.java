// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.form;

@Deprecated
public interface FormProperty
{
    String getId();
    
    String getName();
    
    FormType getType();
    
    String getValue();
    
    boolean isReadable();
    
    boolean isWritable();
    
    boolean isRequired();
}
