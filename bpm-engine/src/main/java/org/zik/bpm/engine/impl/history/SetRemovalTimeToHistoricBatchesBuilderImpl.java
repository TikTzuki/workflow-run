// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.batch.removaltime.SetRemovalTimeToHistoricBatchesCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import java.util.Arrays;
import org.zik.bpm.engine.history.SetRemovalTimeToHistoricBatchesBuilder;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.batch.history.HistoricBatchQuery;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricBatchesBuilder;

public class SetRemovalTimeToHistoricBatchesBuilderImpl implements SetRemovalTimeSelectModeForHistoricBatchesBuilder
{
    protected HistoricBatchQuery query;
    protected List<String> ids;
    protected Mode mode;
    protected Date removalTime;
    protected CommandExecutor commandExecutor;
    
    public SetRemovalTimeToHistoricBatchesBuilderImpl(final CommandExecutor commandExecutor) {
        this.mode = null;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public SetRemovalTimeToHistoricBatchesBuilder byQuery(final HistoricBatchQuery query) {
        this.query = query;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricBatchesBuilder byIds(final String... ids) {
        this.ids = ((ids != null) ? Arrays.asList(ids) : null);
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricBatchesBuilder absoluteRemovalTime(final Date removalTime) {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.ABSOLUTE_REMOVAL_TIME;
        this.removalTime = removalTime;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricBatchesBuilder calculatedRemovalTime() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.CALCULATED_REMOVAL_TIME;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricBatchesBuilder clearedRemovalTime() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.CLEARED_REMOVAL_TIME;
        return this;
    }
    
    @Override
    public Batch executeAsync() {
        return this.commandExecutor.execute((Command<Batch>)new SetRemovalTimeToHistoricBatchesCmd(this));
    }
    
    public HistoricBatchQuery getQuery() {
        return this.query;
    }
    
    public List<String> getIds() {
        return this.ids;
    }
    
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public Mode getMode() {
        return this.mode;
    }
    
    public enum Mode
    {
        CALCULATED_REMOVAL_TIME, 
        ABSOLUTE_REMOVAL_TIME, 
        CLEARED_REMOVAL_TIME;
    }
}
