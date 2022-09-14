// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.test;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface Deployment {
    String[] resources() default {};
}
