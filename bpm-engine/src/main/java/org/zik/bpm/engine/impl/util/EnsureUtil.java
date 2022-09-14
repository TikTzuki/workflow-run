// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.context.Context;
import java.lang.reflect.Constructor;
import java.util.regex.Pattern;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Iterator;
import org.zik.bpm.engine.exception.NotValidException;
import java.util.Map;
import java.util.Collection;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.ProcessEngineException;

public final class EnsureUtil
{
    private static final EngineUtilLogger LOG;
    
    public static void ensureNotNull(final String variableName, final Object value) {
        ensureNotNull("", variableName, value);
    }
    
    public static void ensureNotNull(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Object value) {
        ensureNotNull(exceptionClass, null, variableName, value);
    }
    
    public static void ensureNotNull(final String message, final String variableName, final Object value) {
        ensureNotNull(NullValueException.class, message, variableName, value);
    }
    
    public static void ensureNotNull(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Object value) {
        if (value == null) {
            throw generateException(exceptionClass, message, variableName, "is null");
        }
    }
    
    public static void ensureNull(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Object value) {
        if (value != null) {
            throw generateException(exceptionClass, message, variableName, "is not null");
        }
    }
    
    public static void ensureNotNull(final String variableName, final Object... values) {
        ensureNotNull("", variableName, values);
    }
    
    public static void ensureNotNull(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Object... values) {
        ensureNotNull(exceptionClass, null, variableName, values);
    }
    
    public static void ensureNotNull(final String message, final String variableName, final Object... values) {
        ensureNotNull(NullValueException.class, message, variableName, values);
    }
    
    public static void ensureNotNull(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Object... values) {
        if (values == null) {
            throw generateException(exceptionClass, message, variableName, "is null");
        }
        for (final Object value : values) {
            if (value == null) {
                throw generateException(exceptionClass, message, variableName, "contains null value");
            }
        }
    }
    
    public static void ensureNotEmpty(final String variableName, final String value) {
        ensureNotEmpty("", variableName, value);
    }
    
    public static void ensureNotEmpty(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final String value) {
        ensureNotEmpty(exceptionClass, null, variableName, value);
    }
    
    public static void ensureNotEmpty(final String message, final String variableName, final String value) {
        ensureNotEmpty(ProcessEngineException.class, message, variableName, value);
    }
    
    public static void ensureNotEmpty(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final String value) {
        ensureNotNull(exceptionClass, message, variableName, value);
        if (value.trim().isEmpty()) {
            throw generateException(exceptionClass, message, variableName, "is empty");
        }
    }
    
    public static void ensureNotEmpty(final String variableName, final Collection collection) {
        ensureNotEmpty("", variableName, collection);
    }
    
    public static void ensureNotEmpty(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Collection collection) {
        ensureNotEmpty(exceptionClass, null, variableName, collection);
    }
    
    public static void ensureNotEmpty(final String message, final String variableName, final Collection collection) {
        ensureNotEmpty(ProcessEngineException.class, message, variableName, collection);
    }
    
    public static void ensureNotEmpty(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Collection collection) {
        ensureNotNull(exceptionClass, message, variableName, collection);
        if (collection.isEmpty()) {
            throw generateException(exceptionClass, message, variableName, "is empty");
        }
    }
    
    public static void ensureNotEmpty(final String variableName, final Map map) {
        ensureNotEmpty("", variableName, map);
    }
    
    public static void ensureNotEmpty(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Map map) {
        ensureNotEmpty(exceptionClass, null, variableName, map);
    }
    
    public static void ensureNotEmpty(final String message, final String variableName, final Map map) {
        ensureNotEmpty(ProcessEngineException.class, message, variableName, map);
    }
    
    public static void ensureNotEmpty(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Map map) {
        ensureNotNull(exceptionClass, message, variableName, map);
        if (map.isEmpty()) {
            throw generateException(exceptionClass, message, variableName, "is empty");
        }
    }
    
    public static void ensureEquals(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final long obj1, final long obj2) {
        if (obj1 != obj2) {
            throw generateException(exceptionClass, "", variableName, "value differs from expected");
        }
    }
    
    public static void ensureEquals(final String variableName, final long obj1, final long obj2) {
        ensureEquals(ProcessEngineException.class, variableName, obj1, obj2);
    }
    
    public static void ensurePositive(final String variableName, final Long value) {
        ensurePositive("", variableName, value);
    }
    
    public static void ensurePositive(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Long value) {
        ensurePositive(exceptionClass, null, variableName, value);
    }
    
    public static void ensurePositive(final String message, final String variableName, final Long value) {
        ensurePositive(ProcessEngineException.class, message, variableName, value);
    }
    
