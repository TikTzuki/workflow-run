// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface DeploymentQueryProperty
{
    public static final QueryProperty DEPLOYMENT_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty DEPLOYMENT_NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty DEPLOY_TIME = new QueryPropertyImpl("DEPLOY_TIME_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
