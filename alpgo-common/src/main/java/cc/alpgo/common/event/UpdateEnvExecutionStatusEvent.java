package cc.alpgo.common.event;

import cc.alpgo.common.enums.EnvTaskExecutionStatus;
import org.springframework.context.ApplicationEvent;

public class UpdateEnvExecutionStatusEvent extends ApplicationEvent {
    private EnvTaskExecutionStatus status;
    private String key;

    public UpdateEnvExecutionStatusEvent(String key, EnvTaskExecutionStatus status) {
        super(status);
        this.key = key;
        this.status = status;
    }
    public String getKey() {
        return key;
    }

    public EnvTaskExecutionStatus getStatus() {
        return status;
    }
}
