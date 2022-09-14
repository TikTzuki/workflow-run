// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface GroupQueryProperty
{
    public static final QueryProperty GROUP_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty TYPE = new QueryPropertyImpl("TYPE_");
}
