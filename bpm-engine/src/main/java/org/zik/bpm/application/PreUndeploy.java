// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PreUndeploy {
}
