package tech.iooo.boot.inner;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import io.vertx.core.Verticle;
import lombok.Builder;
import lombok.Data;
import lombok.Synchronized;
import lombok.experimental.Accessors;

/**
 * Created on 2018/9/3 上午10:10
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public class IoooVerticleServicesHolder {

    /**
     * ClassName deploymentId instance
     */
    private static final Map<String, VerticleInfo> VERTICLE_SERVICES = Maps.newConcurrentMap();

    public static void addVerticle(String className, VerticleInfo info) {
        VERTICLE_SERVICES.put(className, info);
    }

    @Synchronized
    public static Map<String, VerticleInfo> verticleServices() {
        return VERTICLE_SERVICES;
    }

    @Synchronized
    public static Map<String, VerticleInfo> inactiveVerticleServices() {
        return VERTICLE_SERVICES.entrySet().stream()
                                .filter(entry -> !entry.getValue().active)
                                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Synchronized
    public static Map<String, VerticleInfo> activeVerticleServices() {
        return VERTICLE_SERVICES.entrySet().stream()
                                .filter(entry -> entry.getValue().active)
                                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Synchronized
    public static void active(String className, String id) {
        VERTICLE_SERVICES.get(className).setActive(true).setId(id);
    }

    @Data
    @Builder
    @Accessors(chain = true)
    public static class VerticleInfo {
        private String id;
        private Verticle verticle;
        private Boolean active;
    }
}
