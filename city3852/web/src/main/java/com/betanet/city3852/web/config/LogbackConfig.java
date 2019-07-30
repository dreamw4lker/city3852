package com.betanet.city3852.web.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.ContextAwareBase;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
public class LogbackConfig extends ContextAwareBase implements Configurator {

    @Override
    public void configure(LoggerContext loggerContext) {
        loggerContext.reset();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        try {
            configurator.doConfigure(getConfigurationStream());
        } catch (JoranException e) {
            log.error(e.getMessage(), e);
        }
    }

    private InputStream getConfigurationStream() {
        File externalConfigFile = new File("logback.xml");
        if (externalConfigFile.exists()) {
            try {
                return new FileInputStream(externalConfigFile);
            } catch (Exception e) {
                log.error("Cannot read external logback config for city3852 server: " + e.getMessage(), e);
            }
        }
        return LogbackConfig.class.getResourceAsStream("/logback-default.xml");
    }
}
