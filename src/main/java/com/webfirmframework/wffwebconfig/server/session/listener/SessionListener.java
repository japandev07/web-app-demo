package com.webfirmframework.wffwebconfig.server.session.listener;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.logging.Logger;

@WebListener
public class SessionListener implements HttpSessionListener {

    private static final Logger LOGGER = Logger
            .getLogger(SessionListener.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        LOGGER.info("SessionListener.sessionCreated()");
        // NOP
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        LOGGER.info("SessionListener.sessionDestroyed()");

        if (!ServerConstants.MULTI_NODE_MODE) {
            //the below code is required only if the multi node mode is disabled.
            //for multi node mode internally it does nothing.
            HttpSession session = sessionEvent.getSession();
            BrowserPageContext.INSTANCE.httpSessionClosed(session.getId());
        }
    }

}
