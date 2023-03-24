package cc.alpgo.quartz.task;

import cc.alpgo.quartz.event.NLPLineArchivedStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author alpgo
 */
@Component("novelTask")
public class NovelTask
{
    private static final Logger logger = LoggerFactory.getLogger(NovelTask.class);

    @Autowired
    private ApplicationContext applicationContext;

    public void novelArchivedLines() {
        applicationContext.publishEvent(new NLPLineArchivedStartEvent(this));
    }
}
