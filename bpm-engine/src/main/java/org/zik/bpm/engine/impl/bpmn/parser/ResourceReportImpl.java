// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.Problem;
import java.util.List;
import org.zik.bpm.engine.ResourceReport;

public class ResourceReportImpl implements ResourceReport
{
    protected String resourceName;
    protected List<Problem> errors;
    protected List<Problem> warnings;
    
    public ResourceReportImpl(final String resourceName, final List<Problem> errors, final List<Problem> warnings) {
        this.errors = new ArrayList<Problem>();
        this.warnings = new ArrayList<Problem>();
        this.resourceName = resourceName;
        this.errors.addAll(errors);
        this.warnings.addAll(warnings);
    }
    
    @Override
    public String getResourceName() {
        return this.resourceName;
    }
    
    @Override
    public List<Problem> getErrors() {
        return this.errors;
    }
    
    @Override
    public List<Problem> getWarnings() {
        return this.warnings;
    }
}
