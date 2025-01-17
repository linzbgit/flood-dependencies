package cn.flood.oauth.configuration.client;

import cn.flood.oauth.configuration.client.restTempate.HttpClientProperties;
import cn.flood.oauth.configuration.client.restTempate.RestTemplateConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * 开启自动配置
 *
 * @author mmdai
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import({HttpClientProperties.class, RestTemplateConfiguration.class,
    FloodPermissionClientConfiguration.class})
public @interface EnableFloodPermissionClient {

}
