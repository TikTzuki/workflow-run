// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.util.ArrayList;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.ResourceReport;
import org.zik.bpm.engine.impl.bpmn.parser.ResourceReportImpl;
import java.util.List;

public class ParseException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    protected List<ResourceReport> resorceReports;
    
    public ParseException(final String exceptionMessage, final String resource, final List<Problem> errors, final List<Problem> warnings) {
        super(exceptionMessage);
        final ResourceReportImpl resourceReport = new ResourceReportImpl(resource, errors, warnings);
        final List<ResourceReport> reports = new ArrayList<ResourceReport>();
        reports.add(resourceReport);
        this.resorceReports = reports;
    }
    
    public List<ResourceReport> getResorceReports() {
        return this.resorceReports;
    }
}
