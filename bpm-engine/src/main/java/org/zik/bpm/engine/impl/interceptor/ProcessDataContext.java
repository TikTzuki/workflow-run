// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import java.util.ArrayList;
import java.util.List;
import org.camunda.commons.logging.MdcAccess;
import java.util.ArrayDeque;
import java.util.Deque;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.HashMap;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Map;

public class ProcessDataContext
{
    protected static final String NULL_VALUE = "~NULL_VALUE~";
    protected String mdcPropertyActivityId;
    protected String mdcPropertyApplicationName;
    protected String mdcPropertyBusinessKey;
    protected String mdcPropertyDefinitionId;
    protected String mdcPropertyInstanceId;
    protected String mdcPropertyTenantId;
    protected boolean handleMdc;
    protected ProcessDataStack activityIdStack;
    protected Map<String, ProcessDataStack> mdcDataStacks;
    protected ProcessDataSections sections;
    
    public ProcessDataContext(final ProcessEngineConfigurationImpl configuration) {
        this(configuration, false);
    }
    
    public ProcessDataContext(final ProcessEngineConfigurationImpl configuration, final boolean initFromCurrentMdc) {
        this.handleMdc = false;
        this.mdcDataStacks = new HashMap<String, ProcessDataStack>();
        this.sections = new ProcessDataSections();
        this.mdcPropertyActivityId = configuration.getLoggingContextActivityId();
        this.activityIdStack = new ProcessDataStack(isNotBlank(this.mdcPropertyActivityId) ? this.mdcPropertyActivityId : null);
        if (isNotBlank(this.mdcPropertyActivityId)) {
            this.mdcDataStacks.put(this.mdcPropertyActivityId, this.activityIdStack);
        }
        this.mdcPropertyApplicationName = configuration.getLoggingContextApplicationName();
        if (isNotBlank(this.mdcPropertyApplicationName)) {
            this.mdcDataStacks.put(this.mdcPropertyApplicationName, new ProcessDataStack(this.mdcPropertyApplicationName));
        }
        this.mdcPropertyBusinessKey = configuration.getLoggingContextBusinessKey();
        if (isNotBlank(this.mdcPropertyBusinessKey)) {
            this.mdcDataStacks.put(this.mdcPropertyBusinessKey, new ProcessDataStack(this.mdcPropertyBusinessKey));
        }
        this.mdcPropertyDefinitionId = configuration.getLoggingContextProcessDefinitionId();
        if (isNotBlank(this.mdcPropertyDefinitionId)) {
            this.mdcDataStacks.put(this.mdcPropertyDefinitionId, new ProcessDataStack(this.mdcPropertyDefinitionId));
        }
        this.mdcPropertyInstanceId = configuration.getLoggingContextProcessInstanceId();
        if (isNotBlank(this.mdcPropertyInstanceId)) {
            this.mdcDataStacks.put(this.mdcPropertyInstanceId, new ProcessDataStack(this.mdcPropertyInstanceId));
        }
        this.mdcPropertyTenantId = configuration.getLoggingContextTenantId();
        if (isNotBlank(this.mdcPropertyTenantId)) {
            this.mdcDataStacks.put(this.mdcPropertyTenantId, new ProcessDataStack(this.mdcPropertyTenantId));
        }
        this.handleMdc = !this.mdcDataStacks.isEmpty();
        if (initFromCurrentMdc) {
            final boolean valuePushed;
            this.mdcDataStacks.values().forEach(stack -> {
                valuePushed = stack.pushCurrentValueFromMdc();
                if (valuePushed) {
                    this.sections.addToCurrentSection(stack);
                }
                return;
            });
            this.sections.sealCurrentSection();
        }
    }
    
    public boolean pushSection(final ExecutionEntity execution) {
        if (this.handleMdc && this.hasNoMdcValues()) {
            this.clearMdc();
        }
        final int numSections = this.sections.size();
        this.addToStack(this.activityIdStack, execution.getActivityId());
        this.addToStack(execution.getProcessDefinitionId(), this.mdcPropertyDefinitionId);
        this.addToStack(execution.getProcessInstanceId(), this.mdcPropertyInstanceId);
        this.addToStack(execution.getTenantId(), this.mdcPropertyTenantId);
        if (isNotBlank(this.mdcPropertyApplicationName)) {
            final ProcessApplicationReference currentPa = Context.getCurrentProcessApplication();
            if (currentPa != null) {
                this.addToStack(currentPa.getName(), this.mdcPropertyApplicationName);
            }
        }
        if (isNotBlank(this.mdcPropertyBusinessKey)) {
            this.addToStack(execution.getBusinessKey(), this.mdcPropertyBusinessKey);
        }
        this.sections.sealCurrentSection();
        final boolean newSectionCreated = numSections != this.sections.size();
        return newSectionCreated;
    }
    
