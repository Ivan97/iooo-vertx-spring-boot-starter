package tech.iooo.boot.config;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.Callable;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.SimpleAspect;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.iooo.boot.inner.IoooVerticleServicesHolder;

/**
 * @author 龙也
 * @date 2021/1/26 11:46 上午
 */
@Slf4j
@Component
public class IoooVerticleFactory implements VerticleFactory {
    /**
     * @return The prefix for the factory, e.g. "java", or "js".
     */
    @Override
    public String prefix() {
        return Constants.ioooVerticlePrefix;
    }

    /**
     * Create a verticle instance. If this method is likely to be slow (e.g. Ruby or JS verticles which might have to
     * start up a language engine) then make sure it is run on a worker thread by {@link Vertx#executeBlocking}.
     *
     * @param verticleName The verticle name
     * @param classLoader  The class loader
     * @param promise      the promise to complete with the result
     */
    @Override
    public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
        String className = VerticleFactory.removePrefix(verticleName);
        Optional.ofNullable(IoooVerticleServicesHolder.inactiveVerticleServices().get(className))
                .ifPresent(it -> promise.complete(() -> {
                    Verticle verticle = it.getVerticle();
                    return ProxyUtil.proxy(verticle, DeploymentAspect.getInstance());
                }));
    }

    private static class DeploymentAspect extends SimpleAspect {

        private static final long serialVersionUID = -4975424981254459163L;

        private static DeploymentAspect deploymentAspectInstance;

        private DeploymentAspect() {}

        public static DeploymentAspect getInstance() {
            if (deploymentAspectInstance == null) {
                deploymentAspectInstance = new DeploymentAspect();
            }
            return deploymentAspectInstance;
        }

        @Override
        public boolean before(Object target, Method method, Object[] args) {
            if (log.isTraceEnabled()) {
                log.trace("load vertical from IoooVerticleServicesHolder");
            }
            return true;

        }

        @Override
        public boolean after(Object target, Method method, Object[] args, Object returnVal) {
            return true;
        }

        @Override
        public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
            log.error("error when loading vertical from IoooVerticleServicesHolder", e);
            return true;
        }
    }
}
