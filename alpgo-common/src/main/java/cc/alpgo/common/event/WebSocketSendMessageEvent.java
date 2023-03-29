package cc.alpgo.common.event;

import cc.alpgo.common.core.domain.model.LoginUser;
import org.springframework.context.ApplicationEvent;

public class WebSocketSendMessageEvent extends ApplicationEvent {
    public WebSocketSendMessageEvent(String wsId, String name, String msg) {
        super(wsId + "_" + name + "_" + msg);
        this.wsId = wsId;
        this.name = name;
        this.msg = msg;
    }
    String wsId;
    String name;
    String msg;

    public String getWsId() {
        return wsId;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }
}
