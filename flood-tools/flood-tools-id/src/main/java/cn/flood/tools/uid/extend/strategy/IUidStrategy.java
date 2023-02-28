package cn.flood.tools.uid.extend.strategy;

import cn.flood.tools.uid.extend.annotation.UidModel;

/**
 * @类名称 IUidStrategy.java
 * @类描述 <pre>uid策略接口</pre>
 * @作者 庄梦蝶殇 linhuaichuan1989@126.com
 * @创建时间 2018年8月31日 下午5:27:38
 * @版本 1.00
 * @修改记录 <pre>
 *     版本                       修改人 		修改日期 		 修改内容描述
 *     ----------------------------------------------
 *     1.00 	庄梦蝶殇 	2018年8月31日
 *     ----------------------------------------------
 * </pre>
 */
public interface IUidStrategy {

  /**
   * 策略名
   *
   * @return
   * @方法名称 getName
   * @功能描述 <pre>策略名</pre>
   */
  public UidModel getName();

  /**
   * @param group 分组
   * @return id
   * @方法名称 getUID
   * @功能描述 <pre>获取ID</pre>
   */
  public long getUID(String group);

  /**
   * @param uid
   * @param group 分组
   * @return 输出json字符串：{\"UID\":\"\",\"timestamp\":\"\",\"workerId\":\"\",\"dataCenterId\":\"\",\"sequence\":\"\"}
   * @方法名称 parseUID
   * @功能描述 <pre>解析ID</pre>
   */
  String parseUID(long uid, String group);
}
