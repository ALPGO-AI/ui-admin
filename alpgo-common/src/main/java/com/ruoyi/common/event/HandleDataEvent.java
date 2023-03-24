package com.alpgo.common.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.redis.connection.Message;

public class HandleDataEvent extends ApplicationEvent {
    private Message message;

    public HandleDataEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
