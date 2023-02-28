package cn.flood.tools.uid.extend.strategy;

import cn.flood.tools.uid.extend.annotation.UidModel;
import cn.flood.tools.uid.leaf.ISegmentService;
import cn.flood.tools.uid.leaf.SegmentServiceImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @类名称 LeafSegmentStrategy.java
 * @类描述 <pre>Leaf分段批量Id策略(可配置asynLoadingSegment-异步标识)</pre>
 * @作者 庄梦蝶殇 linhuaichuan1989@126.com
 * @创建时间 2018年9月5日 上午11:35:53
 * @版本 1.00
 * @修改记录 <pre>
 *     版本                       修改人 		修改日期 		 修改内容描述
 *     ----------------------------------------------
 *     1.00 	庄梦蝶殇 	2018年9月5日
 *     ----------------------------------------------
 * </pre>
 */
public class LeafSegmentStrategy implements IUidStrategy {

  private final static String MSG_UID_PARSE = "{\"UID\":\"%s\"}";

  /**
   * 生成器集合
   */
  protected static Map<String, ISegmentService> generatorMap = new ConcurrentHashMap<>();

  protected JdbcTemplate jdbcTemplate;

  private boolean asynLoadingSegment = true;

  public LeafSegmentStrategy() {
  }

  public LeafSegmentStrategy(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * 获取uid生成器
   *
   * @param prefix 前缀
   * @return uid生成器
   * @方法名称 getUidGenerator
   * @功能描述 <pre>获取uid生成器</pre>
   */
  public ISegmentService getSegmentService(String prefix) {
    ISegmentService segmentService = generatorMap.get(prefix);
    if (null == segmentService) {
      synchronized (generatorMap) {
        if (null == segmentService) {
          segmentService = new SegmentServiceImpl(jdbcTemplate, prefix);
        }
        generatorMap.put(prefix, segmentService);
      }
    }
    return segmentService;
  }

  @Override
  public UidModel getName() {
    return UidModel.segment;
  }

  @Override
  public long getUID(String group) {
    return getSegmentService(group).getId();
  }

  @Override
  public String parseUID(long uid, String group) {
    return String.format(MSG_UID_PARSE, uid);
  }

  public boolean isAsynLoadingSegment() {
    return asynLoadingSegment;
  }

  public void setAsynLoadingSegment(boolean asynLoadingSegment) {
    this.asynLoadingSegment = asynLoadingSegment;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
}
