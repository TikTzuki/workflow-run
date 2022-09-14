// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.context.Context;

public class QueryValidators
{
    public static class AdhocQueryValidator<T extends AbstractQuery<?, ?>> implements Validator<T>
    {
        public static final AdhocQueryValidator INSTANCE;
        
        private AdhocQueryValidator() {
        }
        
        @Override
        public void validate(final T query) {
            if (!Context.getProcessEngineConfiguration().isEnableExpressionsInAdhocQueries() && !query.getExpressions().isEmpty()) {
                throw new BadUserRequestException("Expressions are forbidden in adhoc queries. This behavior can be toggled in the process engine configuration");
            }
        }
        
        public static <T extends AbstractQuery<?, ?>> AdhocQueryValidator<T> get() {
            return (AdhocQueryValidator<T>)AdhocQueryValidator.INSTANCE;
        }
        
        static {
            INSTANCE = new AdhocQueryValidator();
        }
    }
    
    public static class StoredQueryValidator<T extends AbstractQuery<?, ?>> implements Validator<T>
    {
        public static final StoredQueryValidator INSTANCE;
        
        private StoredQueryValidator() {
        }
        
        @Override
        public void validate(final T query) {
            if (!Context.getProcessEngineConfiguration().isEnableExpressionsInStoredQueries() && !query.getExpressions().isEmpty()) {
                throw new BadUserRequestException("Expressions are forbidden in stored queries. This behavior can be toggled in the process engine configuration");
            }
        }
        
        public static <T extends AbstractQuery<?, ?>> StoredQueryValidator<T> get() {
            return (StoredQueryValidator<T>)StoredQueryValidator.INSTANCE;
        }
        
        static {
            INSTANCE = new StoredQueryValidator();
        }
    }
}
