// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.model;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class CmmnSentryDeclaration implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String PLAN_ITEM_ON_PART = "planItemOnPart";
    public static final String IF_PART = "ifPart";
    public static final String VARIABLE_ON_PART = "variableOnPart";
    protected String id;
    protected Map<String, List<CmmnOnPartDeclaration>> onPartMap;
    protected List<CmmnOnPartDeclaration> onParts;
    protected List<CmmnVariableOnPartDeclaration> variableOnParts;
    protected CmmnIfPartDeclaration ifPart;
    
    public CmmnSentryDeclaration(final String id) {
        this.onPartMap = new HashMap<String, List<CmmnOnPartDeclaration>>();
        this.onParts = new ArrayList<CmmnOnPartDeclaration>();
        this.variableOnParts = new ArrayList<CmmnVariableOnPartDeclaration>();
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public List<CmmnOnPartDeclaration> getOnParts() {
        return this.onParts;
    }
    
    public List<CmmnOnPartDeclaration> getOnParts(final String sourceId) {
        return this.onPartMap.get(sourceId);
    }
    
    public void addOnPart(final CmmnOnPartDeclaration onPart) {
        final CmmnActivity source = onPart.getSource();
        if (source == null) {
            return;
        }
        final String sourceId = source.getId();
        List<CmmnOnPartDeclaration> onPartDeclarations = this.onPartMap.get(sourceId);
        if (onPartDeclarations == null) {
            onPartDeclarations = new ArrayList<CmmnOnPartDeclaration>();
            this.onPartMap.put(sourceId, onPartDeclarations);
        }
        for (final CmmnOnPartDeclaration onPartDeclaration : onPartDeclarations) {
            if (onPart.getStandardEvent().equals(onPartDeclaration.getStandardEvent())) {
                if (onPartDeclaration.getSentry() == onPart.getSentry()) {
                    return;
                }
                if (onPartDeclaration.getSentry() == null && onPart.getSentry() != null) {
                    onPartDeclaration.setSentry(onPart.getSentry());
                    return;
                }
                continue;
            }
        }
        onPartDeclarations.add(onPart);
        this.onParts.add(onPart);
    }
    
    public void addVariableOnParts(final CmmnVariableOnPartDeclaration variableOnPartDeclaration) {
        this.variableOnParts.add(variableOnPartDeclaration);
    }
    
    public boolean hasVariableOnPart(final String variableEventName, final String variableName) {
        for (final CmmnVariableOnPartDeclaration variableOnPartDeclaration : this.variableOnParts) {
            if (variableOnPartDeclaration.getVariableEvent().equals(variableEventName) && variableOnPartDeclaration.getVariableName().equals(variableName)) {
                return true;
            }
        }
        return false;
    }
    
    public List<CmmnVariableOnPartDeclaration> getVariableOnParts() {
        return this.variableOnParts;
    }
    
    public CmmnIfPartDeclaration getIfPart() {
        return this.ifPart;
    }
    
    public void setIfPart(final CmmnIfPartDeclaration ifPart) {
        this.ifPart = ifPart;
    }
}
