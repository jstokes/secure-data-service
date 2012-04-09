package org.slc.sli.ingestion.tool;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;

import org.slf4j.LoggerFactory;

/**
 * Provides logging to console or file for the offline tool
 *
 * @author npandey
 */
public class LoggerUtil {
    private static Logger logger = null;
    private static LoggerContext loggerContext;
    private static PatternLayoutEncoder encoder;
    private static FileAppender<ILoggingEvent> fileAppender;

    public static FileAppender<ILoggingEvent> getFileAppender() {
        return fileAppender;
    }

    private static ConsoleAppender<ILoggingEvent> consoleAppender;

    static {
        loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        logger = loggerContext.getLogger("LoggerUtil.class");

        encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%date %-5level %msg%n");
        encoder.start();

        consoleAppender = new ConsoleAppender<ILoggingEvent>();
        consoleAppender.setName("ConsoleAppender");
        consoleAppender.setContext(loggerContext);
        consoleAppender.setTarget("System.err");
        consoleAppender.setEncoder(encoder);

        fileAppender = new FileAppender<ILoggingEvent>();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("ToolLog");
        fileAppender.setEncoder(encoder);
    }
    public static Logger getLogger() {
        return logger;
    }

    public static void logToConsole() {
        logger.detachAndStopAllAppenders();

        consoleAppender.start();

        logger.addAppender(consoleAppender);
    }

    public static void logToFile(String file) {
        logger.detachAndStopAllAppenders();

        fileAppender.setFile(file);
        fileAppender.start();

        logger.addAppender(fileAppender);
    }

}
