package cn.flood.base.undertow;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;

import cn.flood.base.undertow.server.UndertowServerFactoryCustomizer;
import io.undertow.Undertow;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Undertow http2 h2c 配置，对 servlet 开启
 *
 * @author pangu
 */
@AutoConfiguration
@ConditionalOnClass(Undertow.class)
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
public class UndertowHttp2Configuration {

  @Bean
  public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowHttp2WebServerFactoryCustomizer() {
    return factory -> factory
        .addBuilderCustomizers(builder -> builder.setServerOption(ENABLE_HTTP2, true));
  }

  /**
   * 实例化UndertowServerFactoryCustomizer，解决undertow启动提示warn的问题
   *
   * @return UndertowServerFactoryCustomizer
   */
  @Bean
  public UndertowServerFactoryCustomizer undertowServerFactoryCustomizer() {
    return new UndertowServerFactoryCustomizer();
  }
}
