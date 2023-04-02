package cc.alpgo.common.domain;

import cc.alpgo.common.event.StartEnvApplicationEvent;

import java.util.Date;

public class EnvTaskVO {

    public EnvTaskVO() {
    }
    public EnvTaskVO(StartEnvApplicationEvent event) {
        this.uuid = event.getUuid();
        this.envKey = event.getEnvKey();
        this.taskName = event.getTaskName();
        this.envName = event.getEnvName();
        this.createTime = event.getCreateTime();
    }
    private Date createTime;
    private String uuid;
    private String envKey;
    private String envName;
    private String taskName;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
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
