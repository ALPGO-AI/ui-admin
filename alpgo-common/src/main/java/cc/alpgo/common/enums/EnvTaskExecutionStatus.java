package cc.alpgo.common.enums;

public enum EnvTaskExecutionStatus {
    Idle, Start, Processing, ImageUploading, Finished;

    public static boolean canRunNewTask(EnvTaskExecutionStatus status) {
        if (status == null) {
            return true;
        }
        return status.equals(Idle) || status.equals(Finished);
    }
}
