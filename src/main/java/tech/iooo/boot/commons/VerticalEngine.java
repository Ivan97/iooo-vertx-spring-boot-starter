package tech.iooo.boot.commons;

import java.util.Objects;

import com.google.common.collect.Lists;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import tech.iooo.boot.annotation.VerticleDeploymentOptions;
import tech.iooo.boot.config.Constants;
import tech.iooo.boot.inner.IoooVerticleServicesHolder;
import tech.iooo.boot.inner.IoooVerticleServicesHolder.VerticleInfo;

/**
 * @author 龙也
 * @date 2021/1/26 11:41 上午
 */
@Slf4j
@Component
public class VerticalEngine implements SmartLifecycle, ApplicationContextAware {

    @Autowired
    private Vertx vertx;

    private boolean running;
    private ApplicationContext applicationContext;

    @Override
    public void start() {
        applicationContext.getBeansOfType(Verticle.class).forEach((name, verticle) -> {
            VerticleInfo verticleInfo = VerticleInfo.builder()
                                                    .verticle(verticle)
                                                    .active(false)
                                                    .build();
            IoooVerticleServicesHolder.addVerticle(verticle.getClass().getName(), verticleInfo);
        });

        IoooVerticleServicesHolder
            .verticleServices()
            .values()
            .forEach(info -> {
                Class<?> verticleClass = info.getVerticle().getClass();
                VerticleDeploymentOptions verticleDeploymentOption = verticleClass
                    .getAnnotation(VerticleDeploymentOptions.class);
                DeploymentOptions deploymentOptions;
                if (Objects.nonNull(verticleDeploymentOption)) {
                    deploymentOptions = new DeploymentOptions();
                    deploymentOptions
                        .setHa(verticleDeploymentOption.ha())
                        .setInstances(verticleDeploymentOption.instances())
                        .setMaxWorkerExecuteTime(verticleDeploymentOption.maxWorkerExecuteTime())
                        .setMaxWorkerExecuteTimeUnit(verticleDeploymentOption.maxWorkerExecuteTimeUnit())
                        .setWorker(verticleDeploymentOption.worker())
                        .setWorkerPoolSize(verticleDeploymentOption.workerPoolSize());
                    if (StringUtils.isNotBlank(verticleDeploymentOption.extraClasspath())) {
                        deploymentOptions.setExtraClasspath(
                            Lists.newArrayList(verticleDeploymentOption.extraClasspath()));
                    }
                    if (StringUtils.isNotEmpty(verticleDeploymentOption.isolationGroup())) {
                        deploymentOptions.setIsolationGroup(verticleDeploymentOption.isolationGroup());
                    }
                    if (StringUtils.isNotEmpty(verticleDeploymentOption.workerPoolName())) {
                        deploymentOptions.setWorkerPoolName(verticleDeploymentOption.workerPoolName());
                    }
                } else {
                    deploymentOptions = Constants.defaultDeploymentOptions;
                }
                String verticleClassName = verticleClass.getName();
                vertx.deployVerticle(Constants.ioooVerticlePrefix + ":" + verticleClassName,
                                     deploymentOptions, res -> {
                        if (res.succeeded()) {
                            if (log.isInfoEnabled()) {
                                String className = ClassUtils.getUserClass(verticleClass).getSimpleName();
                                log.info("deployed verticle [{}] with id [{}].", className, res.result());
                            }
                            IoooVerticleServicesHolder.active(verticleClassName, res.result());
                        } else {
                            log.error("error with deploy verticle " + verticleClassName, res.cause());
                        }
                    });
            });
        this.running = true;
    }

    @Override
    public void stop() {
        IoooVerticleServicesHolder.activeVerticleServices().values()
                                  .forEach(info -> vertx.undeploy(info.getId(), res -> {
                                      if (res.succeeded()) {
                                          if (log.isInfoEnabled()) {
                                              log.info("unload verticle [{}] with id [{}].",
                                                       info.getVerticle().getClass().getSimpleName(), info.getId());
                                          }
                                      } else {
                                          log.error("something happened while unload verticle " + info.getId(),
                                                    res.cause());
                                      }
                                  }));
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
