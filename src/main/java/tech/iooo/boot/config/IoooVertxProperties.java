package tech.iooo.boot.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 龙也
 * @date 2021/1/26 11:26 上午
 */
@Data
@ConfigurationProperties(prefix = "vertx")
public class IoooVertxProperties {

    private IoooVertxProperties.Verticle verticle = new IoooVertxProperties.Verticle();
    private DefaultDeploymentOption defaultDeploymentOption = new IoooVertxProperties.DefaultDeploymentOption();

    @Data
    public static class Verticle {
        /**
         * deploy过程是否开启快速失败
         */
        private boolean failFast = false;
    }

    @Data
    public static class DefaultDeploymentOption {

        private String config;
        private List<String> extraClasspath;
        private Boolean ha;
        private Integer instances;
        private List<String> isolatedClasses;
        private String isolationGroup;
        private Long maxWorkerExecuteTime;
        private TimeUnit maxWorkerExecuteTimeUnit;
        private Boolean worker;
        private String workerPoolName;
        private Integer workerPoolSize;
    }
}
