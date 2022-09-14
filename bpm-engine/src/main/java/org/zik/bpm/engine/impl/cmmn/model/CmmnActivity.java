// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.model;

import org.zik.bpm.engine.impl.core.delegate.CoreActivityBehavior;
import java.util.HashMap;
import java.util.ArrayList;
import org.zik.bpm.engine.delegate.VariableListener;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import java.util.Map;
import java.util.List;
import org.zik.bpm.engine.impl.core.model.CoreActivity;

public class CmmnActivity extends CoreActivity
{
    private static final long serialVersionUID = 1L;
    protected List<CmmnActivity> activities;
    protected Map<String, CmmnActivity> namedActivities;
    protected CmmnElement cmmnElement;
    protected CmmnActivityBehavior activityBehavior;
    protected CmmnCaseDefinition caseDefinition;
    protected CmmnActivity parent;
    protected List<CmmnSentryDeclaration> sentries;
    protected Map<String, CmmnSentryDeclaration> sentryMap;
    protected List<CmmnSentryDeclaration> entryCriteria;
    protected List<CmmnSentryDeclaration> exitCriteria;
    protected Map<String, Map<String, List<VariableListener<?>>>> resolvedVariableListeners;
    protected Map<String, Map<String, List<VariableListener<?>>>> resolvedBuiltInVariableListeners;
    
    public CmmnActivity(final String id, final CmmnCaseDefinition caseDefinition) {
        super(id);
        this.activities = new ArrayList<CmmnActivity>();
        this.namedActivities = new HashMap<String, CmmnActivity>();
        this.sentries = new ArrayList<CmmnSentryDeclaration>();
        this.sentryMap = new HashMap<String, CmmnSentryDeclaration>();
        this.entryCriteria = new ArrayList<CmmnSentryDeclaration>();
        this.exitCriteria = new ArrayList<CmmnSentryDeclaration>();
        this.caseDefinition = caseDefinition;
    }
    
    @Override
    public CmmnActivity createActivity(final String activityId) {
        final CmmnActivity activity = new CmmnActivity(activityId, this.caseDefinition);
        if (activityId != null) {
            this.namedActivities.put(activityId, activity);
        }
        activity.setParent(this);
        this.activities.add(activity);
        return activity;
    }
    
    @Override
    public List<CmmnActivity> getActivities() {
        return this.activities;
    }
    
    @Override
    public CmmnActivity findActivity(final String activityId) {
        return (CmmnActivity)super.findActivity(activityId);
    }
    
    @Override
    public CmmnActivity getChildActivity(final String activityId) {
        return this.namedActivities.get(activityId);
    }
    
    @Override
    public CmmnActivityBehavior getActivityBehavior() {
        return this.activityBehavior;
    }
    
    public void setActivityBehavior(final CmmnActivityBehavior behavior) {
        this.activityBehavior = behavior;
    }
    
    public CmmnActivity getParent() {
        return this.parent;
    }
    
    public void setParent(final CmmnActivity parent) {
        this.parent = parent;
    }
    
    public CmmnCaseDefinition getCaseDefinition() {
        return this.caseDefinition;
    }
    
    public void setCaseDefinition(final CmmnCaseDefinition caseDefinition) {
        this.caseDefinition = caseDefinition;
    }
    
    public CmmnElement getCmmnElement() {
        return this.cmmnElement;
    }
    
    public void setCmmnElement(final CmmnElement cmmnElement) {
        this.cmmnElement = cmmnElement;
    }
    
    public List<CmmnSentryDeclaration> getSentries() {
        return this.sentries;
    }
    
    public CmmnSentryDeclaration getSentry(final String sentryId) {
        return this.sentryMap.get(sentryId);
    }
    
    public void addSentry(final CmmnSentryDeclaration sentry) {
        this.sentryMap.put(sentry.getId(), sentry);
        this.sentries.add(sentry);
    }
    
    public List<CmmnSentryDeclaration> getEntryCriteria() {
        return this.entryCriteria;
    }
    
    public void setEntryCriteria(final List<CmmnSentryDeclaration> entryCriteria) {
        this.entryCriteria = entryCriteria;
    }
    
    public void addEntryCriteria(final CmmnSentryDeclaration entryCriteria) {
        this.entryCriteria.add(entryCriteria);
    }
    
    public List<CmmnSentryDeclaration> getExitCriteria() {
        return this.exitCriteria;
    }
    
    public void setExitCriteria(final List<CmmnSentryDeclaration> exitCriteria) {
        this.exitCriteria = exitCriteria;
    }
    
    public void addExitCriteria(final CmmnSentryDeclaration exitCriteria) {
        this.exitCriteria.add(exitCriteria);
    }
    
    public Map<String, List<VariableListener<?>>> getVariableListeners(final String eventName, final boolean includeCustomListeners) {
        Map<String, Map<String, List<VariableListener<?>>>> listenerCache;
        if (includeCustomListeners) {
            if (this.resolvedVariableListeners == null) {
                this.resolvedVariableListeners = new HashMap<String, Map<String, List<VariableListener<?>>>>();
            }
            listenerCache = this.resolvedVariableListeners;
        }
        else {
            if (this.resolvedBuiltInVariableListeners == null) {
                this.resolvedBuiltInVariableListeners = new HashMap<String, Map<String, List<VariableListener<?>>>>();
            }
            listenerCache = this.resolvedBuiltInVariableListeners;
        }
        Map<String, List<VariableListener<?>>> resolvedListenersForEvent = listenerCache.get(eventName);
        if (resolvedListenersForEvent == null) {
            resolvedListenersForEvent = new HashMap<String, List<VariableListener<?>>>();
            listenerCache.put(eventName, resolvedListenersForEvent);
            for (CmmnActivity currentActivity = this; currentActivity != null; currentActivity = currentActivity.getParent()) {
                List<VariableListener<?>> localListeners = null;
                if (includeCustomListeners) {
                    localListeners = currentActivity.getVariableListenersLocal(eventName);
                }
                else {
                    localListeners = currentActivity.getBuiltInVariableListenersLocal(eventName);
                }
                if (localListeners != null && !localListeners.isEmpty()) {
                    resolvedListenersForEvent.put(currentActivity.getId(), localListeners);
                }
            }
        }
        return resolvedListenersForEvent;
    }
}
