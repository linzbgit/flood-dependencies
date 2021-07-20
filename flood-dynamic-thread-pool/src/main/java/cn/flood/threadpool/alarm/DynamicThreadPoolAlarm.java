package cn.flood.threadpool.alarm;

import cn.flood.json.JsonUtils;
import cn.flood.threadpool.DynamicThreadPoolManager;
import cn.flood.threadpool.FloodThreadPoolExecutor;
import cn.flood.threadpool.config.DynamicThreadPoolProperties;
import cn.flood.threadpool.config.ThreadPoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池告警
 * @时间 2020-05-26 21:44
 */
public class DynamicThreadPoolAlarm {

    @Autowired
    private DynamicThreadPoolManager dynamicThreadPoolManager;

    @Autowired
    private DynamicThreadPoolProperties dynamicThreadPoolProperties;

    @Autowired(required = false)
    private ThreadPoolAlarmNotify threadPoolAlarmNotify;

    /**
     * 应用名称，告警用到
     */
    @Value("${spring.application.name:unknown}")
    private String applicationName;

    /**
     * 是否使用默认告警
     */
    @Value("${flood.threadpools.alarm.default:true}")
    private boolean useDefaultAlarm;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                dynamicThreadPoolProperties.getExecutors().stream().forEach(prop -> {
                    String threadPoolName = prop.getThreadPoolName();
                    FloodThreadPoolExecutor threadPoolExecutor = dynamicThreadPoolManager.getThreadPoolExecutor(threadPoolName);

                    int queueCapacityThreshold = prop.getQueueCapacityThreshold();
                    int taskCount = threadPoolExecutor.getQueue().size();
                    if (taskCount > queueCapacityThreshold) {
                        sendQueueCapacityThresholdAlarmMessage(prop, taskCount);
                    }

                    AtomicLong rejectCount = dynamicThreadPoolManager.getRejectCount(threadPoolName);
                    if (rejectCount != null && rejectCount.get() > 0) {
                        sendRejectAlarmMessage(rejectCount.get(), prop);
                        // 清空拒绝数据
                        dynamicThreadPoolManager.clearRejectCount(threadPoolName);
                    }

                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendRejectAlarmMessage(long rejectCount, ThreadPoolProperties prop) {
        AlarmMessage alarmMessage = AlarmMessage.builder()
                .alarmName("rejectCount")
                .alarmType(getAlarmType())
                .apiUrl(dynamicThreadPoolProperties.getAlarmApiUrl())
                .message(getRejectCountMessage(rejectCount, prop))
                .accessToken(dynamicThreadPoolProperties.getAccessToken())
                .secret(dynamicThreadPoolProperties.getSecret())
                .alarmTimeInterval(dynamicThreadPoolProperties.getAlarmTimeInterval())
                .build();

        if (useDefaultAlarm) {
            AlarmManager.sendAlarmMessage(alarmMessage);
        }

        alarmNotify(alarmMessage);
    }

    private void sendQueueCapacityThresholdAlarmMessage(ThreadPoolProperties prop, int taskCount) {
        AlarmMessage alarmMessage = AlarmMessage.builder()
                .alarmName("queueCapacityThreshold")
                .alarmType(getAlarmType())
                .apiUrl(dynamicThreadPoolProperties.getAlarmApiUrl())
                .message(getQueueCapacityThresholdMessage(prop, taskCount))
                .accessToken(dynamicThreadPoolProperties.getAccessToken())
                .secret(dynamicThreadPoolProperties.getSecret())
                .alarmTimeInterval(dynamicThreadPoolProperties.getAlarmTimeInterval())
                .build();

        if (useDefaultAlarm) {
            AlarmManager.sendAlarmMessage(alarmMessage);
        }

        alarmNotify(alarmMessage);
    }

    private void alarmNotify(AlarmMessage alarmMessage) {
        if (threadPoolAlarmNotify != null) {
            threadPoolAlarmNotify.alarmNotify(alarmMessage);
        }
    }

    private String getQueueCapacityThresholdMessage(ThreadPoolProperties prop, int taskCount) {
        return getALarmMessage("线程池出现任务堆积情况,队列容量:" + prop.getQueueCapacity() + ",等待执行任务数量:" + taskCount , prop);
    }

    private String getRejectCountMessage(long rejectCount, ThreadPoolProperties prop) {
        return getALarmMessage("线程池中出现RejectedExecutionException异常" + rejectCount + "次", prop);
    }

    private String getALarmMessage(String reason, ThreadPoolProperties prop) {
        StringBuilder content = new StringBuilder();
        content.append("告警应用:").append(applicationName).append("\n");
        content.append("线程池名称:").append(prop.getThreadPoolName()).append("\n");
        content.append("告警原因:").append(reason).append("\n");
        content.append("参数信息:").append(formatThreadPoolParam(prop));
        content.append("业务负责人:").append(dynamicThreadPoolProperties.getOwner()).append("\n");
        content.append("告警间隔:").append(dynamicThreadPoolProperties.getAlarmTimeInterval()).append("分钟\n");
        return content.toString();
    }

    private String formatThreadPoolParam(ThreadPoolProperties prop) {
        StringBuilder content = new StringBuilder("\n");
        Map map = JsonUtils.toJavaObject(JsonUtils.toJSONString(prop), Map.class);
        map.forEach((k,v) -> {
            content.append(k).append(":").append(v).append("\n");
        });
        return content.toString();
    }

    private AlarmTypeEnum getAlarmType() {
        return StringUtils.hasText(dynamicThreadPoolProperties.getAlarmApiUrl()) ? AlarmTypeEnum.EXTERNAL_SYSTEM : AlarmTypeEnum.DING_TALK;
    }

}
