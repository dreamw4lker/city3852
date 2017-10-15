package com.betanet.city3852.web;

import java.io.IOException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Server runner.
 * 
 * @author shkirkov.au
 */
public class ServerMain {
    private static final String XML_CONFIG_FILES_LOCATION = "classpath:/spring/servlet-context.xml";
    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static final int DEFAULT_SERVER_PORT = 12112;
    private static final int SESSION_MAX_INACTIVE_INTERVAL = 3600;

    private int serverPort = DEFAULT_SERVER_PORT;

    public static void main(String[] args) throws Exception {
        new ServerMain().startServer();
    }

    private void startServer() throws Exception {
        initConfiguration();
        Server server = new Server(serverPort);
        //log.info("City3852 is at port {}", serverPort);
        server.setHandler(getServletContextHandler(getContext()));
        server.start();
        server.join();
    }

    private void initConfiguration() {
        //TODO: server port configuration
    }

    private Handler getServletContextHandler(WebApplicationContext applicationContext) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath(CONTEXT_PATH);
        //addSecurityFilter(contextHandler);
        DispatcherServlet servlet = new DispatcherServlet(applicationContext);
        servlet.setThrowExceptionIfNoHandlerFound(true);
        contextHandler.addServlet(new ServletHolder(servlet), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(applicationContext));
        contextHandler.getSessionHandler().setMaxInactiveInterval(SESSION_MAX_INACTIVE_INTERVAL);
        return contextHandler;
    }

    private WebApplicationContext getContext() {
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocation(XML_CONFIG_FILES_LOCATION);
        return context;
    }
    
    //TODO: securyty filters (if needed)
}
