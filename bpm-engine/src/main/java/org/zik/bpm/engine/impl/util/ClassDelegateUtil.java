// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.zik.bpm.engine.ArtifactFactory;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;

public class ClassDelegateUtil
{
    private static final EngineUtilLogger LOG;
    
    public static Object instantiateDelegate(final Class<?> clazz, final List<FieldDeclaration> fieldDeclarations) {
        return instantiateDelegate(clazz.getName(), fieldDeclarations);
    }
    
    public static Object instantiateDelegate(final String className, final List<FieldDeclaration> fieldDeclarations) {
        final ArtifactFactory artifactFactory = Context.getProcessEngineConfiguration().getArtifactFactory();
        try {
            final Class<?> clazz = ReflectUtil.loadClass(className);
            final Object object = artifactFactory.getArtifact(clazz);
            applyFieldDeclaration(fieldDeclarations, object);
            return object;
        }
        catch (Exception e) {
            throw ClassDelegateUtil.LOG.exceptionWhileInstantiatingClass(className, e);
        }
    }
    
    public static void applyFieldDeclaration(final List<FieldDeclaration> fieldDeclarations, final Object target) {
        if (fieldDeclarations != null) {
            for (final FieldDeclaration declaration : fieldDeclarations) {
                applyFieldDeclaration(declaration, target);
            }
        }
    }
    
    public static void applyFieldDeclaration(final FieldDeclaration declaration, final Object target) {
        final Method setterMethod = ReflectUtil.getSetter(declaration.getName(), target.getClass(), declaration.getValue().getClass());
        if (setterMethod != null) {
            try {
                setterMethod.invoke(target, declaration.getValue());
                return;
            }
            catch (Exception e) {
                throw ClassDelegateUtil.LOG.exceptionWhileApplyingFieldDeclatation(declaration.getName(), target.getClass().getName(), e);
            }
        }
        final Field field = ReflectUtil.getField(declaration.getName(), target);
        EnsureUtil.ensureNotNull("Field definition uses unexisting field '" + declaration.getName() + "' on class " + target.getClass().getName(), "field", field);
        if (!fieldTypeCompatible(declaration, field)) {
            throw ClassDelegateUtil.LOG.incompatibleTypeForFieldDeclaration(declaration, target, field);
        }
        ReflectUtil.setField(field, target, declaration.getValue());
    }
    
    public static boolean fieldTypeCompatible(final FieldDeclaration declaration, final Field field) {
        return declaration.getValue() == null || field.getType().isAssignableFrom(declaration.getValue().getClass());
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
