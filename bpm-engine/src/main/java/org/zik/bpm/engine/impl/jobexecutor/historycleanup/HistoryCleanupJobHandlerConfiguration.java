// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import java.util.Calendar;
import java.util.Date;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.util.JsonUtil;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;

public class HistoryCleanupJobHandlerConfiguration implements JobHandlerConfiguration
{
    public static final int START_DELAY = 10;
    public static final int MAX_DELAY = 3600;
    public static final String JOB_CONFIG_COUNT_EMPTY_RUNS = "countEmptyRuns";
    public static final String JOB_CONFIG_EXECUTE_AT_ONCE = "immediatelyDue";
    public static final String JOB_CONFIG_MINUTE_FROM = "minuteFrom";
    public static final String JOB_CONFIG_MINUTE_TO = "minuteTo";
    private int countEmptyRuns;
    private boolean immediatelyDue;
    private int minuteFrom;
    private int minuteTo;
    
    public HistoryCleanupJobHandlerConfiguration() {
        this.countEmptyRuns = 0;
        this.minuteFrom = 0;
        this.minuteTo = 59;
    }
    
    @Override
    public String toCanonicalString() {
        final JsonObject json = JsonUtil.createObject();
        JsonUtil.addField(json, "countEmptyRuns", this.countEmptyRuns);
        JsonUtil.addField(json, "immediatelyDue", this.immediatelyDue);
        JsonUtil.addField(json, "minuteFrom", this.minuteFrom);
        JsonUtil.addField(json, "minuteTo", this.minuteTo);
        return json.toString();
    }
    
    public static HistoryCleanupJobHandlerConfiguration fromJson(final JsonObject jsonObject) {
        final HistoryCleanupJobHandlerConfiguration config = new HistoryCleanupJobHandlerConfiguration();
        if (jsonObject.has("countEmptyRuns")) {
            config.setCountEmptyRuns(JsonUtil.getInt(jsonObject, "countEmptyRuns"));
        }
        if (jsonObject.has("immediatelyDue")) {
            config.setImmediatelyDue(JsonUtil.getBoolean(jsonObject, "immediatelyDue"));
        }
        config.setMinuteFrom(JsonUtil.getInt(jsonObject, "minuteFrom"));
        config.setMinuteTo(JsonUtil.getInt(jsonObject, "minuteTo"));
        return config;
    }
    
    public Date getNextRunWithDelay(final Date date) {
        final Date result = this.addSeconds(date, Math.min((int)(Math.pow(2.0, this.countEmptyRuns) * 10.0), 3600));
        return result;
    }
    
    private Date addSeconds(final Date date, final int amount) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(13, amount);
        return c.getTime();
    }
    
    public int getCountEmptyRuns() {
        return this.countEmptyRuns;
    }
    
    public void setCountEmptyRuns(final int countEmptyRuns) {
        this.countEmptyRuns = countEmptyRuns;
    }
    
    public boolean isImmediatelyDue() {
        return this.immediatelyDue;
    }
    
    public void setImmediatelyDue(final boolean immediatelyDue) {
        this.immediatelyDue = immediatelyDue;
    }
    
    public int getMinuteFrom() {
        return this.minuteFrom;
    }
    
    public void setMinuteFrom(final int minuteFrom) {
        this.minuteFrom = minuteFrom;
    }
    
    public int getMinuteTo() {
        return this.minuteTo;
    }
    
    public void setMinuteTo(final int minuteTo) {
        this.minuteTo = minuteTo;
    }
}
