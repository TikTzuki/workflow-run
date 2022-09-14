// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.HashMap;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.Arrays;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.Iterator;
import java.net.URL;
import java.io.InputStream;
import java.util.Map;

public abstract class ReflectUtil
{
    private static final EngineUtilLogger LOG;
    private static final Map<String, String> charEncodings;
    
    public static ClassLoader getClassLoader() {
        ClassLoader loader = getCustomClassLoader();
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return loader;
    }
    
    public static Class<?> loadClass(final String className) {
        Class<?> clazz = null;
        final ClassLoader classLoader = getCustomClassLoader();
        Throwable throwable = null;
        if (classLoader != null) {
            try {
                ReflectUtil.LOG.debugClassLoading(className, "custom classloader", classLoader);
                clazz = Class.forName(className, true, classLoader);
            }
            catch (Throwable t) {
                throwable = t;
            }
        }
        if (clazz == null) {
            try {
                final ClassLoader contextClassloader = ClassLoaderUtil.getContextClassloader();
                if (contextClassloader != null) {
                    ReflectUtil.LOG.debugClassLoading(className, "current thread context classloader", contextClassloader);
                    clazz = Class.forName(className, true, contextClassloader);
                }
            }
            catch (Throwable t) {
                if (throwable == null) {
                    throwable = t;
                }
            }
            if (clazz == null) {
                try {
                    final ClassLoader localClassloader = ClassLoaderUtil.getClassloader(ReflectUtil.class);
                    ReflectUtil.LOG.debugClassLoading(className, "local classloader", localClassloader);
                    clazz = Class.forName(className, true, localClassloader);
                }
                catch (Throwable t) {
                    if (throwable == null) {
                        throwable = t;
                    }
                }
            }
        }
        if (clazz == null) {
            throw ReflectUtil.LOG.classLoadingException(className, throwable);
        }
        return clazz;
    }
    
    public static <T> Class<? extends T> loadClass(final String className, final ClassLoader customClassloader, final Class<T> clazz) throws ClassNotFoundException, ClassCastException {
        if (customClassloader != null) {
            return (Class<? extends T>)customClassloader.loadClass(className);
        }
        return (Class<? extends T>)loadClass(className);
    }
    
    public static InputStream getResourceAsStream(final String name) {
        InputStream resourceStream = null;
        ClassLoader classLoader = getCustomClassLoader();
        if (classLoader != null) {
            resourceStream = classLoader.getResourceAsStream(name);
        }
        if (resourceStream == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            resourceStream = classLoader.getResourceAsStream(name);
            if (resourceStream == null) {
                classLoader = ReflectUtil.class.getClassLoader();
                resourceStream = classLoader.getResourceAsStream(name);
            }
        }
        return resourceStream;
    }
    
    public static URL getResource(final String name) {
        URL url = null;
        ClassLoader classLoader = getCustomClassLoader();
        if (classLoader != null) {
            url = classLoader.getResource(name);
        }
        if (url == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            url = classLoader.getResource(name);
            if (url == null) {
                classLoader = ReflectUtil.class.getClassLoader();
                url = classLoader.getResource(name);
            }
        }
        return url;
    }
    
    public static String getResourceUrlAsString(final String name) {
        String url = getResource(name).toString();
        for (final Map.Entry<String, String> mapping : ReflectUtil.charEncodings.entrySet()) {
            url = url.replaceAll(mapping.getKey(), mapping.getValue());
        }
        return url;
    }
    
    public static URI urlToURI(final URL url) {
        try {
            return url.toURI();
        }
        catch (URISyntaxException e) {
            throw ReflectUtil.LOG.cannotConvertUrlToUri(url, e);
        }
    }
    
    public static Object instantiate(final String className) {
        try {
            final Class<?> clazz = loadClass(className);
            return clazz.newInstance();
        }
        catch (Exception e) {
            throw ReflectUtil.LOG.exceptionWhileInstantiatingClass(className, e);
        }
    }
    
