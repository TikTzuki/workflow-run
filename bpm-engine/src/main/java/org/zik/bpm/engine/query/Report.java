// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.query;

import org.zik.bpm.engine.history.DurationReportResult;
import java.util.List;

public interface Report
{
    List<DurationReportResult> duration(final PeriodUnit p0);
}
