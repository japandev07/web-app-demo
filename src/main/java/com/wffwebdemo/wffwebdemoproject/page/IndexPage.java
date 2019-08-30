package com.wffwebdemo.wffwebdemoproject.page;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.SharedTagContent;
import com.webfirmframework.wffweb.tag.html.SharedTagContent.UpdateClientNature;
import com.wffwebdemo.wffwebdemoproject.common.util.ScheduledThreadPool;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage implements Threaded {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger
            .getLogger(IndexPage.class.getName());
    
    public static final SharedTagContent<Date> CURRENT_DATE_TIME_STC = new SharedTagContent<>(UpdateClientNature.SEQUENTIAL, new Date());
    
    private static volatile ScheduledFuture<?> TIME_PRINTING_THREAD;
    
    private static final AtomicInteger TOTAL_ONLINE_USERS = new AtomicInteger(0);
    
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
    
    private static void initTimerThread() {
        if (TIME_PRINTING_THREAD == null) {
            synchronized (IndexPage.class) {
                if (TIME_PRINTING_THREAD == null) {
                    TIME_PRINTING_THREAD = ScheduledThreadPool.NEW_SINGLE_THREAD_SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> CURRENT_DATE_TIME_STC.setContent(new Date()), 1, 1, TimeUnit.SECONDS);            
                }
            }
        }        
    }
    
    private static void stopTimerThread() {
        if (TIME_PRINTING_THREAD != null) {
            synchronized (IndexPage.class) {
                if (TIME_PRINTING_THREAD != null && TOTAL_ONLINE_USERS.get() == 0) {
                    TIME_PRINTING_THREAD.cancel(true);
                    TIME_PRINTING_THREAD = null;            
                }
            }
        }        
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
        TOTAL_ONLINE_USERS.incrementAndGet();
        initTimerThread();
    }

    @Override
    public void stopAllThreads() {
        final int count = TOTAL_ONLINE_USERS.decrementAndGet();
        if (count == 0) {
            stopTimerThread();
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
