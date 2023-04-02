package cc.alpgo.common.event;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

public class StartEnvApplicationEvent extends ApplicationEvent {
    private ApplicationEvent event;
    private String envKey;
    private String envName;
    private String taskName;
    private String uuid;
    private Date createTime;
    public StartEnvApplicationEvent(String uuid, ApplicationEvent event, String envKey, String envName, String taskName) {
        super(event);
        this.uuid = uuid;
        this.event = event;
        this.envKey = envKey;
        this.envName = envName;
        this.taskName = taskName;
        this.createTime = new Date();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getUuid() {
        return uuid;
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
