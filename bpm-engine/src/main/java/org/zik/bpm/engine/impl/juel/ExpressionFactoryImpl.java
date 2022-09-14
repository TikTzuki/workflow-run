// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.MethodExpression;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.io.InputStream;
import java.io.IOException;
import org.zik.bpm.engine.impl.javax.el.ELException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;
import org.zik.bpm.engine.impl.javax.el.ExpressionFactory;

public class ExpressionFactoryImpl extends ExpressionFactory
{
    public static final String PROP_METHOD_INVOCATIONS = "javax.el.methodInvocations";
    public static final String PROP_VAR_ARGS = "javax.el.varArgs";
    public static final String PROP_NULL_PROPERTIES = "javax.el.nullProperties";
    public static final String PROP_CACHE_SIZE = "javax.el.cacheSize";
    private final TreeStore store;
    private final TypeConverter converter;
    
    public ExpressionFactoryImpl() {
        this(Profile.JEE6);
    }
    
    public ExpressionFactoryImpl(final Profile profile) {
        final Properties properties = this.loadProperties("el.properties");
        this.store = this.createTreeStore(1000, profile, properties);
        this.converter = this.createTypeConverter(properties);
    }
    
    public ExpressionFactoryImpl(final Properties properties) {
        this(Profile.JEE6, properties);
    }
    
    public ExpressionFactoryImpl(final Profile profile, final Properties properties) {
        this.store = this.createTreeStore(1000, profile, properties);
        this.converter = this.createTypeConverter(properties);
    }
    
    public ExpressionFactoryImpl(final Properties properties, final TypeConverter converter) {
        this(Profile.JEE6, properties, converter);
    }
    
    public ExpressionFactoryImpl(final Profile profile, final Properties properties, final TypeConverter converter) {
        this.store = this.createTreeStore(1000, profile, properties);
        this.converter = converter;
    }
    
    public ExpressionFactoryImpl(final TreeStore store) {
        this(store, TypeConverter.DEFAULT);
    }
    
    public ExpressionFactoryImpl(final TreeStore store, final TypeConverter converter) {
        this.store = store;
        this.converter = converter;
    }
    
    private Properties loadDefaultProperties() {
        final String home = System.getProperty("java.home");
        final String path = home + File.separator + "lib" + File.separator + "el.properties";
        final File file = new File(path);
        if (file.exists()) {
            final Properties properties = new Properties();
            InputStream input = null;
            try {
                properties.load(input = new FileInputStream(file));
            }
            catch (IOException e) {
                throw new ELException("Cannot read default EL properties", e);
            }
            finally {
                try {
                    input.close();
                }
                catch (IOException ex) {}
            }
            if (this.getClass().getName().equals(properties.getProperty("javax.el.ExpressionFactory"))) {
                return properties;
            }
        }
        if (this.getClass().getName().equals(System.getProperty("javax.el.ExpressionFactory"))) {
            return System.getProperties();
        }
        return null;
    }
    