    public static <T> T instantiate(final Class<T> type) {
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            throw ReflectUtil.LOG.exceptionWhileInstantiatingClass(type.getName(), e);
        }
    }
    
    public static <T> T createInstance(final Class<? extends T> clazz) {
        return (T)instantiate(clazz);
    }
    
    public static Object invoke(final Object target, final String methodName, final Object[] args) {
        try {
            final Class<?> clazz = target.getClass();
            final Method method = findMethod(clazz, methodName, Arrays.stream(args).map((Function<? super Object, ?>)Object::getClass).toArray(Class[]::new));
            method.setAccessible(true);
            return method.invoke(target, args);
        }
        catch (Exception e) {
            throw ReflectUtil.LOG.exceptionWhileInvokingMethod(methodName, target, e);
        }
    }
    
    public static Field getField(final String fieldName, final Object object) {
        return getField(fieldName, object.getClass());
    }
    
    public static Field getField(final String fieldName, final Class<?> clazz) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        }
        catch (SecurityException e) {
            throw ReflectUtil.LOG.unableToAccessField(field, clazz.getName());
        }
        catch (NoSuchFieldException e2) {
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(fieldName, superClass);
            }
        }
        return field;
    }
    
    public static void setField(final Field field, final Object object, final Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        }
        catch (Exception e) {
            throw ReflectUtil.LOG.exceptionWhileSettingField(field, object, value, e);
        }
    }
    
    public static Method getSetter(final String fieldName, final Class<?> clazz, final Class<?> fieldType) {
        final String setterName = buildSetterName(fieldName);
        try {
            final Method[] methods2;
            final Method[] methods = methods2 = clazz.getMethods();
            for (final Method method : methods2) {
                if (method.getName().equals(setterName)) {
                    final Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 1 && paramTypes[0].isAssignableFrom(fieldType)) {
                        return method;
                    }
                }
            }
            return null;
        }
        catch (SecurityException e) {
            throw ReflectUtil.LOG.unableToAccessMethod(setterName, clazz.getName());
        }
    }
    
    public static Method getSingleSetter(final String fieldName, final Class<?> clazz) {
        final String setterName = buildSetterName(fieldName);
        try {
            final Method[] methods = clazz.getMethods();
            final List<Method> candidates = new ArrayList<Method>();
            final Set<Class<?>> parameterTypes = new HashSet<Class<?>>();
            for (final Method method : methods) {
                if (method.getName().equals(setterName)) {
                    final Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 1) {
                        candidates.add(method);
                        parameterTypes.add(paramTypes[0]);
                    }
                }
            }
            if (parameterTypes.size() > 1) {
                throw ReflectUtil.LOG.ambiguousSetterMethod(setterName, clazz.getName());
            }
            if (candidates.size() >= 1) {
                return candidates.get(0);
            }
            return null;
        }
        catch (SecurityException e) {
            throw ReflectUtil.LOG.unableToAccessMethod(setterName, clazz.getName());
        }
    }
    
    private static String buildSetterName(final String fieldName) {
        return "set" + Character.toTitleCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
    }
    
    private static Method findMethod(final Class<?> clazz, final String methodName, final Class<?>[] args) {
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && matches(method.getParameterTypes(), args)) {
                return method;
            }
        }
        final Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            return findMethod(superClass, methodName, args);
        }
        return null;
    }
    
    public static Object instantiate(final String className, final Object[] args) {
        final Class<?> clazz = loadClass(className);
        final Constructor<?> constructor = findMatchingConstructor(clazz, args);
        EnsureUtil.ensureNotNull("couldn't find constructor for " + className + " with args " + Arrays.asList(args), "constructor", constructor);
        try {
            return constructor.newInstance(args);
        }
        catch (Exception e) {
            throw ReflectUtil.LOG.exceptionWhileInstantiatingClass(className, e);
        }
    }
    
    private static <T> Constructor<T> findMatchingConstructor(final Class<T> clazz, final Object[] args) {
        for (final Constructor constructor : clazz.getDeclaredConstructors()) {
            if (matches(constructor.getParameterTypes(), Arrays.stream(args).map((Function<? super Object, ?>)Object::getClass).toArray(Class[]::new))) {
                return (Constructor<T>)constructor;
            }
        }
        return null;
    }
    
    private static boolean matches(final Class<?>[] parameterTypes, final Class<?>[] args) {
        if (parameterTypes == null || parameterTypes.length == 0) {
            return args == null || args.length == 0;
        }
        if (args == null || parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; ++i) {
            if (args[i] != null && !parameterTypes[i].isAssignableFrom(args[i])) {
                return false;
            }
        }
        return true;
    }
    
    private static ClassLoader getCustomClassLoader() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration != null) {
            final ClassLoader classLoader = processEngineConfiguration.getClassLoader();
            if (classLoader != null) {
                return classLoader;
            }
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> declaringType, final String methodName, final Class<?>... parameterTypes) {
        return findMethod(declaringType, methodName, parameterTypes);
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        (charEncodings = new HashMap<String, String>()).put("\u00e4", "%C3%A4");
        ReflectUtil.charEncodings.put("\u00f6", "%C3%B6");
        ReflectUtil.charEncodings.put("\u00fc", "%C3%BC");
        ReflectUtil.charEncodings.put("\u00c4", "%C3%84");
        ReflectUtil.charEncodings.put("\u00d6", "%C3%96");
        ReflectUtil.charEncodings.put("\u00dc", "%C3%9C");
    }
}
