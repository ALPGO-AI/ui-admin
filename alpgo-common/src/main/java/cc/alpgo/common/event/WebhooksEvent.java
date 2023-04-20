package cc.alpgo.common.event;

import org.springframework.context.ApplicationEvent;

public class WebhooksEvent extends ApplicationEvent {
    private String payload;
    public WebhooksEvent(String payload) {
        super(payload);
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