    private Properties loadProperties(final String path) {
        final Properties properties = new Properties(this.loadDefaultProperties());
        InputStream input = null;
        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        }
        catch (SecurityException e2) {
            input = ClassLoader.getSystemResourceAsStream(path);
        }
        if (input != null) {
            try {
                properties.load(input);
            }
            catch (IOException e) {
                throw new ELException("Cannot read EL properties", e);
            }
            finally {
                try {
                    input.close();
                }
                catch (IOException ex) {}
            }
        }
        return properties;
    }
    
    private boolean getFeatureProperty(final Profile profile, final Properties properties, final Builder.Feature feature, final String property) {
        return Boolean.valueOf(properties.getProperty(property, String.valueOf(profile.contains(feature))));
    }
    
    protected TreeStore createTreeStore(final int defaultCacheSize, final Profile profile, final Properties properties) {
        TreeBuilder builder = null;
        if (properties == null) {
            builder = this.createTreeBuilder(null, profile.features());
        }
        else {
            final EnumSet<Builder.Feature> features = EnumSet.noneOf(Builder.Feature.class);
            if (this.getFeatureProperty(profile, properties, Builder.Feature.METHOD_INVOCATIONS, "javax.el.methodInvocations")) {
                features.add(Builder.Feature.METHOD_INVOCATIONS);
            }
            if (this.getFeatureProperty(profile, properties, Builder.Feature.VARARGS, "javax.el.varArgs")) {
                features.add(Builder.Feature.VARARGS);
            }
            if (this.getFeatureProperty(profile, properties, Builder.Feature.NULL_PROPERTIES, "javax.el.nullProperties")) {
                features.add(Builder.Feature.NULL_PROPERTIES);
            }
            builder = this.createTreeBuilder(properties, (Builder.Feature[])features.toArray(new Builder.Feature[0]));
        }
        int cacheSize = defaultCacheSize;
        if (properties != null && properties.containsKey("javax.el.cacheSize")) {
            try {
                cacheSize = Integer.parseInt(properties.getProperty("javax.el.cacheSize"));
            }
            catch (NumberFormatException e) {
                throw new ELException("Cannot parse EL property javax.el.cacheSize", e);
            }
        }
        final Cache cache = (cacheSize > 0) ? new Cache(cacheSize) : null;
        return new TreeStore(builder, cache);
    }
    
    protected TypeConverter createTypeConverter(final Properties properties) {
        final Class<?> clazz = this.load(TypeConverter.class, properties);
        if (clazz == null) {
            return TypeConverter.DEFAULT;
        }
        try {
            return TypeConverter.class.cast(clazz.newInstance());
        }
        catch (Exception e) {
            throw new ELException("TypeConverter " + clazz + " could not be instantiated", e);
        }
    }
    
    protected TreeBuilder createTreeBuilder(final Properties properties, final Builder.Feature... features) {
        final Class<?> clazz = this.load(TreeBuilder.class, properties);
        if (clazz == null) {
            return new Builder(features);
        }
        try {
            if (!Builder.class.isAssignableFrom(clazz)) {
                return TreeBuilder.class.cast(clazz.newInstance());
            }
            final Constructor<?> constructor = clazz.getConstructor(Builder.Feature[].class);
            if (constructor != null) {
                return TreeBuilder.class.cast(constructor.newInstance(features));
            }
            if (features == null || features.length == 0) {
                return TreeBuilder.class.cast(clazz.newInstance());
            }
            throw new ELException("Builder " + clazz + " is missing constructor (can't pass features)");
        }
        catch (Exception e) {
            throw new ELException("TreeBuilder " + clazz + " could not be instantiated", e);
        }
    }
    
    private Class<?> load(final Class<?> clazz, final Properties properties) {
        if (properties != null) {
            final String className = properties.getProperty(clazz.getName());
            if (className != null) {
                ClassLoader loader;
                try {
                    loader = Thread.currentThread().getContextClassLoader();
                }
                catch (Exception e) {
                    throw new ELException("Could not get context class loader", e);
                }
                try {
                    return (loader == null) ? Class.forName(className) : loader.loadClass(className);
                }
                catch (ClassNotFoundException e2) {
                    throw new ELException("Class " + className + " not found", e2);
                }
                catch (Exception e) {
                    throw new ELException("Class " + className + " could not be instantiated", e);
                }
            }
        }
        return null;
    }
    
    @Override
    public final Object coerceToType(final Object obj, final Class<?> targetType) {
        return this.converter.convert(obj, targetType);
    }
    
    @Override
    public final ObjectValueExpression createValueExpression(final Object instance, final Class<?> expectedType) {
        return new ObjectValueExpression(this.converter, instance, expectedType);
    }
    
    @Override
    public final TreeValueExpression createValueExpression(final ELContext context, final String expression, final Class<?> expectedType) {
        return new TreeValueExpression(this.store, context.getFunctionMapper(), context.getVariableMapper(), this.converter, expression, expectedType);
    }
    
    @Override
    public final TreeMethodExpression createMethodExpression(final ELContext context, final String expression, final Class<?> expectedReturnType, final Class<?>[] expectedParamTypes) {
        return new TreeMethodExpression(this.store, context.getFunctionMapper(), context.getVariableMapper(), this.converter, expression, expectedReturnType, expectedParamTypes);
    }
    
    public enum Profile
    {
        JEE5(EnumSet.noneOf(Builder.Feature.class)), 
        JEE6(EnumSet.of(Builder.Feature.METHOD_INVOCATIONS, Builder.Feature.VARARGS));
        
        private final EnumSet<Builder.Feature> features;
        
        private Profile(final EnumSet<Builder.Feature> features) {
            this.features = features;
        }
        
        Builder.Feature[] features() {
            return this.features.toArray(new Builder.Feature[this.features.size()]);
        }
        
        boolean contains(final Builder.Feature feature) {
            return this.features.contains(feature);
        }
    }
}
