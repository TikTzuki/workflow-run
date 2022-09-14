// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.stream.Stream;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.Arrays;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;

public class DatabaseUtil
{
    public static boolean checkDatabaseType(final String... databaseTypes) {
        return checkDatabaseType(Context.getCommandContext().getProcessEngineConfiguration(), databaseTypes);
    }
    
    public static boolean checkDatabaseType(final ProcessEngineConfigurationImpl configuration, final String... databaseTypes) {
        final String dbType = configuration.getDatabaseType();
        final Stream<String> stream = Arrays.stream(databaseTypes);
        final String obj = dbType;
        Objects.requireNonNull(obj);
        return stream.anyMatch(obj::equals);
    }
}
