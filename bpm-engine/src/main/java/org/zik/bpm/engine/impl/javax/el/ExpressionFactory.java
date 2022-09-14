// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.lang.reflect.Constructor;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public abstract class ExpressionFactory
{
    public static ExpressionFactory newInstance() {
        return newInstance(null);
    }
    
    public static ExpressionFactory newInstance(final Properties properties) {
        ClassLoader classLoader;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        catch (SecurityException e) {
            classLoader = ExpressionFactory.class.getClassLoader();
        }
        String className = null;
        final String serviceId = "META-INF/services/" + ExpressionFactory.class.getName();
        InputStream input = classLoader.getResourceAsStream(serviceId);
        try {
            if (input != null) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                className = reader.readLine();
                reader.close();
            }
        }
        catch (IOException ex) {
            if (input != null) {
                try {
                    input.close();
                }
                catch (Exception ex2) {}
                finally {
                    input = null;
                }
            }
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (Exception ex3) {
                    input = null;
                }
                finally {
                    input = null;
                }
            }
        }
        Label_0463: {
            if (className != null) {
                if (className.trim().length() != 0) {
                    break Label_0463;
                }
            }
            try {
                final String home = System.getProperty("java.home");
                if (home != null) {
                    final String path = home + File.separator + "lib" + File.separator + "el.properties";
                    final File file = new File(path);
                    if (file.exists()) {
                        input = new FileInputStream(file);
                        final Properties props = new Properties();
                        props.load(input);
                        className = props.getProperty(ExpressionFactory.class.getName());
                    }
                }
            }
            catch (IOException ex4) {}
            catch (SecurityException ex5) {}
            finally {
                if (input != null) {
                    try {
                        input.close();
                    }
                    catch (IOException ex6) {
                        input = null;
                    }
                    finally {
                        input = null;
                    }
                }
            }
        }
        Label_0491: {
            if (className != null) {
                if (className.trim().length() != 0) {
                    break Label_0491;
                }
            }
            try {
                className = System.getProperty(ExpressionFactory.class.getName());
            }
            catch (Exception ex7) {}
        }
        if (className == null || className.trim().length() == 0) {
            className = "org.camunda.bpm.engine.impl.juel.ExpressionFactoryImpl";
        }
        return newInstance(properties, className, classLoader);
    }
    
    private static ExpressionFactory newInstance(final Properties properties, final String className, final ClassLoader classLoader) {
        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(className.trim());
            if (!ExpressionFactory.class.isAssignableFrom(clazz)) {
                throw new ELException("Invalid expression factory class: " + clazz.getName());
            }
        }
        catch (ClassNotFoundException e) {
            throw new ELException("Could not find expression factory class", e);
        }
        try {
            if (properties != null) {
                Constructor<?> constructor = null;
                try {
                    constructor = clazz.getConstructor(Properties.class);
                }
                catch (Exception ex) {}
                if (constructor != null) {
                    return (ExpressionFactory)constructor.newInstance(properties);
                }
            }
            return (ExpressionFactory)clazz.newInstance();
        }
        catch (Exception e2) {
            throw new ELException("Could not create expression factory instance", e2);
        }
    }
    
    public abstract Object coerceToType(final Object p0, final Class<?> p1);
    
    public abstract MethodExpression createMethodExpression(final ELContext p0, final String p1, final Class<?> p2, final Class<?>[] p3);
    
    public abstract ValueExpression createValueExpression(final ELContext p0, final String p1, final Class<?> p2);
    
    public abstract ValueExpression createValueExpression(final Object p0, final Class<?> p1);
}
