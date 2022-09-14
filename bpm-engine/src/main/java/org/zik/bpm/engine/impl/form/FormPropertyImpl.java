// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import org.zik.bpm.engine.impl.form.handler.FormPropertyHandler;
import org.zik.bpm.engine.form.FormType;
import org.zik.bpm.engine.form.FormProperty;

public class FormPropertyImpl implements FormProperty
{
    protected String id;
    protected String name;
    protected FormType type;
    protected boolean isRequired;
    protected boolean isReadable;
    protected boolean isWritable;
    protected String value;
    
    public FormPropertyImpl(final FormPropertyHandler formPropertyHandler) {
        this.id = formPropertyHandler.getId();
        this.name = formPropertyHandler.getName();
        this.type = formPropertyHandler.getType();
        this.isRequired = formPropertyHandler.isRequired();
        this.isReadable = formPropertyHandler.isReadable();
        this.isWritable = formPropertyHandler.isWritable();
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public FormType getType() {
        return this.type;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    @Override
    public boolean isRequired() {
        return this.isRequired;
    }
    
    @Override
    public boolean isReadable() {
        return this.isReadable;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public boolean isWritable() {
        return this.isWritable;
    }
}
