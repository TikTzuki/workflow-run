// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface ProcessDefinitionQueryProperty
{
    public static final QueryProperty PROCESS_DEFINITION_KEY = new QueryPropertyImpl("KEY_");
    public static final QueryProperty PROCESS_DEFINITION_CATEGORY = new QueryPropertyImpl("CATEGORY_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty PROCESS_DEFINITION_VERSION = new QueryPropertyImpl("VERSION_");
    public static final QueryProperty PROCESS_DEFINITION_NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty DEPLOYMENT_ID = new QueryPropertyImpl("DEPLOYMENT_ID_");
    public static final QueryProperty DEPLOY_TIME = new QueryPropertyImpl("DEPLOY_TIME_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
    public static final QueryProperty VERSION_TAG = new QueryPropertyImpl("VERSION_TAG_");
}
