// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.process;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.impl.core.model.CoreActivity;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.PvmTransition;

import java.util.*;


public abstract class ScopeImpl extends CoreActivity implements PvmScope {
    private static final long serialVersionUID = 1L;
    protected boolean isSubProcessScope = false;
    protected List<ActivityImpl> flowActivities = new ArrayList();
    protected Map<String, ActivityImpl> namedFlowActivities = new HashMap();
    protected Set<ActivityImpl> eventActivities = new HashSet();
    protected ProcessDefinitionImpl processDefinition;
    protected final Map<String, BacklogErrorCallback> BACKLOG = new HashMap();

    public ScopeImpl(String id, ProcessDefinitionImpl processDefinition) {
        super(id);
        this.processDefinition = processDefinition;
    }

    public ActivityImpl findActivity(String activityId) {
        return (ActivityImpl) super.findActivity(activityId);
    }

    public TransitionImpl findTransition(String transitionId) {
        Iterator var2 = this.flowActivities.iterator();

        while (var2.hasNext()) {
            PvmActivity childActivity = (PvmActivity) var2.next();
            Iterator var4 = childActivity.getOutgoingTransitions().iterator();

            while (var4.hasNext()) {
                PvmTransition transition = (PvmTransition) var4.next();
                if (transitionId.equals(transition.getId())) {
                    return (TransitionImpl) transition;
                }
            }
        }

        var2 = this.flowActivities.iterator();

        TransitionImpl nestedTransition;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            ActivityImpl childActivity = (ActivityImpl) var2.next();
            nestedTransition = childActivity.findTransition(transitionId);
        } while (nestedTransition == null);

        return nestedTransition;
    }

    public ActivityImpl findActivityAtLevelOfSubprocess(String activityId) {
        if (!this.isSubProcessScope()) {
            throw new ProcessEngineException("This is not a sub process scope.");
        } else {
            ActivityImpl activity = this.findActivity(activityId);
            return activity != null && activity.getLevelOfSubprocessScope() == this ? activity : null;
        }
    }

    public ActivityImpl getChildActivity(String activityId) {
        return (ActivityImpl) this.namedFlowActivities.get(activityId);
    }

    public Collection<BacklogErrorCallback> getBacklogErrorCallbacks() {
        return this.BACKLOG.values();
    }

    public boolean isBacklogEmpty() {
        return this.BACKLOG.isEmpty();
    }

    public void addToBacklog(String activityRef, BacklogErrorCallback callback) {
        this.BACKLOG.put(activityRef, callback);
    }

    public ActivityImpl createActivity(String activityId) {
        ActivityImpl activity = new ActivityImpl(activityId, this.processDefinition);
        if (activityId != null) {
            if (this.processDefinition.findActivity(activityId) != null) {
                throw new PvmException("duplicate activity id '" + activityId + "'");
            }

            if (this.BACKLOG.containsKey(activityId)) {
                this.BACKLOG.remove(activityId);
            }

            this.namedFlowActivities.put(activityId, activity);
        }

        activity.flowScope = this;
        this.flowActivities.add(activity);
        return activity;
    }

    public boolean isAncestorFlowScopeOf(ScopeImpl other) {
        for (ScopeImpl otherAncestor = other.getFlowScope(); otherAncestor != null; otherAncestor = otherAncestor.getFlowScope()) {
            if (this == otherAncestor) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(ActivityImpl activity) {
        if (this.namedFlowActivities.containsKey(activity.getId())) {
            return true;
        } else {
            Iterator var2 = this.flowActivities.iterator();

            ActivityImpl nestedActivity;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                nestedActivity = (ActivityImpl) var2.next();
            } while (!nestedActivity.contains(activity));

            return true;
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public List<ExecutionListener> getExecutionListeners(String eventName) {
        throw new RuntimeException("Deprecated");
//        return super.getListeners(eventName);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void addExecutionListener(String eventName, ExecutionListener executionListener) {
        super.addListener(eventName, executionListener);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void addExecutionListener(String eventName, ExecutionListener executionListener, int index) {
        super.addListener(eventName, executionListener, index);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Map<String, List<ExecutionListener>> getExecutionListeners() {
        throw new RuntimeException("Deprecated");
//        return super.getListeners();
    }

    public List<ActivityImpl> getActivities() {
        return this.flowActivities;
    }

    public Set<ActivityImpl> getEventActivities() {
        return this.eventActivities;
    }

    public boolean isSubProcessScope() {
        return this.isSubProcessScope;
    }

    public void setSubProcessScope(boolean isSubProcessScope) {
        this.isSubProcessScope = isSubProcessScope;
    }

    public ProcessDefinitionImpl getProcessDefinition() {
        return this.processDefinition;
    }

    public interface BacklogErrorCallback {
        void callback();
    }
}
