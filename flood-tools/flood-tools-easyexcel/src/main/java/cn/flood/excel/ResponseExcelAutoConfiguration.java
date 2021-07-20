package cn.flood.excel;

import cn.flood.excel.aop.ResponseExcelReturnValueHandler;
import cn.flood.excel.config.ExcelConfigProperties;
import cn.flood.excel.handler.SheetWriteHandler;
import cn.flood.excel.processor.NameProcessor;
import cn.flood.excel.processor.NameSpelExpressionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
 * <p>
 * 配置初始化
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ResponseExcelAutoConfiguration implements ApplicationContextAware, InitializingBean {
	private ApplicationContext applicationContext;
	private final List<SheetWriteHandler> sheetWriteHandlerList;


	@Bean
	@ConditionalOnMissingBean
	public NameProcessor nameProcessor() {
		return new NameSpelExpressionProcessor();
	}


	@Override
	public void afterPropertiesSet() {
		RequestMappingHandlerAdapter handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<HandlerMethodReturnValueHandler> returnValueHandlers = handlerAdapter.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(new ResponseExcelReturnValueHandler(sheetWriteHandlerList));
		assert returnValueHandlers != null;
		newHandlers.addAll(returnValueHandlers);
		handlerAdapter.setReturnValueHandlers(newHandlers);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
