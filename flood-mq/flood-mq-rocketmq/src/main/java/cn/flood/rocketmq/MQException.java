package cn.flood.rocketmq;

/**
 * Created by yipin on 2017/6/28.
 * RocketMQ的自定义异常
 */
public class MQException extends RuntimeException {
    public MQException(String msg) {
        super(msg);
    }
}
