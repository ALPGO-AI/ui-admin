package com.alpgo.quartz.util;

import org.quartz.JobExecutionContext;
import com.alpgo.quartz.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author alpgo
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
