package com.wffwebdemo.wffwebdemoproject.page;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.common.util.ScheduledThreadPool;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

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
        
        //it must be set to false if there is an anchor tag which opens new tab/window to another wffweb single page
        //otherwise the browserPage will be removed from the BrowserPageContext
        //when clicking on the link. But, if the link redirects to another wffweb sigle page then no need to set it false
        super.removeFromContext(false, BrowserPage.On.INIT_REMOVE_PREVIOUS);
        
        
        //to add a custom server method
        super.addServerMethod("testServerMethod", new CustomServerMethod(httpSession));
        
        // here we should return the object IndexPageLayout

        indexPageLayout = new IndexPageLayout(httpSession, locale, this);
        return indexPageLayout;
    }

    

    @Override
    public void startAllThreads() {
        for (Runnable runnable : indexPageLayout.getTimers()) {
            ScheduledThreadPool.NEW_SINGLE_THREAD_SCHEDULED_EXECUTOR.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void stopAllThreads() {

        for (Runnable runnable : indexPageLayout.getTimers()) {
            try {
                ScheduledThreadPool.NEW_SINGLE_THREAD_SCHEDULED_EXECUTOR.cancel(runnable, true);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
