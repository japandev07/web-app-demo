package com.webfirmframework.ui.page.model;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageSession;

public record DocumentModel(BrowserPageSession session, BrowserPage browserPage,
                            String contextPath) {

}
