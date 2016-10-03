package com.wffwebdemo.wffwebdemoproject.page;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage {

    private static final long serialVersionUID = 1L;
    private HttpSession httpSession;
    private IndexPageLayout indexPageLayout;

    @Override
    public String webSocketUrl() {
        return "wss://wffweb.herokuapp.com/ws-for-index-page";
    }
    
    public IndexPage(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
    
    @Override
    public AbstractHtml render() {

        // here we should return the object IndexPageLayout

        indexPageLayout = new IndexPageLayout(httpSession);
        return indexPageLayout;
    }
    
    public void stopAllThreads() {
        List<Thread> allThreads = indexPageLayout.getAllThreads();
        for (Thread thread : allThreads) {
            thread.interrupt();
        }
        allThreads.clear();
    }

}
