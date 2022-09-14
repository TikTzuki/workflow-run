// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.identity.Authentication;
import org.zik.bpm.engine.IdentityService;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.BadUserRequestException;

public class QueryMaxResultsLimitUtil
{
    public static void checkMaxResultsLimit(final int resultsCount, final int maxResultsLimit, final boolean isUserAuthenticated) {
        if (isUserAuthenticated && maxResultsLimit < Integer.MAX_VALUE) {
            if (resultsCount == Integer.MAX_VALUE) {
                throw new BadUserRequestException("An unbound number of results is forbidden!");
            }
            if (resultsCount > maxResultsLimit) {
                throw new BadUserRequestException("Max results limit of " + maxResultsLimit + " exceeded!");
            }
        }
    }
    
    public static void checkMaxResultsLimit(final int resultsCount, final ProcessEngineConfigurationImpl processEngineConfig) {
        final int maxResultsLimit = processEngineConfig.getQueryMaxResultsLimit();
        checkMaxResultsLimit(resultsCount, maxResultsLimit, isUserAuthenticated(processEngineConfig));
    }
    
    public static void checkMaxResultsLimit(final int resultsCount) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration == null) {
            throw new ProcessEngineException("Command context unset.");
        }
        checkMaxResultsLimit(resultsCount, getMaxResultsLimit(processEngineConfiguration), isUserAuthenticated(processEngineConfiguration));
    }
    
    protected static boolean isUserAuthenticated(final ProcessEngineConfigurationImpl processEngineConfig) {
        final String userId = getAuthenticatedUserId(processEngineConfig);
        return userId != null && !userId.isEmpty();
    }
    
    protected static String getAuthenticatedUserId(final ProcessEngineConfigurationImpl processEngineConfig) {
        final IdentityService identityService = processEngineConfig.getIdentityService();
        final Authentication currentAuthentication = identityService.getCurrentAuthentication();
        if (currentAuthentication == null) {
            return null;
        }
        return currentAuthentication.getUserId();
    }
    
    protected static int getMaxResultsLimit(final ProcessEngineConfigurationImpl processEngineConfig) {
        return processEngineConfig.getQueryMaxResultsLimit();
    }
}
