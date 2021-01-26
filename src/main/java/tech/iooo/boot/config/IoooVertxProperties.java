package tech.iooo.boot.config;

import java.util.Collections;
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

    private DefaultDeploymentOptions defaultDeploymentOptions = new DefaultDeploymentOptions();

    @Data
    public static class DefaultDeploymentOptions {

        private List<String> extraClasspath = Collections.emptyList();
        private Boolean ha = false;
        private Integer instances = 1;
        private List<String> isolatedClasses = Collections.emptyList();
        private String isolationGroup = "";
        private Long maxWorkerExecuteTime = 60_000_000_000L;
        private TimeUnit maxWorkerExecuteTimeUnit = TimeUnit.NANOSECONDS;
        private Boolean worker = false;
        private String workerPoolName = "";
        private Integer workerPoolSize = 20;
    }
}
