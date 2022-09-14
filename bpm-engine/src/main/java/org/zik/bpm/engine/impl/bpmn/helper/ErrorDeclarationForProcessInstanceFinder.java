// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.helper;

import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.bpmn.parser.ErrorEventDefinition;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.tree.TreeVisitor;

public class ErrorDeclarationForProcessInstanceFinder implements TreeVisitor<PvmScope>
{
    protected Exception exception;
    protected String errorCode;
    protected PvmActivity errorHandlerActivity;
    protected ErrorEventDefinition errorEventDefinition;
    protected PvmActivity currentActivity;
    
    public ErrorDeclarationForProcessInstanceFinder(final Exception exception, final String errorCode, final PvmActivity currentActivity) {
        this.exception = exception;
        this.errorCode = errorCode;
        this.currentActivity = currentActivity;
    }
    
    @Override
    public void visit(final PvmScope scope) {
        final List<ErrorEventDefinition> errorEventDefinitions = scope.getProperties().get(BpmnProperties.ERROR_EVENT_DEFINITIONS);
        for (final ErrorEventDefinition errorEventDefinition : errorEventDefinitions) {
            final PvmActivity activityHandler = scope.getProcessDefinition().findActivity(errorEventDefinition.getHandlerActivityId());
            if (!this.isReThrowingErrorEventSubprocess(activityHandler) && ((this.exception != null && errorEventDefinition.catchesException(this.exception)) || (this.exception == null && errorEventDefinition.catchesError(this.errorCode)))) {
                this.errorHandlerActivity = activityHandler;
                this.errorEventDefinition = errorEventDefinition;
                break;
            }
        }
    }
    
    protected boolean isReThrowingErrorEventSubprocess(final PvmActivity activityHandler) {
        final ScopeImpl activityHandlerScope = (ScopeImpl)activityHandler;
        return activityHandlerScope.isAncestorFlowScopeOf((ScopeImpl)this.currentActivity);
    }
    
    public PvmActivity getErrorHandlerActivity() {
        return this.errorHandlerActivity;
    }
    
    public ErrorEventDefinition getErrorEventDefinition() {
        return this.errorEventDefinition;
    }
}
