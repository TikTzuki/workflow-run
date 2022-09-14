// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.entity.repository;

import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.query.QueryProperty;

public interface DecisionRequirementsDefinitionQueryProperty
{
    public static final QueryProperty DECISION_REQUIREMENTS_DEFINITION_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty DECISION_REQUIREMENTS_DEFINITION_KEY = new QueryPropertyImpl("KEY_");
    public static final QueryProperty DECISION_REQUIREMENTS_DEFINITION_NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty DECISION_REQUIREMENTS_DEFINITION_VERSION = new QueryPropertyImpl("VERSION_");
    public static final QueryProperty DECISION_REQUIREMENTS_DEFINITION_CATEGORY = new QueryPropertyImpl("CATEGORY_");
    public static final QueryProperty DEPLOYMENT_ID = new QueryPropertyImpl("DEPLOYMENT_ID_");
    public static final QueryProperty TENANT_ID = new QueryPropertyImpl("TENANT_ID_");
}
