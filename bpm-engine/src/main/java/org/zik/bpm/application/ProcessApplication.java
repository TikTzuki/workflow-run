// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import java.lang.annotation.Inherited;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface ProcessApplication {
    public static final String DEFAULT_META_INF_PROCESSES_XML = "META-INF/processes.xml";
    
    String value() default "";
    
    String name() default "";
    
    String[] deploymentDescriptors() default { "META-INF/processes.xml" };
}
