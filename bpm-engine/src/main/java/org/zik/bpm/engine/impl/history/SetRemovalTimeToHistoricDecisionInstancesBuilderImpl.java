// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.batch.removaltime.SetRemovalTimeToHistoricDecisionInstancesCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import java.util.Arrays;
import org.zik.bpm.engine.history.SetRemovalTimeToHistoricDecisionInstancesBuilder;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import java.util.List;
import org.zik.bpm.engine.history.HistoricDecisionInstanceQuery;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder;

public class SetRemovalTimeToHistoricDecisionInstancesBuilderImpl implements SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder
{
    protected HistoricDecisionInstanceQuery query;
    protected List<String> ids;
    protected Date removalTime;
    protected Mode mode;
    protected boolean isHierarchical;
    protected CommandExecutor commandExecutor;
    
    public SetRemovalTimeToHistoricDecisionInstancesBuilderImpl(final CommandExecutor commandExecutor) {
        this.mode = null;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public SetRemovalTimeToHistoricDecisionInstancesBuilder byQuery(final HistoricDecisionInstanceQuery query) {
        this.query = query;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricDecisionInstancesBuilder byIds(final String... ids) {
        this.ids = ((ids != null) ? Arrays.asList(ids) : null);
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricDecisionInstancesBuilder absoluteRemovalTime(final Date removalTime) {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.ABSOLUTE_REMOVAL_TIME;
        this.removalTime = removalTime;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricDecisionInstancesBuilder calculatedRemovalTime() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.CALCULATED_REMOVAL_TIME;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricDecisionInstancesBuilder clearedRemovalTime() {
        EnsureUtil.ensureNull(BadUserRequestException.class, "The removal time modes are mutually exclusive", "mode", this.mode);
        this.mode = Mode.CLEARED_REMOVAL_TIME;
        return this;
    }
    
    @Override
    public SetRemovalTimeToHistoricDecisionInstancesBuilder hierarchical() {
        this.isHierarchical = true;
        return this;
    }
    
    @Override
    public Batch executeAsync() {
        return this.commandExecutor.execute((Command<Batch>)new SetRemovalTimeToHistoricDecisionInstancesCmd(this));
    }
    
    public HistoricDecisionInstanceQuery getQuery() {
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
