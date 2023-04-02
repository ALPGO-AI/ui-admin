package cc.alpgo.quartz.task;

import cc.alpgo.quartz.event.EnvTaskCheckEvent;
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
@Component("envTask")
public class EnvTask
{
    private static final Logger logger = LoggerFactory.getLogger(EnvTask.class);

    @Autowired
    private ApplicationContext applicationContext;

    public void checkEnvTask() {
        applicationContext.publishEvent(new EnvTaskCheckEvent(this));
    }
}
