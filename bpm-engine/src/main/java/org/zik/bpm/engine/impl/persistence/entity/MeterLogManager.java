// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.impl.Direction;
import org.zik.bpm.engine.impl.QueryPropertyImpl;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ChronoUnit;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Collections;
import java.util.HashMap;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Iterator;
import java.util.Date;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.management.MetricIntervalValue;
import java.util.List;
import org.zik.bpm.engine.impl.metrics.Meter;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.metrics.MetricsQueryImpl;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class MeterLogManager extends AbstractManager
{
    public static final String SELECT_METER_INTERVAL = "selectMeterLogAggregatedByTimeInterval";
    public static final String SELECT_METER_SUM = "selectMeterLogSum";
    public static final String DELETE_ALL_METER = "deleteAllMeterLogEntries";
    public static final String DELETE_ALL_METER_BY_TIMESTAMP_AND_REPORTER = "deleteMeterLogEntriesByTimestampAndReporter";
    public static final String SELECT_UNIQUE_TASK_WORKER = "selectUniqueTaskWorkerCount";
    public static final String SELECT_TASK_METER_FOR_CLEANUP = "selectTaskMetricIdsForCleanup";
    public static final String DELETE_TASK_METER_BY_TIMESTAMP = "deleteTaskMeterLogEntriesByTimestamp";
    public static final String DELETE_TASK_METER_BY_REMOVAL_TIME = "deleteTaskMetricsByRemovalTime";
    public static final String DELETE_TASK_METER_BY_IDS = "deleteTaskMeterLogEntriesByIds";
    
    public void insert(final MeterLogEntity meterLogEntity) {
        this.getDbEntityManager().insert(meterLogEntity);
    }
    
    public Long executeSelectSum(final MetricsQueryImpl query) {
        Long result = (Long)this.getDbEntityManager().selectOne("selectMeterLogSum", query);
        result = ((result != null) ? result : 0L);
        if (this.shouldAddCurrentUnloggedCount(query)) {
            final Meter meter = Context.getProcessEngineConfiguration().getMetricsRegistry().getDbMeterByName(query.getName());
            if (meter != null) {
                result += meter.get();
            }
        }
        return result;
    }
    
    public List<MetricIntervalValue> executeSelectInterval(final MetricsQueryImpl query) {
        List<MetricIntervalValue> intervalResult = (List<MetricIntervalValue>)this.getDbEntityManager().selectList("selectMeterLogAggregatedByTimeInterval", query);
        intervalResult = ((intervalResult != null) ? intervalResult : new ArrayList<MetricIntervalValue>());
        final String reporterId = Context.getProcessEngineConfiguration().getDbMetricsReporter().getMetricsCollectionTask().getReporter();
        if (!intervalResult.isEmpty() && this.isEndTimeAfterLastReportInterval(query) && reporterId != null) {
            final Map<String, Meter> metrics = Context.getProcessEngineConfiguration().getMetricsRegistry().getDbMeters();
            final String queryName = query.getName();
            if (queryName != null) {
                final MetricIntervalEntity intervalEntity = intervalResult.get(0);
                long entityValue = intervalEntity.getValue();
                if (metrics.get(queryName) != null) {
                    entityValue += metrics.get(queryName).get();
                }
                intervalEntity.setValue(entityValue);
            }
            else {
                final Set<String> metricNames = metrics.keySet();
                final Date lastIntervalTimestamp = intervalResult.get(0).getTimestamp();
                for (final String metricName : metricNames) {
                    final MetricIntervalEntity entity = new MetricIntervalEntity(lastIntervalTimestamp, metricName, reporterId);
                    final int idx = intervalResult.indexOf(entity);
                    if (idx >= 0) {
                        final MetricIntervalEntity intervalValue = intervalResult.get(idx);
                        intervalValue.setValue(intervalValue.getValue() + metrics.get(metricName).get());
                    }
                }
            }
        }
        return intervalResult;
    }
    
    protected boolean isEndTimeAfterLastReportInterval(final MetricsQueryImpl query) {
        final long reportingIntervalInSeconds = Context.getProcessEngineConfiguration().getDbMetricsReporter().getReportingIntervalInSeconds();
        return query.getEndDate() == null || query.getEndDateMilliseconds() >= ClockUtil.getCurrentTime().getTime() - 1000L * reportingIntervalInSeconds;
    }
    
    protected boolean shouldAddCurrentUnloggedCount(final MetricsQueryImpl query) {
        return query.getName() != null && this.isEndTimeAfterLastReportInterval(query);
    }
    
    public void deleteAll() {
        this.getDbEntityManager().delete(MeterLogEntity.class, "deleteAllMeterLogEntries", null);
    }
    
    public void deleteByTimestampAndReporter(final Date timestamp, final String reporter) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        if (timestamp != null) {
            parameters.put("milliseconds", timestamp.getTime());
        }
        parameters.put("reporter", reporter);
        this.getDbEntityManager().delete(MeterLogEntity.class, "deleteMeterLogEntriesByTimestampAndReporter", parameters);
    }
    
    public long findUniqueTaskWorkerCount(final Date startTime, final Date endTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("startTime", startTime);
        parameters.put("endTime", endTime);
        return (long)this.getDbEntityManager().selectOne("selectUniqueTaskWorkerCount", parameters);
    }
    
    public void deleteTaskMetricsByTimestamp(final Date timestamp) {
        final Map<String, Object> parameters = (Map<String, Object>)Collections.singletonMap("timestamp", timestamp);
        this.getDbEntityManager().delete(TaskMeterLogEntity.class, "deleteTaskMeterLogEntriesByTimestamp", parameters);
    }
    
    public void deleteTaskMetricsById(final List<String> taskMetricIds) {
        this.getDbEntityManager().deletePreserveOrder(TaskMeterLogEntity.class, "deleteTaskMeterLogEntriesByIds", taskMetricIds);
    }
    
    public DbOperation deleteTaskMetricsByRemovalTime(final Date currentTimestamp, final Integer timeToLive, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        final Date removalTime = Date.from(currentTimestamp.toInstant().minus((long)timeToLive, (TemporalUnit)ChronoUnit.DAYS));
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(TaskMeterLogEntity.class, "deleteTaskMetricsByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    public List<String> findTaskMetricsForCleanup(final int batchSize, final Integer timeToLive, final int minuteFrom, final int minuteTo) {
        final Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("currentTimestamp", ClockUtil.getCurrentTime());
        queryParameters.put("timeToLive", timeToLive);
        if (minuteTo - minuteFrom + 1 < 60) {
            queryParameters.put("minuteFrom", minuteFrom);
            queryParameters.put("minuteTo", minuteTo);
        }
        final ListQueryParameterObject parameterObject = new ListQueryParameterObject(queryParameters, 0, batchSize);
        parameterObject.getOrderingProperties().add(new QueryOrderingProperty(new QueryPropertyImpl("TIMESTAMP_"), Direction.ASCENDING));
        return (List<String>)this.getDbEntityManager().selectList("selectTaskMetricIdsForCleanup", parameterObject);
    }
}
