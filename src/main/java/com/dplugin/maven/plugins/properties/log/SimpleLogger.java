package com.dplugin.maven.plugins.properties.log;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * 日志简单实现类 2016-07-23 21:25:46
 * @author nayuan
 */
public class SimpleLogger implements Logger {

    private static Log logger = null;

    private String prefix;

    /**
     * SimpleLogger
     * @param prefix 输入日志前缀
     */
    public SimpleLogger(String prefix) {
        this.prefix = prefix;
    }

    public static void initLogger(Log logger) {
        if(logger == null) {
            logger = new SystemStreamLog();
        }
        SimpleLogger.logger = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(CharSequence content) {
        if(this.isDebugEnabled()) {
            logger.debug(prefix + content);
        }
    }

    @Override
    public void debug(CharSequence content, Throwable error) {
        if(this.isDebugEnabled()) {
            logger.debug(prefix + content, error);
        }
    }

    @Override
    public void debug(Throwable error) {
        if(this.isDebugEnabled()) {
            logger.debug(error);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(CharSequence content) {
        logger.info(prefix + content);
    }

    @Override
    public void info(CharSequence content, Throwable error) {
        logger.info(prefix + content, error);
    }

    @Override
    public void info(Throwable error) {
        logger.info(error);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(CharSequence content) {
        logger.warn(prefix + content);
    }

    @Override
    public void warn(CharSequence content, Throwable error) {
        logger.warn(prefix + content, error);
    }

    @Override
    public void warn(Throwable error) {
        logger.warn(error);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(CharSequence content) {
        logger.error(prefix + content);
    }

    @Override
    public void error(CharSequence content, Throwable error) {
        logger.error(prefix + content, error);
    }

    @Override
    public void error(Throwable error) {
        logger.error(error);
    }


}
