package tech.iooo.boot.config;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 龙也
 * @date 2021/1/26 11:13 上午
 */
@Configuration
@EnableConfigurationProperties(IoooVertxProperties.class)
public class IoooVertxConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Vertx vertx() {
        return Vertx.vertx();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus eventBus(Vertx vertx) {
        return vertx.eventBus();
    }
}
