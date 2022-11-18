package cn.flood.base.aop;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 
* <p>Title: ServiceAspect</p>  
* <p>Description: 相对具体的业务逻辑服务层</p>  
* @author mmdai  
* @date 2018年10月11日
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServiceAspect implements LogAspect {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String CREATE = "create";

	@Pointcut(
			"@within(org.springframework.stereotype.Service))"
	)
	public void log() {
		
	}
	
	@Around("log()") 
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Stopwatch stopwatch = Stopwatch.createStarted();
		String methodName = joinPoint.getSignature().getName();
		if(CREATE.equalsIgnoreCase(methodName)){
			return joinPoint.proceed();
		}
		logger.info("【service】【{}】 start", methodName);
//		if (logger.isDebugEnabled()) {
//			logger.debug("【service】【{}】【{}】", methodName, before(joinPoint));
//		}
		Object result = joinPoint.proceed();
//		if (logger.isDebugEnabled()) {
//			logger.debug("【service】【{}】【{}】", methodName, after(result));
//		}
		logger.info("【service】【{}】 end,cost【{}ms】", methodName, stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
		return result;
	}
}