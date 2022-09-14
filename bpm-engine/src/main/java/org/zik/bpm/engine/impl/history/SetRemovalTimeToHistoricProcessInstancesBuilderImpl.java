// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.batch.removaltime.SetRemovalTimeToHistoricProcessInstancesCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import java.util.Arrays;
import org.zik.bpm.engine.history.SetRemovalTimeToHistoricProcessInstancesBuilder;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder;

public class SetRemovalTimeToHistoricProcessInstancesBuilderImpl implements SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder
{
    protected HistoricProcessInstanceQuery query;
    protected List<String> ids;
    protected Date removalTime;
    protected Mode mode;
    protected boolean isHierarchical;
    protected CommandExecutor commandExecutor;
    
    public SetRemovalTimeToHistoricProcessInstancesBuilderImpl(final CommandExecutor commandExecutor) {
        this.mode = null;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public SetRemovalTimeToHistoricProcessInstancesBuilder byQuery(final HistoricProcessInstanceQuery query) {
        this.query = query;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricProcessInstancesBuilder byIds(final String... ids) {
        this.ids = ((ids != null) ? Arrays.asList(ids) : null);
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricProcessInstancesBuilder absoluteRemovalTime(final Date removalTime) {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.ABSOLUTE_REMOVAL_TIME;
        this.removalTime = removalTime;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricProcessInstancesBuilder calculatedRemovalTime() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.CALCULATED_REMOVAL_TIME;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricProcessInstancesBuilder clearedRemovalTime() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.CLEARED_REMOVAL_TIME;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricProcessInstancesBuilder hierarchical() {
        this.isHierarchical = true;
        return this;
    }
    
    @Override
    public Batch executeAsync() {
        return this.commandExecutor.execute((Command<Batch>)new SetRemovalTimeToHistoricProcessInstancesCmd(this));
    }
    
    public HistoricProcessInstanceQuery getQuery() {
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
    
    public boolean isHierarchical() {
        return this.isHierarchical;
    }
    
    public enum Mode
    {
        CALCULATED_REMOVAL_TIME, 
        ABSOLUTE_REMOVAL_TIME, 
        CLEARED_REMOVAL_TIME;
    }
}
