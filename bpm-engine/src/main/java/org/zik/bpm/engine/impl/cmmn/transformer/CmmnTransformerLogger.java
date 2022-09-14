// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.transformer;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class CmmnTransformerLogger extends ProcessEngineLogger
{
    public ProcessEngineException transformResourceException(final String name, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("001", "Could not transform resource '{}'.", new Object[] { name }), cause);
    }
    
    public ProcessEngineException parseProcessException(final String name, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("002", "Error while parsing process of resource '{}'.", new Object[] { name }), cause);
    }
    
    public void ignoredSentryWithMissingCondition(final String id) {
        this.logInfo("003", "Sentry with id '{}' will be ignored. Reason: Neither ifPart nor onParts are defined with a condition.", new Object[] { id });
    }
    
    public void ignoredSentryWithInvalidParts(final String id) {
        this.logInfo("004", "Sentry with id '{}' will be ignored. Reason: ifPart and all onParts are not valid.", new Object[] { id });
    }
    
    public void ignoredUnsupportedAttribute(final String attribute, final String element, final String id) {
        this.logInfo("005", "The attribute '{}' based on the element '{}' of the sentry with id '{}' is not supported and will be ignored.", new Object[] { attribute, element, id });
    }
    
    public void multipleIgnoredConditions(final String id) {
        this.logInfo("006", "The ifPart of the sentry with id '{}' has more than one condition. Only the first one will be used and the other conditions will be ignored.", new Object[] { id });
    }
    
    public CmmnTransformException nonMatchingVariableEvents(final String id) {
        return new CmmnTransformException(this.exceptionMessage("007", "The variableOnPart of the sentry with id '{}' must have one valid variable event. ", new Object[] { id }));
    }
    
    public CmmnTransformException emptyVariableName(final String id) {
        return new CmmnTransformException(this.exceptionMessage("008", "The variableOnPart of the sentry with id '{}' must have variable name. ", new Object[] { id }));
    }
}
