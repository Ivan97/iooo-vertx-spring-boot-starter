package tech.iooo.boot;

import io.vertx.core.Vertx;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tech.iooo.boot.config.IoooVertxConfiguration;

/**
 * @author 龙也
 * @date 2021/1/26 11:29 上午
 */
@ComponentScan
@Configuration(
    proxyBeanMethods = false
)
@ConditionalOnClass({Vertx.class})
@Import({IoooVertxConfiguration.class})
public class IoooVertxAutoConfiguration {
}
