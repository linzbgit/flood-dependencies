package cn.flood.mq.rocketmq.annotation;

import cn.flood.mq.rocketmq.base.MessageExtConst;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by yipin on 2017/6/27.
 * RocketMQ消费者自动装配注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MQConsumer {
    String consumerGroup();
    String topic();

    /**
     * 广播模式消费： BROADCASTING
     * 集群模式消费： CLUSTERING
     * @return 消息模式
     */
    String messageMode() default MessageExtConst.MESSAGE_MODE_CLUSTERING;

    /**
     * 使用线程池并发消费: CONCURRENTLY("CONCURRENTLY"),
     * 单线程消费: ORDERLY("ORDERLY");
     * @return 消费模式
     */
    String consumeMode() default MessageExtConst.CONSUME_MODE_CONCURRENTLY;

    /**
     * 是否去重，默认DEDUP_DISABLE 不开启
     * 不开启去重， DEDUP_DISABLE
     * 开启去重 DEDUP_CONSUME_LATER
     * @return
     */
    int dedup() default MessageExtConst.DEDUP_DISABLE;

    String[] tag() default {"*"};
}
