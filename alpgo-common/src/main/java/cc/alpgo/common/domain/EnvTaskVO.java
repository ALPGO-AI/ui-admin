package cc.alpgo.common.domain;

import cc.alpgo.common.event.StartEnvApplicationEvent;

public class EnvTaskVO {

    public EnvTaskVO() {
    }
    public EnvTaskVO(StartEnvApplicationEvent event) {
        this.envKey = event.getEnvKey();
        this.taskName = event.getTaskName();
        this.envName = event.getEnvName();
    }
    private String envKey;
    private String envName;
    private String taskName;

    public String getEnvKey() {
        return envKey;
    }

    public void setEnvKey(String envKey) {
        this.envKey = envKey;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
