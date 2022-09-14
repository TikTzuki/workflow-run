// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.regex.Matcher;
import java.util.Properties;
import java.util.Iterator;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.util.Map;
import java.util.regex.Pattern;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;

public class PropertyHelper
{
    protected static final ContainerIntegrationLogger LOG;
    public static final String KEBAB_CASE = "-";
    public static final String SNAKE_CASE = "_";
    public static final String CAMEL_CASE = "";
    private static final Pattern PROPERTY_TEMPLATE;
    
    public static boolean getBooleanProperty(final Map<String, String> properties, final String name, final boolean defaultValue) {
        final String value = properties.get(name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
    
    public static Object convertToClass(final String value, final Class<?> clazz) {
        Object propertyValue;
        if (clazz.isAssignableFrom(Integer.TYPE)) {
            propertyValue = Integer.parseInt(value);
        }
        else if (clazz.isAssignableFrom(Long.TYPE)) {
            propertyValue = Long.parseLong(value);
        }
        else if (clazz.isAssignableFrom(Float.TYPE)) {
            propertyValue = Float.parseFloat(value);
        }
        else if (clazz.isAssignableFrom(Boolean.TYPE)) {
            propertyValue = Boolean.parseBoolean(value);
        }
        else {
            propertyValue = value;
        }
        return propertyValue;
    }
    
    public static void applyProperty(final Object configuration, final String key, final String stringValue) {
        final Class<?> configurationClass = configuration.getClass();
        final Method setter = ReflectUtil.getSingleSetter(key, configurationClass);
        if (setter != null) {
            try {
                final Class<?> parameterClass = setter.getParameterTypes()[0];
                final Object value = convertToClass(stringValue, parameterClass);
                setter.invoke(configuration, value);
                return;
            }
            catch (Exception e) {
                throw PropertyHelper.LOG.cannotSetValueForProperty(key, configurationClass.getCanonicalName(), e);
            }
            throw PropertyHelper.LOG.cannotFindSetterForProperty(key, configurationClass.getCanonicalName());
        }
        throw PropertyHelper.LOG.cannotFindSetterForProperty(key, configurationClass.getCanonicalName());
    }
    
    public static void applyProperties(final Object configuration, final Map<String, String> properties, final String namingStrategy) {
        for (final Map.Entry<String, String> property : properties.entrySet()) {
            String key = property.getKey();
            if (!"".equals(namingStrategy)) {
                key = convertToCamelCase(key, namingStrategy);
            }
            applyProperty(configuration, key, property.getValue());
        }
    }
    
    public static void applyProperties(final Object configuration, final Map<String, String> properties) {
        applyProperties(configuration, properties, "");
    }
    
    public static String resolveProperty(final Properties props, final String original) {
        final Matcher matcher = PropertyHelper.PROPERTY_TEMPLATE.matcher(original);
        final StringBuilder buffer = new StringBuilder();
        boolean found = false;
        while (matcher.find()) {
            found = true;
            final String propertyName = matcher.group(2).trim();
            buffer.append(matcher.group(1)).append(props.containsKey(propertyName) ? props.getProperty(propertyName) : "").append(matcher.group(3));
        }
        return found ? buffer.toString() : original;
    }
    
    protected static String convertToCamelCase(String value, final String token) {
        while (value.contains(token)) {
            value = value.replaceFirst(token + "[a-z]", String.valueOf(Character.toUpperCase(value.charAt(value.indexOf(token) + 1))));
        }
        return value;
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
        PROPERTY_TEMPLATE = Pattern.compile("([^\\$]*)\\$\\{(.+?)\\}([^\\$]*)");
    }
}
