// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.exception.NotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import org.zik.bpm.engine.ClassLoadingException;
import java.io.IOException;
import java.lang.reflect.Field;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import org.zik.bpm.engine.ParseException;
import org.zik.bpm.engine.Problem;
import java.util.List;
import org.zik.bpm.engine.impl.util.io.StreamSource;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class EngineUtilLogger extends ProcessEngineLogger
{
    public ProcessEngineException malformedUrlException(final String url, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("001", "The URL '{}' is malformed", new Object[] { url }), cause);
    }
    
    public ProcessEngineException multipleSourcesException(final StreamSource source1, final StreamSource source2) {
        return new ProcessEngineException(this.exceptionMessage("002", "Multiple sources detected, which is invalid. Source 1: '{}', Source 2: {}", new Object[] { source1, source2 }));
    }
    
    public ProcessEngineException parsingFailureException(final String name, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("003", "Could not parse '{}'. {}", new Object[] { name, cause.getMessage() }), cause);
    }
    
    public void logParseWarnings(final String formattedMessage) {
        this.logWarn("004", "Warnings during parsing: {}", new Object[] { formattedMessage });
    }
    
    public ProcessEngineException exceptionDuringParsing(final String string, final String resourceName, final List<Problem> errors, final List<Problem> warnings) {
        return new ParseException(this.exceptionMessage("005", "Could not parse BPMN process. Errors: {}", new Object[] { string }), resourceName, errors, warnings);
    }
    
    public void unableToSetSchemaResource(final Throwable cause) {
        this.logWarn("006", "Setting schema resource failed because of: '{}'", new Object[] { cause.getMessage(), cause });
    }
    
    public ProcessEngineException invalidBitNumber(final int bitNumber) {
        return new ProcessEngineException(this.exceptionMessage("007", "Invalid bit {}. Only 8 bits are supported.", new Object[] { bitNumber }));
    }
    
    public ProcessEngineException exceptionWhileInstantiatingClass(final String className, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("008", "Exception while instantiating class '{}': {}", new Object[] { className, e.getMessage() }), e);
    }
    
    public ProcessEngineException exceptionWhileApplyingFieldDeclatation(final String declName, final String className, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("009", "Exception while applying field declaration '{}' on class '{}': {}", new Object[] { declName, className, e.getMessage() }), e);
    }
    
    public ProcessEngineException incompatibleTypeForFieldDeclaration(final FieldDeclaration declaration, final Object target, final Field field) {
        return new ProcessEngineException(this.exceptionMessage("010", "Incompatible type set on field declaration '{}' for class '{}'. Declared value has type '{}', while expecting '{}'", new Object[] { declaration.getName(), target.getClass().getName(), declaration.getValue().getClass().getName(), field.getType().getName() }));
    }
    
    public ProcessEngineException exceptionWhileReadingStream(final String inputStreamName, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("011", "Exception while reading {} as input stream: {}", new Object[] { inputStreamName, e.getMessage() }), e);
    }
    
    public ProcessEngineException exceptionWhileReadingFile(final String filePath, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("012", "Exception while reading file {}: {}", new Object[] { filePath, e.getMessage() }), e);
    }
    
    public ProcessEngineException exceptionWhileGettingFile(final String filePath, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("013", "Exception while getting file {}: {}", new Object[] { filePath, e.getMessage() }), e);
    }
    
    public ProcessEngineException exceptionWhileWritingToFile(final String filePath, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("014", "Exception while writing to file {}: {}", new Object[] { filePath, e.getMessage() }), e);
    }
    
    public void debugCloseException(final IOException ignore) {
        this.logDebug("015", "Ignored exception on resource close", new Object[] { ignore });
    }
    
    public void debugClassLoading(final String className, final String classLoaderDescription, final ClassLoader classLoader) {
        this.logDebug("016", "Attempting to load class '{}' with {}: {}", new Object[] { className, classLoaderDescription, classLoader });
    }
    
    public ClassLoadingException classLoadingException(final String className, final Throwable throwable) {
        return new ClassLoadingException(this.exceptionMessage("017", "Cannot load class '{}': {}", new Object[] { className, throwable.getMessage() }), className, throwable);
    }
    
    public ProcessEngineException cannotConvertUrlToUri(final URL url, final URISyntaxException e) {
        return new ProcessEngineException(this.exceptionMessage("018", "Cannot convert URL[{}] to URI: {}", new Object[] { url, e.getMessage() }), e);
    }
    
    public ProcessEngineException exceptionWhileInvokingMethod(final String methodName, final Object target, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("019", "Exception while invoking method '{}' on object of type '{}': {}'", new Object[] { methodName, target, e.getMessage() }), e);
    }
    
    public ProcessEngineException unableToAccessField(final Field field, final String name) {
        return new ProcessEngineException(this.exceptionMessage("020", "Unable to access field {} on class {}, access protected", new Object[] { field, name }));
    }
    
    public ProcessEngineException unableToAccessMethod(final String methodName, final String name) {
        return new ProcessEngineException(this.exceptionMessage("021", "Unable to access method {} on class {}, access protected", new Object[] { methodName, name }));
    }
    
    public ProcessEngineException exceptionWhileSettingField(final Field field, final Object object, final Object value, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("022", "Exception while setting value '{}' to field '{}' on object of type '{}': {}", new Object[] { value, field, object.getClass().getName(), e.getMessage() }), e);
    }
    
    public ProcessEngineException ambiguousSetterMethod(final String setterName, final String name) {
        return new ProcessEngineException(this.exceptionMessage("023", "Ambiguous setter: more than one method named {} on class {}, with different parameter types.", new Object[] { setterName, name }));
    }
    
    public NotFoundException cannotFindResource(final String resourcePath) {
        return new NotFoundException(this.exceptionMessage("024", "Unable to find resource at path {}", new Object[] { resourcePath }));
    }
    
    public IllegalStateException notInsideCommandContext(final String operation) {
        return new IllegalStateException(this.exceptionMessage("025", "Operation {} requires active command context. No command context active on thread {}.", new Object[] { operation, Thread.currentThread() }));
    }
    
    public ProcessEngineException exceptionWhileParsingCronExpresison(final String duedateDescription, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("026", "Exception while parsing cron expression '{}': {}", new Object[] { duedateDescription, e.getMessage() }), e);
    }
    
    public ProcessEngineException exceptionWhileResolvingDuedate(final String duedate, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("027", "Exception while resolving duedate '{}': {}", new Object[] { duedate, e.getMessage() }), e);
    }
    
    public Exception cannotParseDuration(final String expressions) {
        return new ProcessEngineException(this.exceptionMessage("028", "Cannot parse duration '{}'.", new Object[] { expressions }));
    }
    
    public void logParsingRetryIntervals(final String intervals, final Exception e) {
        this.logWarn("029", "Exception while parsing retry intervals '{}'", new Object[] { intervals, e.getMessage(), e });
    }
    
    public void logJsonException(final Exception e) {
        this.logDebug("030", "Exception while parsing JSON: {}", new Object[] { e.getMessage(), e });
    }
    
    public void logAccessExternalSchemaNotSupported(final Exception e) {
        this.logDebug("031", "Could not restrict external schema access. This indicates that this is not supported by your JAXP implementation: {}", new Object[] { e.getMessage() });
    }
    
    public void logMissingPropertiesFile(final String file) {
        this.logWarn("032", "Could not find the '{}' file on the classpath. If you have removed it, please restore it.", new Object[] { file });
    }
    
    public ProcessEngineException exceptionDuringFormParsing(final String cause, final String resourceName) {
        return new ProcessEngineException(this.exceptionMessage("033", "Could not parse Camunda Form resource {}. Cause: {}", new Object[] { resourceName, cause }));
    }
    
    public void debugCouldNotResolveCallableElement(final String callingProcessDefinitionId, final String activityId, final Throwable cause) {
        this.logDebug("046", "Could not resolve a callable element for activity {} in process {}. Reason: {}", new Object[] { activityId, callingProcessDefinitionId, cause.getMessage() });
    }
    
    public ProcessEngineException exceptionWhileSettingXxeProcessing(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("047", "Exception while configuring XXE processing: {}", new Object[] { cause.getMessage() }), cause);
    }
}
