package com.wffwebdemo.wffwebdemoproject.server.ws.servlet;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.wffwebdemo.wffwebdemoproject.server.ws.JettyWSServerForIndexPageCreator;

@WebServlet(name = "Index page WebSocket Servlet", urlPatterns = {
        "/ws-for-index-page" })
public class JettyWSServerForIndexPageServlet extends WebSocketServlet {

    private static final Logger LOGGER = Logger
            .getLogger(JettyWSServerForIndexPageServlet.class.getName());

    private static final long serialVersionUID = 1L;

    @Override
    public void configure(WebSocketServletFactory servletFactory) {

        LOGGER.info("JettyWSServerForIndexPageServlet.configure()");

        // in milliseconds
        servletFactory.getPolicy().setIdleTimeout(1000 * 60 * 30);
        LOGGER.info(
                "servletFactory.getPolicy().setIdleTimeout(1000 * 60 * 30)");
        // servletFactory.register(JettyWSServerForIndexPage.class);

        servletFactory.setCreator(new JettyWSServerForIndexPageCreator());

    }

}