    protected boolean hasNoMdcValues() {
        return this.mdcDataStacks.values().stream().allMatch(ProcessDataStack::isEmpty);
    }
    
    public void popSection() {
        this.sections.popCurrentSection();
    }
    
    public void clearMdc() {
        if (this.handleMdc) {
            this.mdcDataStacks.values().forEach(ProcessDataStack::clearMdcProperty);
        }
    }
    
    public void updateMdcFromCurrentValues() {
        if (this.handleMdc) {
            this.mdcDataStacks.values().forEach(ProcessDataStack::updateMdcWithCurrentValue);
        }
    }
    
    public String getLatestActivityId() {
        return this.activityIdStack.getCurrentValue();
    }
    
    protected void addToStack(final String value, final String property) {
        if (!isNotBlank(property)) {
            return;
        }
        final ProcessDataStack stack = this.mdcDataStacks.get(property);
        this.addToStack(stack, value);
    }
    
    protected void addToStack(final ProcessDataStack stack, final String value) {
        final String current = stack.getCurrentValue();
        if (valuesEqual(current, value)) {
            return;
        }
        stack.pushCurrentValue(value);
        this.sections.addToCurrentSection(stack);
    }
    
    protected static boolean isNotBlank(final String property) {
        return property != null && !property.trim().isEmpty();
    }
    
    protected static boolean valuesEqual(final String val1, final String val2) {
        if (isNull(val1)) {
            return val2 == null;
        }
        return val1.equals(val2);
    }
    
    protected static boolean isNull(final String value) {
        return value == null || "~NULL_VALUE~".equals(value);
    }
    
    protected static class ProcessDataStack
    {
        protected String mdcName;
        protected Deque<String> deque;
        
        public ProcessDataStack(final String mdcName) {
            this.deque = new ArrayDeque<String>();
            this.mdcName = mdcName;
        }
        
        public boolean isEmpty() {
            return this.deque.isEmpty();
        }
        
        public String getCurrentValue() {
            return this.deque.peekFirst();
        }
        
        public void pushCurrentValue(final String value) {
            this.deque.addFirst((value != null) ? value : "~NULL_VALUE~");
            this.updateMdcWithCurrentValue();
        }
        
        public boolean pushCurrentValueFromMdc() {
            if (ProcessDataContext.isNotBlank(this.mdcName)) {
                final String mdcValue = MdcAccess.get(this.mdcName);
                this.deque.addFirst((mdcValue != null) ? mdcValue : "~NULL_VALUE~");
                return true;
            }
            return false;
        }
        
        public void removeCurrentValue() {
            this.deque.removeFirst();
            this.updateMdcWithCurrentValue();
        }
        
        public void clearMdcProperty() {
            if (ProcessDataContext.isNotBlank(this.mdcName)) {
                MdcAccess.remove(this.mdcName);
            }
        }
        
        public void updateMdcWithCurrentValue() {
            if (ProcessDataContext.isNotBlank(this.mdcName)) {
                final String currentValue = this.getCurrentValue();
                if (ProcessDataContext.isNull(currentValue)) {
                    MdcAccess.remove(this.mdcName);
                }
                else {
                    MdcAccess.put(this.mdcName, currentValue);
                }
            }
        }
    }
    
    protected static class ProcessDataSections
    {
        protected Deque<List<ProcessDataStack>> sections;
        protected boolean currentSectionSealed;
        
        protected ProcessDataSections() {
            this.sections = new ArrayDeque<List<ProcessDataStack>>();
            this.currentSectionSealed = true;
        }
        
        public void addToCurrentSection(final ProcessDataStack stack) {
            List<ProcessDataStack> currentSection;
            if (this.currentSectionSealed) {
                currentSection = new ArrayList<ProcessDataStack>();
                this.sections.addFirst(currentSection);
                this.currentSectionSealed = false;
            }
            else {
                currentSection = this.sections.peekFirst();
            }
            currentSection.add(stack);
        }
        
        public void popCurrentSection() {
            final List<ProcessDataStack> section = this.sections.pollFirst();
            if (section != null) {
                section.forEach(ProcessDataStack::removeCurrentValue);
            }
            this.currentSectionSealed = true;
        }
        
        public void sealCurrentSection() {
            this.currentSectionSealed = true;
        }
        
        public int size() {
            return this.sections.size();
        }
    }
}
