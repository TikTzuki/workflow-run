// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.delegate;

import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;

public abstract class ClassDelegate
{
    protected String className;
    protected List<FieldDeclaration> fieldDeclarations;
    
    public ClassDelegate(final String className, final List<FieldDeclaration> fieldDeclarations) {
        this.className = className;
        this.fieldDeclarations = fieldDeclarations;
    }
    
    public ClassDelegate(final Class<?> clazz, final List<FieldDeclaration> fieldDeclarations) {
        this(clazz.getName(), fieldDeclarations);
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public List<FieldDeclaration> getFieldDeclarations() {
        return this.fieldDeclarations;
    }
}
