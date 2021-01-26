package tech.iooo.boot.config;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author 龙也
 * @date 2021/1/26 11:43 上午
 */
@Slf4j
@Component
public class IoooVertxBeanProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@Nullable Object bean, @Nullable String beanName)
        throws BeansException {
        if (bean instanceof Vertx) {
            IoooVerticleFactory factory = applicationContext.getBean(IoooVerticleFactory.class);
            if (!((Vertx)bean).verticleFactories().contains(factory)) {
                ((Vertx)bean).registerVerticleFactory(factory);
                if (log.isDebugEnabled()) {
                    log.debug("VerticleFactory register");
                }
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
