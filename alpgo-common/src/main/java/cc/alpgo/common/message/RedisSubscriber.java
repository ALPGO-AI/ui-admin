package cc.alpgo.common.message;

import cc.alpgo.common.event.HandleDataEvent;
import cc.alpgo.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber implements MessageListener {

    protected final Logger logger = LoggerFactory.getLogger(RedisSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String channel = new String(message.getChannel());
        logger.info("订阅频道:" + channel);
        logger.info("接收数据:" + new String(message.getBody()));
        SpringUtils.publishEvent(new HandleDataEvent(this, message));
    }
}
