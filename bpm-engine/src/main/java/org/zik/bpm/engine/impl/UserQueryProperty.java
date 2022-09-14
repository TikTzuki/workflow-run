// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface UserQueryProperty
{
    public static final QueryProperty USER_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty FIRST_NAME = new QueryPropertyImpl("FIRST_");
    public static final QueryProperty LAST_NAME = new QueryPropertyImpl("LAST_");
    public static final QueryProperty EMAIL = new QueryPropertyImpl("EMAIL_");
}
