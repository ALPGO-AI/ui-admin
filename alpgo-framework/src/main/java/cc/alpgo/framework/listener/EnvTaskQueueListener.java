package cc.alpgo.framework.listener;

import cc.alpgo.common.core.redis.RedisCache;
import cc.alpgo.common.event.StartEnvApplicationEvent;
import cc.alpgo.common.event.UpdateEnvExecutionStatusEvent;
import cc.alpgo.common.enums.EnvTaskExecutionStatus;
import cc.alpgo.framework.config.StaticScheduleTask;
import cc.alpgo.framework.websocket.WebSocketUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@EnableAsync
public class EnvTaskQueueListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger log = LoggerFactory.getLogger(EnvTaskQueueListener.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UpdateEnvExecutionStatusEvent) {
            redisCache.setCacheEnvironmentExecutionStatusMapValue(((UpdateEnvExecutionStatusEvent) event).getKey(), ((UpdateEnvExecutionStatusEvent) event).getStatus());
            updateTaskList();
        }
    }

    public void scheduled() {
        for (String key : environmentTaskQueues.keySet()) {
            if (hasNextTaskFromEnvironmentQueue(key)) {
                if (EnvTaskExecutionStatus.canRunNewTask(
                        redisCache.getCacheEnvironmentExecutionStatusMapValue(key)
                )) {
                    ApplicationEvent event = getNextTaskFromEnvironmentQueue(key).getEvent();
                    log.info("开始执行api任务: {}", event);
                    applicationContext.publishEvent(event);
                }
            }
        }
    }


    Map<String, ConcurrentLinkedQueue<StartEnvApplicationEvent>> environmentTaskQueues = new ConcurrentHashMap<>();

    public Map<String, ConcurrentLinkedQueue<StartEnvApplicationEvent>> getEnvironmentTaskQueues() {
        return environmentTaskQueues;
    }

    public void addTaskToEnvironmentQueue(String environment, StartEnvApplicationEvent taskEvent) {
        ConcurrentLinkedQueue<StartEnvApplicationEvent> taskQueue = environmentTaskQueues.get(environment);
        if (taskQueue == null) {
            taskQueue = new ConcurrentLinkedQueue<>();
            updateStatus(environment, EnvTaskExecutionStatus.Idle);
            environmentTaskQueues.put(environment, taskQueue);
        }
        taskQueue.add(taskEvent);
        updateTaskList();
    }

    private void updateTaskList() {
        try {
            WebSocketUsers.sendMessageToUsersByText("TASK_LIST_UPDATED");
        } catch (Exception e) {
        }
    }

    public boolean hasNextTaskFromEnvironmentQueue(String environment) {
        ConcurrentLinkedQueue<StartEnvApplicationEvent> taskQueue = environmentTaskQueues.get(environment);
        if (taskQueue == null) {
            return false;
        }
        return taskQueue.peek() != null;
    }
    public StartEnvApplicationEvent getNextTaskFromEnvironmentQueue(String environment) {
        ConcurrentLinkedQueue<StartEnvApplicationEvent> taskQueue = environmentTaskQueues.get(environment);
        if (taskQueue == null) {
            return null;
        }
        StartEnvApplicationEvent poll = taskQueue.poll();
        updateTaskList();
        return poll;
    }

    public void closeEnvironmentQueue(String environment) {
        ConcurrentLinkedQueue<StartEnvApplicationEvent> taskQueue = environmentTaskQueues.get(environment);
        if (taskQueue != null) {
            taskQueue.clear();
            updateStatus(environment, EnvTaskExecutionStatus.Idle);
            environmentTaskQueues.remove(environment);
            updateTaskList();
        }
    }

    private void updateStatus(String envKey, EnvTaskExecutionStatus processing) {
        applicationContext.publishEvent(new UpdateEnvExecutionStatusEvent(envKey, processing));
    }
}
