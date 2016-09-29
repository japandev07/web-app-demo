package com.wffwebdemo.wffwebdemoproject.page;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage {

    private static final long serialVersionUID = 1L;

    @Override
    public String webSocketUrl() {
        try {
            return "ws://"+InetAddress.getLocalHost().getHostAddress()+"/wffwebdemoproject/ws-for-index-page";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    @Override
    public AbstractHtml render() {

        // here we should return the object IndexPageLayout

        return new IndexPageLayout();
    }

}
