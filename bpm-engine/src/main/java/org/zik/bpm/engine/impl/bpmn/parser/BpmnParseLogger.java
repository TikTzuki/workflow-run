// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class BpmnParseLogger extends ProcessEngineLogger
{
    public void parsingElement(final String elementType, final String elementId) {
        this.logDebug("001", "Parsing element from type '{}' with id '{}'", new Object[] { elementType, elementId });
    }
    
    public void ignoringNonExecutableProcess(final String elementId) {
        this.logInfo("002", "Ignoring non-executable process with id '{}'. Set the attribute isExecutable=\"true\" to deploy this process.", new Object[] { elementId });
    }
    
    public void missingIsExecutableAttribute(final String elementId) {
        this.logInfo("003", "Process with id '{}' has no attribute isExecutable. Better set the attribute explicitly, especially to be compatible with future engine versions which might change the default behavior.", new Object[] { elementId });
    }
    
    public void parsingFailure(final Throwable cause) {
        this.logError("004", "Unexpected Exception with message: {} ", new Object[] { cause.getMessage() });
    }
    
    public ProcessEngineException parsingProcessException(final Exception cause) {
        return new ProcessEngineException(this.exceptionMessage("009", "Error while parsing process. {}.", new Object[] { cause.getMessage() }), cause);
    }
    
    public void exceptionWhileGeneratingProcessDiagram(final Throwable t) {
        this.logError("010", "Error while generating process diagram, image will not be stored in repository", new Object[] { t });
    }
    
    public ProcessEngineException messageEventSubscriptionWithSameNameExists(final String resourceName, final String eventName) {
        throw new ProcessEngineException(this.exceptionMessage("011", "Cannot deploy process definition '{}': there already is a message event subscription for the message with name '{}'.", new Object[] { resourceName, eventName }));
    }
}
