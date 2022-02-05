package com.webfirmframework.wffwebconfig.server.session.listener;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffwebconfig.page.IndexPage;
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

    @SuppressWarnings("unused")
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        LOGGER.info("SessionListener.sessionDestroyed()");

        HttpSession session = sessionEvent.getSession();
        Object attrValue = session.getAttribute("indexPageInstanceId");

        if (attrValue != null) {
            String indexPageInstanceId = attrValue.toString();
            IndexPage indexPage = (IndexPage) BrowserPageContext.INSTANCE
                    .getBrowserPage(indexPageInstanceId);

        }

        BrowserPageContext.INSTANCE.httpSessionClosed(session.getId());
    }

}
