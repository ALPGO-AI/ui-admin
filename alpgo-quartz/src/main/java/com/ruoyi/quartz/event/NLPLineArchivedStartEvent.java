package com.alpgo.quartz.event;

import org.springframework.context.ApplicationEvent;

public class NLPLineArchivedStartEvent extends ApplicationEvent {

    public NLPLineArchivedStartEvent(Object source) {
        super(source);
    }
}
