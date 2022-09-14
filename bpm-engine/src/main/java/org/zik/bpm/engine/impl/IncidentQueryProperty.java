// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.QueryProperty;

public interface IncidentQueryProperty
{
    public static final QueryProperty INCIDENT_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty INCIDENT_MESSAGE = new QueryPropertyImpl("INCIDENT_MSG_");
    public static final QueryProperty INCIDENT_TIMESTAMP = new QueryPropertyImpl("INCIDENT_TIMESTAMP_");
    public static final QueryProperty INCIDENT_TYPE = new QueryPropertyImpl("INCIDENT_TYPE_");
    public static final QueryProperty EXECUTION_ID = new QueryPropertyImpl("EXECUTION_ID_");
    public static final QueryProperty ACTIVITY_ID = new QueryPropertyImpl("ACTIVITY_ID_");
    public static final QueryProperty PROCESS_INSTANCE_ID = new QueryPropertyImpl("PROC_INST_ID_");
    public static final QueryProperty PROCESS_DEFINITION_ID = new QueryPropertyImpl("PROC_DEF_ID_");
    public static final QueryProperty CAUSE_INCIDENT_ID = new QueryPropertyImpl("CAUSE_INCIDENT_ID_");
    public static final QueryProperty ROOT_CAUSE_INCIDENT_ID = new QueryPropertyImpl("ROOT_CAUSE_INCIDENT_ID_");
    public static final QueryProperty CONFIGURATION = new QueryPropertyImpl("CONFIGURATION_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
