// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.process;

import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.pvm.PvmTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransitionImpl extends CoreModelElement implements PvmTransition {
    private static final long serialVersionUID = 1L;
    protected ActivityImpl source;
    protected ActivityImpl destination;
    protected ProcessDefinitionImpl processDefinition;
    protected List<Integer> waypoints;

    public TransitionImpl(final String id, final ProcessDefinitionImpl processDefinition) {
        super(id);
        this.waypoints = new ArrayList<Integer>();
        this.processDefinition = processDefinition;
    }

    @Override
    public ActivityImpl getSource() {
        return this.source;
    }

    public void setDestination(final ActivityImpl destination) {
        this.destination = destination;
        destination.getIncomingTransitions().add(this);
    }

    @Deprecated
    public void addExecutionListener(final ExecutionListener executionListener) {
        super.addListener("take", executionListener);
    }

    @Deprecated
    public List<ExecutionListener> getExecutionListeners() {
        return (List<ExecutionListener>) super.getListeners("take").stream().map(x -> (ExecutionListener) x).collect(Collectors.toList());
    }

    @Deprecated
    public void setExecutionListeners(final List<ExecutionListener> executionListeners) {
        for (final ExecutionListener executionListener : executionListeners) {
            this.addExecutionListener(executionListener);
        }
    }

    @Override
    public String toString() {
        return "(" + this.source.getId() + ")--" + ((this.id != null) ? (this.id + "-->(") : ">(") + this.destination.getId() + ")";
    }

    @Override
    public PvmProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }

    protected void setSource(final ActivityImpl source) {
        this.source = source;
    }

    @Override
    public PvmActivity getDestination() {
        return this.destination;
    }

    public List<Integer> getWaypoints() {
        return this.waypoints;
    }

    public void setWaypoints(final List<Integer> waypoints) {
        this.waypoints = waypoints;
    }
}
