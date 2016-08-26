package home.consul.zuul

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import net.logstash.logback.appender.LogstashTcpSocketAppender
import net.logstash.logback.encoder.LogstashEncoder
import org.slf4j.LoggerFactory

class SampleLogging {

    static void main(String[] args){
//        manuallyConfigured()
        automaticallyConfigured()
    }

    private static void automaticallyConfigured() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.info "test this"
    }

    private static void manuallyConfigured() {
        LogstashTcpSocketAppender logstashTcpSocketAppender;
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        LoggerContext loggerContext = rootLogger.getLoggerContext();
        //loggerContext.reset(); // shouldn't need to use that

        logstashTcpSocketAppender = new LogstashTcpSocketAppender();
        logstashTcpSocketAppender.setName("logstash");
        logstashTcpSocketAppender.setContext(loggerContext);
        logstashTcpSocketAppender.setRemoteHost("localhost");
        logstashTcpSocketAppender.setPort(5006);

        LogstashEncoder encoder = new LogstashEncoder();
        encoder.setContext(loggerContext);
        encoder.start();

        logstashTcpSocketAppender.setEncoder(encoder);
        logstashTcpSocketAppender.start();

        rootLogger.addAppender(logstashTcpSocketAppender);
        rootLogger.setLevel(Level.ALL);
        rootLogger.warn("TEST");
    }
}
