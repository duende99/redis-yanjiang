package ded.redisrelation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @ClassName: Slf4jLogUtil
 * @Description: 日志代理工具类
 * @Author: wlk
 * @CreateDate: 2018/7/12 19:11
 * @UpdateDate: 2018/7/12 19:11
 * @Version: 1.0
 */
public class Slf4jLogUtil {


    public static final Logger LOGGER = LoggerFactory.getLogger(Slf4jLogUtil.class);

    /**
     * 错误
     *
     * @param text
     */
    public static void error(String tag, String text, Exception e) {
        StringBuffer stringBufferText = new StringBuffer();
        stringBufferText.append("Message:").append(text);
        LOGGER.error(ThreadLocalUtil.get("loggerTag") + " " + tag + " " + stringBufferText.toString(), e);
    }

    /**
     * 警告
     *
     * @param tag
     * @param text
     * @param params
     */
    public static void warn(String tag, String text, Object... params) {
        StringBuffer stringBufferText = new StringBuffer();
        stringBufferText.append("Message:").append(text);
        if (params == null || params.length == 0) {
            LOGGER.warn(ThreadLocalUtil.get("loggerTag") + " " + tag + " " + stringBufferText.toString());
            return;
        }
        String[] strings = covert(params);
        LOGGER.warn(ThreadLocalUtil.get("loggerTag") + " " + tag + " " + stringBufferText.toString(), strings);
    }

    /**
     * no controller(service) log info
     *
     * @param tag
     * @param text
     * @param params
     */
    public static void info(String tag, String text, Object... params) {
        if (!LOGGER.isInfoEnabled()) {
            return;
        }
        StringBuffer stringBufferText = new StringBuffer();
        stringBufferText.append("Message:").append(text);
        if (params == null || params.length == 0) {
            LOGGER.info(ThreadLocalUtil.get("loggerTag") + " " + tag + " " + stringBufferText.toString());
            return;
        }
        String[] strings = covert(params);
        LOGGER.info(ThreadLocalUtil.get("loggerTag") + " " + tag + " " + stringBufferText.toString(), strings);

    }

    /**
     * threadInfo(service) log info
     *
     * @param tag
     * @param text
     * @param params
     */
    public static void threadInfo(String tag, String text, Object... params) {
        if (!LOGGER.isInfoEnabled()) {
            return;
        }
        StringBuffer stringBufferText = new StringBuffer();
        stringBufferText.append("Message:").append(text);
        if (params == null || params.length == 0) {
            LOGGER.info(tag + " " + stringBufferText.toString());
            return;
        }
        String[] strings = covert(params);
        LOGGER.info(tag + " " + stringBufferText.toString(), strings);
    }

    /**
     * controller log info
     *
     * @param tag
     * @param text
     * @param params
     */
    public static void infoToController(String tag, String text, Object... params) {
        if (!LOGGER.isInfoEnabled()) {
            return;
        }
        if (params == null || params.length == 0) {
            LOGGER.info(ThreadLocalUtil.get("loggerTag") + " " + tag + " " + text);
            return;
        }
        String[] strings = covert(params);
        LOGGER.info(ThreadLocalUtil.get("loggerTag") + " " + tag + " " + text, strings);


    }

    /**
     * Object[] covert String[]
     *
     * @param params
     * @return
     */
    private static String[] covert(Object[] params) {
        String[] strings = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            strings[i] = StringUtils.isEmpty(params[i]) ? "" : params[i].toString();
        }
        return strings;
    }

    public static void debug(String tag, String text, Object... params) {
        if (!LOGGER.isDebugEnabled()) {
            return;
        }
        if (params == null || params.length == 0) {
            LOGGER.debug(tag + text);
            return;
        }
        String[] strings = covert(params);
        LOGGER.debug(tag + text, strings);

    }


    public static class SimpleLogUtil {

        private final static String fileName = Slf4jLogUtil.class.getSimpleName() + ".java";
        private final static String prefix = "com.migu.cs";

        private static String tag(StackTraceElement ste) {
            String[] arrays = ste.getFileName().split("\\.");
            String tag;
            if (arrays != null && arrays.length > 0) {
                return arrays[0] + "." + ste.getMethodName() + ":" + ste.getLineNumber();
            } else {
                tag = "uk";
            }
            return tag;
        }

        private static String tag() {
            StackTraceElement[] stes = Thread.currentThread().getStackTrace();

            if (!stes[3].getFileName().equals(fileName) && stes[3].getClassName().contains(prefix)) {
                return tag(stes[3]);
            }

            for (StackTraceElement ste : stes) {
                if (ste.getFileName().equals(fileName)) {
                    continue;
                }
                if (ste.getClassName().contains(prefix)) {
                    return tag(ste);
                }
            }
            return "tag:nil";
        }


        public static void warn(String text, Object... params) {
            Slf4jLogUtil.warn(tag(), text, params);
        }

        public static void warn(Class<?> class1, String text, Object... params) {
            Slf4jLogUtil.warn("tag:" + class1.getSimpleName(), text, params);
        }

        public static void error(String text, Exception e) {
            Slf4jLogUtil.error(tag(), text, e);
        }

        /**
         * controller log info invoke
         *
         * @param text
         * @param params
         */
        public static void info(String text, Object... params) {
            Slf4jLogUtil.info(tag(), text, params);
        }

        /**
         * thread log info invoke
         * @param tag
         * @param text
         * @param params
         */

        public static void threadInfo(String tag, String text, Object... params) {
            Slf4jLogUtil.threadInfo(tag, text, params);
        }
        /**
         * no controller log info invoke
         *
         * @param text
         * @param params
         */
        public static void infoToController(String text, Object... params) {
            Slf4jLogUtil.infoToController(tag(), text, params);
        }

        public static void info(Class<?> class1, String text, Object... params) {
            Slf4jLogUtil.info("tag:" + class1.getSimpleName(), text, params);
        }

        public static void debug(String text, Object... params) {
            if (!LOGGER.isDebugEnabled()) {
                return;
            }
            Slf4jLogUtil.debug(tag(), text, params);
        }

        public static void debug(Class<?> class1, String text, Object... params) {
            Slf4jLogUtil.debug("tag:" + class1.getSimpleName(), text, params);
        }
    }

}
