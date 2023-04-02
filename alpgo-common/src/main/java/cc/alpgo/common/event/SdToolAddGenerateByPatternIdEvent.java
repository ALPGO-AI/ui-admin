package cc.alpgo.common.event;

import org.springframework.context.ApplicationEvent;

public class SdToolAddGenerateByPatternIdEvent extends StartEnvApplicationEvent {

    public SdToolAddGenerateByPatternIdEvent(SdToolExecuteGenerateByPatternIdEvent event, String envName, String taskName) {
        super(event, event.getEnvKey(), envName, taskName);
    }
}