    public static void ensurePositive(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Long value) {
        ensureNotNull(exceptionClass, variableName, value);
        if (value <= 0L) {
            throw generateException(exceptionClass, message, variableName, "is not greater than 0");
        }
    }
    
    public static void ensureLessThan(final String message, final String variable, final long value1, final long value2) {
        if (value1 >= value2) {
            throw generateException(ProcessEngineException.class, message, variable, "is not less than" + value2);
        }
    }
    
    public static void ensureGreaterThanOrEqual(final String variableName, final long value1, final long value2) {
        ensureGreaterThanOrEqual("", variableName, value1, value2);
    }
    
    public static void ensureGreaterThanOrEqual(final String message, final String variableName, final long value1, final long value2) {
        ensureGreaterThanOrEqual(ProcessEngineException.class, message, variableName, value1, value2);
    }
    
    public static void ensureGreaterThanOrEqual(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final long value1, final long value2) {
        if (value1 < value2) {
            throw generateException(exceptionClass, message, variableName, "is not greater than or equal to " + value2);
        }
    }
    
    public static void ensureInstanceOf(final String variableName, final Object value, final Class<?> expectedClass) {
        ensureInstanceOf("", variableName, value, expectedClass);
    }
    
    public static void ensureInstanceOf(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Object value, final Class<?> expectedClass) {
        ensureInstanceOf(exceptionClass, null, variableName, value, expectedClass);
    }
    
    public static void ensureInstanceOf(final String message, final String variableName, final Object value, final Class<?> expectedClass) {
        ensureInstanceOf(ProcessEngineException.class, message, variableName, value, expectedClass);
    }
    
    public static void ensureInstanceOf(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Object value, final Class<?> expectedClass) {
        ensureNotNull(exceptionClass, message, variableName, value);
        final Class<?> valueClass = value.getClass();
        if (!expectedClass.isAssignableFrom(valueClass)) {
            throw generateException(exceptionClass, message, variableName, "has class " + valueClass.getName() + " and not " + expectedClass.getName());
        }
    }
    
    public static void ensureOnlyOneNotNull(final String message, final Object... values) {
        ensureOnlyOneNotNull(NullValueException.class, message, values);
    }
    
    public static void ensureOnlyOneNotNull(final Class<? extends ProcessEngineException> exceptionClass, final String message, final Object... values) {
        boolean oneNotNull = false;
        for (final Object value : values) {
            if (value != null) {
                if (oneNotNull) {
                    throw generateException(exceptionClass, null, null, message);
                }
                oneNotNull = true;
            }
        }
        if (!oneNotNull) {
            throw generateException(exceptionClass, null, null, message);
        }
    }
    
    public static void ensureAtLeastOneNotNull(final String message, final Object... values) {
        ensureAtLeastOneNotNull(NullValueException.class, message, values);
    }
    
    public static void ensureAtLeastOneNotNull(final Class<? extends ProcessEngineException> exceptionClass, final String message, final Object... values) {
        for (final Object value : values) {
            if (value != null) {
                return;
            }
        }
        throw generateException(exceptionClass, null, null, message);
    }
    
    public static void ensureAtLeastOneNotEmpty(final String message, final String... values) {
        ensureAtLeastOneNotEmpty(ProcessEngineException.class, message, values);
    }
    
    public static void ensureAtLeastOneNotEmpty(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String... values) {
        for (final String value : values) {
            if (value != null && !value.isEmpty()) {
                return;
            }
        }
        throw generateException(exceptionClass, null, null, message);
    }
    
    public static void ensureNotContainsEmptyString(final String variableName, final Collection<String> values) {
        ensureNotContainsEmptyString((String)null, variableName, values);
    }
    
    public static void ensureNotContainsEmptyString(final String message, final String variableName, final Collection<String> values) {
        ensureNotContainsEmptyString(NotValidException.class, message, variableName, values);
    }
    
    public static void ensureNotContainsEmptyString(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Collection<String> values) {
        ensureNotContainsEmptyString(exceptionClass, null, variableName, values);
    }
    
    public static void ensureNotContainsEmptyString(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Collection<String> values) {
        ensureNotNull(exceptionClass, message, variableName, values);
        for (final String value : values) {
            if (value.isEmpty()) {
                throw generateException(exceptionClass, message, variableName, "contains empty string");
            }
        }
    }
    
    public static void ensureNotContainsNull(final String variableName, final Collection<?> values) {
        ensureNotContainsNull((String)null, variableName, values);
    }
    
    public static void ensureNotContainsNull(final String message, final String variableName, final Collection<?> values) {
        ensureNotContainsNull(NullValueException.class, message, variableName, values);
    }
    
    public static void ensureNotContainsNull(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Collection<?> values) {
        ensureNotContainsNull(exceptionClass, null, variableName, values);
    }
    
