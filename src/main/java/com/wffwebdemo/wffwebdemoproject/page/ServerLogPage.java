package com.wffwebdemo.wffwebdemoproject.page;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.ServerLogPageLayout;

public class ServerLogPage extends BrowserPage {

    private static final long serialVersionUID = 1L;
    
    private ServerLogPageLayout serverLogPageLayout;

    @Override
    public String webSocketUrl() {
        return "wss://wffweb.herokuapp.com/ws-for-index-page";
    }

    @Override
    public AbstractHtml render() {

        // here we should return the object IndexPageLayout

        serverLogPageLayout = new ServerLogPageLayout();
        return serverLogPageLayout;
    }

    public void log(final String msg) {
        serverLogPageLayout.log(msg);
    }
}
