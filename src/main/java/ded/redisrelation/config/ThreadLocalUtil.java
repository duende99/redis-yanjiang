package ded.redisrelation.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: card-business
 * @Package: com.migu.cb.util
 * @ClassName: ThreadLocalUtil
 * @Description: 本地线程内共享数据工具类
 * @Author: wlk
 * @CreateDate: 2018/7/16 13:14
 * @UpdateDate: 2018/7/16 13:14
 * @Version: 1.0
 */
public final class ThreadLocalUtil {
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal() {
        protected Map<String, Object> initialValue() {
            return new HashMap();
        }
    };

    public static Map<String, Object> getThreadLocal() {
        return threadLocal.get();
    }

    public static <T> T get(String key) {
        Map map = (Map) threadLocal.get();
        return (T) map.get(key);
    }

    public static <T> T get(String key, T defaultValue) {
        Map map = (Map) threadLocal.get();
        return (T) map.get(key) == null ? defaultValue : (T) map.get(key);
    }

    public static void set(String key, Object value) {
        threadLocal.get().put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        threadLocal.get().putAll(keyValueMap);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static <T> T remove(String key) {
        return (T) threadLocal.get().remove(key);
    }

}
