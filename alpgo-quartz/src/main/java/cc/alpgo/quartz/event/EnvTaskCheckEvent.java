package cc.alpgo.quartz.event;

import org.springframework.context.ApplicationEvent;

public class EnvTaskCheckEvent extends ApplicationEvent {

    public EnvTaskCheckEvent(Object source) {
        super(source);
    }
}
