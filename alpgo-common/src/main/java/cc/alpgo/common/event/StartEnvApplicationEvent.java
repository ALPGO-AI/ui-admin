package cc.alpgo.common.event;

import org.springframework.context.ApplicationEvent;

public class StartEnvApplicationEvent extends ApplicationEvent {
    private ApplicationEvent event;
    private String envKey;
    private String envName;
    private String taskName;
    public StartEnvApplicationEvent(ApplicationEvent event, String envKey, String envName, String taskName) {
        super(event);
        this.event = event;
        this.envKey = envKey;
        this.envName = envName;
        this.taskName = taskName;
    }

    public ApplicationEvent getEvent() {
        return event;
    }
    public String getEnvKey() {
        return envKey;
    }
    public String getEnvName() {
        return envName;
    }
    public String getTaskName() {
        return taskName;
    }
}
