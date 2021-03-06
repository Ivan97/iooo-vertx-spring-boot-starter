package tech.iooo.boot.config;

import java.util.Optional;

import cn.hutool.core.collection.CollectionUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.iooo.boot.config.IoooVertxProperties.DefaultDeploymentOptions;

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

    @Bean
    @ConditionalOnMissingBean
    public DeploymentOptions defaultDeploymentOptions(IoooVertxProperties properties) {
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        DefaultDeploymentOptions config = properties.getDefaultDeploymentOptions();
        Optional.ofNullable(config.getHa()).ifPresent(deploymentOptions::setHa);
        Optional.ofNullable(config.getInstances()).ifPresent(deploymentOptions::setInstances);
        Optional.ofNullable(config.getMaxWorkerExecuteTime())
                .ifPresent(deploymentOptions::setMaxWorkerExecuteTime);
        Optional.ofNullable(config.getMaxWorkerExecuteTimeUnit())
                .ifPresent(deploymentOptions::setMaxWorkerExecuteTimeUnit);
        Optional.ofNullable(config.getWorker()).ifPresent(deploymentOptions::setWorker);
        Optional.ofNullable(config.getWorkerPoolSize()).ifPresent(deploymentOptions::setWorkerPoolSize);

        if (CollectionUtil.isNotEmpty(config.getExtraClasspath())) {
            deploymentOptions.setExtraClasspath(config.getExtraClasspath());
        }
        if (StringUtils.isNotEmpty(config.getIsolationGroup())) {
            deploymentOptions.setIsolationGroup(config.getIsolationGroup());
        }
        if (StringUtils.isNotEmpty(config.getWorkerPoolName())) {
            deploymentOptions.setWorkerPoolName(config.getWorkerPoolName());
        }
        return deploymentOptions;
    }
}