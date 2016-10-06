package com.wffwebdemo.wffwebdemoproject.server.ws;

import java.util.logging.Logger;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class JettyWSServerForIndexPageCreator implements WebSocketCreator {

    private static final Logger LOGGER = Logger
            .getLogger(JettyWSServerForIndexPageCreator.class.getName());

    @Override
    public Object createWebSocket(ServletUpgradeRequest request,
            ServletUpgradeResponse response) {

        LOGGER.info("JettyWSServerForIndexPageCreator.createWebSocket()");

        for (String subprotocol : request.getSubProtocols()) {
            LOGGER.info("subprotocol " + subprotocol);
            if ("binary".equals(subprotocol)) {
                LOGGER.info("binary equals subprotocol");
                response.setAcceptedSubProtocol(subprotocol);
            }
        }

        return new JettyWSServerForIndexPage(
                request.getHttpServletRequest().getSession());
    }

}