    public static void ensureNotContainsNull(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Collection<?> values) {
        ensureNotNull(exceptionClass, message, variableName, values.toArray(new Object[values.size()]));
    }
    
    public static void ensureNumberOfElements(final String variableName, final Collection collection, final int elements) {
        ensureNumberOfElements("", variableName, collection, elements);
    }
    
    public static void ensureNumberOfElements(final String message, final String variableName, final Collection collection, final int elements) {
        ensureNumberOfElements(ProcessEngineException.class, message, variableName, collection, elements);
    }
    
    public static void ensureNumberOfElements(final Class<? extends ProcessEngineException> exceptionClass, final String variableName, final Collection collection, final int elements) {
        ensureNumberOfElements(exceptionClass, "", variableName, collection, elements);
    }
    
    public static void ensureNumberOfElements(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String variableName, final Collection collection, final int elements) {
        ensureNotNull(exceptionClass, message, variableName, collection);
        if (collection.size() != elements) {
            throw generateException(exceptionClass, message, variableName, "does not have " + elements + " elements");
        }
    }
    
    public static void ensureValidIndividualResourceId(final String message, final String id) {
        ensureValidIndividualResourceId(ProcessEngineException.class, message, id);
    }
    
    public static void ensureValidIndividualResourceId(final Class<? extends ProcessEngineException> exceptionClass, final String message, final String id) {
        ensureNotNull(exceptionClass, message, "id", id);
        if ("*".equals(id)) {
            throw generateException(exceptionClass, message, "id", "cannot be *. * is a reserved identifier.");
        }
    }
    
    public static void ensureValidIndividualResourceIds(final String message, final Collection<String> ids) {
        ensureValidIndividualResourceIds(ProcessEngineException.class, message, ids);
    }
    
    public static void ensureValidIndividualResourceIds(final Class<? extends ProcessEngineException> exceptionClass, final String message, final Collection<String> ids) {
        ensureNotNull(exceptionClass, message, "id", ids);
        for (final String id : ids) {
            ensureValidIndividualResourceId(exceptionClass, message, id);
        }
    }
    
    public static void ensureWhitelistedResourceId(final CommandContext commandContext, final String resourceType, final String resourceId) {
        final String resourcePattern = determineResourceWhitelistPattern(commandContext.getProcessEngineConfiguration(), resourceType);
        final Pattern PATTERN = Pattern.compile(resourcePattern);
        if (!PATTERN.matcher(resourceId).matches()) {
            throw generateException(ProcessEngineException.class, resourceType + " has an invalid id", "'" + resourceId + "'", "is not a valid resource identifier.");
        }
    }
    
    public static void ensureTrue(final String message, final boolean value) {
        if (!value) {
            throw new ProcessEngineException(message);
        }
    }
    
    public static void ensureFalse(final String message, final boolean value) {
        ensureTrue(message, !value);
    }
    
    protected static String determineResourceWhitelistPattern(final ProcessEngineConfiguration processEngineConfiguration, final String resourceType) {
        String resourcePattern = null;
        if (resourceType.equals("User")) {
            resourcePattern = processEngineConfiguration.getUserResourceWhitelistPattern();
        }
        if (resourceType.equals("Group")) {
            resourcePattern = processEngineConfiguration.getGroupResourceWhitelistPattern();
        }
        if (resourceType.equals("Tenant")) {
            resourcePattern = processEngineConfiguration.getTenantResourceWhitelistPattern();
        }
        if (resourcePattern != null && !resourcePattern.isEmpty()) {
            return resourcePattern;
        }
        return processEngineConfiguration.getGeneralResourceWhitelistPattern();
    }
    
    protected static <T extends ProcessEngineException> T generateException(final Class<T> exceptionClass, final String message, final String variableName, final String description) {
        final String formattedMessage = formatMessage(message, variableName, description);
        try {
            final Constructor<T> constructor = exceptionClass.getConstructor(String.class);
            return constructor.newInstance(formattedMessage);
        }
        catch (Exception e) {
            throw EnsureUtil.LOG.exceptionWhileInstantiatingClass(exceptionClass.getName(), e);
        }
    }
    
    protected static String formatMessage(final String message, final String variableName, final String description) {
        return formatMessageElement(message, ": ") + formatMessageElement(variableName, " ") + description;
    }
    
    protected static String formatMessageElement(final String element, final String delimiter) {
        if (element != null && !element.isEmpty()) {
            return element.concat(delimiter);
        }
        return "";
    }
    
    public static void ensureActiveCommandContext(final String operation) {
        if (Context.getCommandContext() == null) {
            throw EnsureUtil.LOG.notInsideCommandContext(operation);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
