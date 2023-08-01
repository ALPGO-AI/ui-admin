package cc.alpgo.common.event;

import org.springframework.context.ApplicationEvent;

public class CustomWebhooksEvent extends ApplicationEvent {
    private String url;
    private String payload;
    public CustomWebhooksEvent(String url, String payload) {
        super(url + "|" +payload);
        this.payload = payload;
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }
    public String getUrl() {
        return url;
    }
}
