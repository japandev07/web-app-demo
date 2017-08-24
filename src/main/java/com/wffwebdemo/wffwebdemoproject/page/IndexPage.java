package com.wffwebdemo.wffwebdemoproject.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;
import com.wffwebdemo.wffwebdemoproject.page.template.LoginTemplate;

public class IndexPage extends BrowserPage implements Threaded {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger
            .getLogger(IndexPage.class.getName());
    
    private final HttpSession httpSession;
    private IndexPageLayout indexPageLayout;
    private Locale locale;

    @Override
    public String webSocketUrl() {
        return "wss://wffweb.herokuapp.com/ws-for-index-page";
    }

    public IndexPage(final HttpSession httpSession, Locale locale) {
        this.httpSession = httpSession;
        this.locale = locale;
    }

    @Override
    public AbstractHtml render() {
        
        super.removeFromContext(false, BrowserPage.On.INIT_REMOVE_PREVIOUS);
        
        //to add a custom server method
        super.addServerMethod("testServerMethod", new CustomServerMethod(httpSession));
        
        // here we should return the object IndexPageLayout

        indexPageLayout = new IndexPageLayout(httpSession, locale, this);
        return indexPageLayout;
    }

    private List<Thread> allActiveThreads;

    @Override
    public void startAllThreads() {

        if (allActiveThreads != null) {
            return;
        }

        allActiveThreads = new ArrayList<Thread>();
        final List<Runnable> allThreads = indexPageLayout.getAllThreads();
        for (final Runnable runnable : allThreads) {
            final Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
            allActiveThreads.add(thread);
        }

    }

    @Override
    public void stopAllThreads() {

        if (allActiveThreads != null) {
            for (final Thread thread : allActiveThreads) {
                thread.interrupt();
            }
            allActiveThreads = null;
        }
    }
    
    @Override
    protected void removedFromContext() {
        
        LOGGER.info("IndexPage#removedFromContext");
        
        // this method will be invoked when this BrowserPage 
        // is removed from BrowserPageContext
        super.removedFromContext();
        
        stopAllThreads();
        
    }

}
