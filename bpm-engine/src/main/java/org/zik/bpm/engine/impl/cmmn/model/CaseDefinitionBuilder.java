// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.model;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.delegate.CaseExecutionListener;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.StageActivityBehavior;
import java.util.ArrayDeque;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import java.util.Deque;

public class CaseDefinitionBuilder
{
    protected CmmnCaseDefinition caseDefinition;
    protected CmmnActivity casePlanModel;
    protected Deque<CmmnActivity> activityStack;
    protected CoreModelElement processElement;
    
    public CaseDefinitionBuilder() {
        this(null);
    }
    
    public CaseDefinitionBuilder(final String caseDefinitionId) {
        this.activityStack = new ArrayDeque<CmmnActivity>();
        this.processElement = this.caseDefinition;
        this.caseDefinition = new CmmnCaseDefinition(caseDefinitionId);
        this.activityStack.push(this.caseDefinition);
        this.createActivity(caseDefinitionId);
        this.behavior(new StageActivityBehavior());
    }
    
    public CaseDefinitionBuilder createActivity(final String id) {
        final CmmnActivity activity = this.activityStack.peek().createActivity(id);
        this.activityStack.push(activity);
        this.processElement = activity;
        return this;
    }
    
    public CaseDefinitionBuilder endActivity() {
        this.activityStack.pop();
        this.processElement = this.activityStack.peek();
        return this;
    }
    
    public CaseDefinitionBuilder behavior(final CmmnActivityBehavior behavior) {
        this.getActivity().setActivityBehavior(behavior);
        return this;
    }
    
    public CaseDefinitionBuilder autoComplete(final boolean autoComplete) {
        this.getActivity().setProperty("autoComplete", autoComplete);
        return this;
    }
    
    protected CmmnActivity getActivity() {
        return this.activityStack.peek();
    }
    
    public CmmnCaseDefinition buildCaseDefinition() {
        return this.caseDefinition;
    }
    
    public CaseDefinitionBuilder listener(final String eventName, final CaseExecutionListener planItemListener) {
        this.activityStack.peek().addListener(eventName, planItemListener);
        return this;
    }
    
    public CaseDefinitionBuilder property(final String name, final Object value) {
        this.activityStack.peek().setProperty(name, value);
        return this;
    }
}
