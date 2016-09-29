package com.wffwebdemo.wffwebdemoproject.page;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage {

    private static final long serialVersionUID = 1L;

    @Override
    public String webSocketUrl() {
        try {
//            return "wss://"+InetAddress.getLocalHost().getHostAddress()+"/ws-for-index-page";
//            return "wss://wffweb.herokuapp.com:"+System.getenv("PORT")+"/ws-for-index-page";
            return "wss://wffweb.herokuapp.com/ws-for-index-page";
        } catch (Exception e) {
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
