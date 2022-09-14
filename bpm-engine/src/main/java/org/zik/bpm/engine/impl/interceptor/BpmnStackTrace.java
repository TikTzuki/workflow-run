// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.Map;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class BpmnStackTrace
{
    private static final ContextLogger LOG;
    protected List<AtomicOperationInvocation> perfromedInvocations;
    
    public BpmnStackTrace() {
        this.perfromedInvocations = new ArrayList<AtomicOperationInvocation>();
    }
    
    public void printStackTrace(final boolean verbose) {
        if (this.perfromedInvocations.isEmpty()) {
            return;
        }
        final StringWriter writer = new StringWriter();
        writer.write("BPMN Stack Trace:\n");
        if (!verbose) {
            this.logNonVerbose(writer);
        }
        else {
            this.logVerbose(writer);
        }
        BpmnStackTrace.LOG.bpmnStackTrace(writer.toString());
        this.perfromedInvocations.clear();
    }
    
    protected void logNonVerbose(final StringWriter writer) {
        this.writeInvocation(this.perfromedInvocations.get(this.perfromedInvocations.size() - 1), writer);
        final List<Map<String, String>> activityTrace = this.collectActivityTrace();
        this.logActivityTrace(writer, activityTrace);
    }
    
    protected void logVerbose(final StringWriter writer) {
        Collections.reverse(this.perfromedInvocations);
        for (final AtomicOperationInvocation invocation : this.perfromedInvocations) {
            this.writeInvocation(invocation, writer);
        }
    }
    
    protected void logActivityTrace(final StringWriter writer, final List<Map<String, String>> activities) {
        for (int i = 0; i < activities.size(); ++i) {
            if (i != 0) {
                writer.write("\t  ^\n");
                writer.write("\t  |\n");
            }
            writer.write("\t");
            final Map<String, String> activity = activities.get(i);
            final String activityId = activity.get("activityId");
            writer.write(activityId);
            final String activityName = activity.get("activityName");
            if (activityName != null) {
                writer.write(", name=");
                writer.write(activityName);
            }
            writer.write("\n");
        }
    }
    
    protected List<Map<String, String>> collectActivityTrace() {
        final List<Map<String, String>> activityTrace = new ArrayList<Map<String, String>>();
        for (final AtomicOperationInvocation atomicOperationInvocation : this.perfromedInvocations) {
            final String activityId = atomicOperationInvocation.getActivityId();
            if (activityId == null) {
                continue;
            }
            final Map<String, String> activity = new HashMap<String, String>();
            activity.put("activityId", activityId);
            final String activityName = atomicOperationInvocation.getActivityName();
            if (activityName != null) {
                activity.put("activityName", activityName);
            }
            if (!activityTrace.isEmpty() && activity.get("activityId").equals(activityTrace.get(0).get("activityId"))) {
                continue;
            }
            activityTrace.add(0, activity);
        }
        return activityTrace;
    }
    
    public void add(final AtomicOperationInvocation atomicOperationInvocation) {
        this.perfromedInvocations.add(atomicOperationInvocation);
    }
    
    protected void writeInvocation(final AtomicOperationInvocation invocation, final StringWriter writer) {
        writer.write("\t");
        writer.write(invocation.getActivityId());
        writer.write(" (");
        writer.write(invocation.getOperation().getCanonicalName());
        writer.write(", ");
        writer.write(invocation.getExecution().toString());
        if (invocation.isPerformAsync()) {
            writer.write(", ASYNC");
        }
        if (invocation.getApplicationContextName() != null) {
            writer.write(", pa=");
            writer.write(invocation.getApplicationContextName());
        }
        writer.write(")\n");
    }
    
    static {
        LOG = ProcessEngineLogger.CONTEXT_LOGGER;
    }
}
