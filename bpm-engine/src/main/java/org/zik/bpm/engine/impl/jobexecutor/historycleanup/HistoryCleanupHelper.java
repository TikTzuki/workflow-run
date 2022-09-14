// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import java.util.Map;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.text.ParseException;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class HistoryCleanupHelper
{
    private static final SimpleDateFormat TIME_FORMAT_WITHOUT_SECONDS;
    private static final SimpleDateFormat TIME_FORMAT_WITHOUT_SECONDS_WITH_TIMEZONE;
    private static final SimpleDateFormat DATE_FORMAT_WITHOUT_TIME;
    
    public static boolean isWithinBatchWindow(final Date date, final ProcessEngineConfigurationImpl configuration) {
        if (configuration.getBatchWindowManager().isBatchWindowConfigured(configuration)) {
            final BatchWindow batchWindow = configuration.getBatchWindowManager().getCurrentOrNextBatchWindow(date, configuration);
            return batchWindow != null && batchWindow.isWithin(date);
        }
        return false;
    }
    
    public static synchronized Date parseTimeConfiguration(final String time) throws ParseException {
        final String today = HistoryCleanupHelper.DATE_FORMAT_WITHOUT_TIME.format(ClockUtil.getCurrentTime());
        try {
            return HistoryCleanupHelper.TIME_FORMAT_WITHOUT_SECONDS_WITH_TIMEZONE.parse(today + time);
        }
        catch (ParseException ex) {
            return HistoryCleanupHelper.TIME_FORMAT_WITHOUT_SECONDS.parse(today + time);
        }
    }
    
    private static Integer getHistoryCleanupBatchSize(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getHistoryCleanupBatchSize();
    }
    
    public static void prepareNextBatch(final HistoryCleanupBatch historyCleanupBatch, final CommandContext commandContext) {
        final HistoryCleanupJobHandlerConfiguration configuration = historyCleanupBatch.getConfiguration();
        final Integer batchSize = getHistoryCleanupBatchSize(commandContext);
        final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        final List<String> historicProcessInstanceIds = commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstanceIdsForCleanup(batchSize, configuration.getMinuteFrom(), configuration.getMinuteTo());
        if (historicProcessInstanceIds.size() > 0) {
            historyCleanupBatch.setHistoricProcessInstanceIds(historicProcessInstanceIds);
        }
        if (historyCleanupBatch.size() < batchSize && processEngineConfiguration.isDmnEnabled()) {
            final List<String> historicDecisionInstanceIds = commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstanceIdsForCleanup(batchSize - historyCleanupBatch.size(), configuration.getMinuteFrom(), configuration.getMinuteTo());
            if (historicDecisionInstanceIds.size() > 0) {
                historyCleanupBatch.setHistoricDecisionInstanceIds(historicDecisionInstanceIds);
            }
        }
        if (historyCleanupBatch.size() < batchSize && processEngineConfiguration.isCmmnEnabled()) {
            final List<String> historicCaseInstanceIds = commandContext.getHistoricCaseInstanceManager().findHistoricCaseInstanceIdsForCleanup(batchSize - historyCleanupBatch.size(), configuration.getMinuteFrom(), configuration.getMinuteTo());
            if (historicCaseInstanceIds.size() > 0) {
                historyCleanupBatch.setHistoricCaseInstanceIds(historicCaseInstanceIds);
            }
        }
        final Map<String, Integer> batchOperationsForHistoryCleanup = processEngineConfiguration.getParsedBatchOperationsForHistoryCleanup();
        if (historyCleanupBatch.size() < batchSize && batchOperationsForHistoryCleanup != null && !batchOperationsForHistoryCleanup.isEmpty()) {
            final List<String> historicBatchIds = commandContext.getHistoricBatchManager().findHistoricBatchIdsForCleanup(batchSize - historyCleanupBatch.size(), batchOperationsForHistoryCleanup, configuration.getMinuteFrom(), configuration.getMinuteTo());
            if (historicBatchIds.size() > 0) {
                historyCleanupBatch.setHistoricBatchIds(historicBatchIds);
            }
        }
        final Integer parsedTaskMetricsTimeToLive = processEngineConfiguration.getParsedTaskMetricsTimeToLive();
        if (parsedTaskMetricsTimeToLive != null && historyCleanupBatch.size() < batchSize) {
            final List<String> taskMetricIds = commandContext.getMeterLogManager().findTaskMetricsForCleanup(batchSize - historyCleanupBatch.size(), parsedTaskMetricsTimeToLive, configuration.getMinuteFrom(), configuration.getMinuteTo());
            if (taskMetricIds.size() > 0) {
                historyCleanupBatch.setTaskMetricIds(taskMetricIds);
            }
        }
    }
    
    public static int[][] listMinuteChunks(final int numberOfChunks) {
        final int[][] minuteChunks = new int[numberOfChunks][2];
        final int chunkLength = 60 / numberOfChunks;
        for (int i = 0; i < numberOfChunks; ++i) {
            minuteChunks[i][0] = chunkLength * i;
            minuteChunks[i][1] = chunkLength * (i + 1) - 1;
        }
        minuteChunks[numberOfChunks - 1][1] = 59;
        return minuteChunks;
    }
    
    public static boolean isBatchWindowConfigured(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getBatchWindowManager().isBatchWindowConfigured(commandContext.getProcessEngineConfiguration());
    }
    
    static {
        TIME_FORMAT_WITHOUT_SECONDS = new SimpleDateFormat("yyyy-MM-ddHH:mm");
        TIME_FORMAT_WITHOUT_SECONDS_WITH_TIMEZONE = new SimpleDateFormat("yyyy-MM-ddHH:mmZ");
        DATE_FORMAT_WITHOUT_TIME = new SimpleDateFormat("yyyy-MM-dd");
    }
}
