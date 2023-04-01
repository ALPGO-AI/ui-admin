package cc.alpgo.framework.listener;

import cc.alpgo.common.core.domain.model.LoginUser;
import cc.alpgo.common.event.WebSocketSendMessageEvent;
import cc.alpgo.common.utils.CosUtil;
import cc.alpgo.framework.websocket.WebSocketUsers;
import cc.alpgo.sdtool.service.IStableDiffusionPatternService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class WebSocketListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger log = LoggerFactory.getLogger(WebSocketListener.class);
    public void onApplicationEvent(ApplicationEvent event) {
        // 判断事件类型，执行特定处理逻辑
        if (event instanceof WebSocketSendMessageEvent) {
            log.info("WebSocketListener catch WebSocketSendMessageEvent {}", ((WebSocketSendMessageEvent) event).toString());
            WebSocketSendMessageEvent socketSendMessageEvent = (WebSocketSendMessageEvent) event;
            String msg = socketSendMessageEvent.getMsg();
            String name = socketSendMessageEvent.getName();
            String wsId = socketSendMessageEvent.getWsId();
            WebSocketUsers.sendMessageToUserByWsId(wsId, name + " " + msg);
            log.info("WebSocketListener upload {} finished", ((WebSocketSendMessageEvent) event).toString());
        }
    }
}