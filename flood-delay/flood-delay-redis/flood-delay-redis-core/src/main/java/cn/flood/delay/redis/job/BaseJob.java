package cn.flood.delay.redis.job;


import cn.flood.delay.redis.configuration.Config;
import cn.flood.delay.redis.constans.LuaScriptConst;
import cn.flood.delay.redis.core.DQRedis;
import cn.flood.delay.redis.core.RawMessage;
import cn.flood.delay.redis.utils.GsonUtil;
import io.lettuce.core.Limit;
import io.lettuce.core.Range;
import io.lettuce.core.ScriptOutputType;

import java.util.List;

/**
 * @author mmdai
 * @date 2019/11/25
 */
class BaseJob {

	DQRedis redis;
	Config config;

	BaseJob(Config config, DQRedis dqRedis) {
		this.config = config;
		this.redis = dqRedis;
	}

	List<String> zrangebyscore(String key, long begin, long end) {
		return redis.zrangebyscore(key, Range.create(begin, end), Limit.create(0L, 1000L));
	}

	RawMessage getTask(String key) {
		String value = redis.hget(config.getHashKey(), key);
		if (null == value) {
			return null;
		}
		return GsonUtil.fromJson(value, RawMessage.class);
	}

	boolean transferMessage(String key, String from, String to, long score) {
		String[] keys  = new String[]{from, to, key};
		String[] args  = new String[]{score + ""};
		Long     count = redis.syncEval(LuaScriptConst.TRANSFER_MESSAGE, ScriptOutputType.INTEGER, keys, args);
		return null != count && count == 1;
	}

	void deleteMessage(String key) {
		redis.zrem(config.getDelayKey(), key);
		redis.zrem(config.getAckKey(), key);
		redis.hdel(config.getHashKey(), key);
	}

}
