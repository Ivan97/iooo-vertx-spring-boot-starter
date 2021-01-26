package tech.iooo.boot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import io.vertx.core.VertxOptions;

/**
 * @author 龙也
 * @date 2021/1/26 11:37 上午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VerticleDeploymentOptions {

    @Deprecated
    String extraClasspath() default "";

    boolean ha() default false;

    int instances() default 1;

    long maxWorkerExecuteTime() default 60_000_000_000L;

    TimeUnit maxWorkerExecuteTimeUnit() default TimeUnit.NANOSECONDS;

    boolean worker() default false;

    int workerPoolSize() default VertxOptions.DEFAULT_WORKER_POOL_SIZE;

    @Deprecated
    String isolationGroup() default "";

    String workerPoolName() default "";
}
