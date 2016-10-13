package com.wffwebdemo.wffwebdemoproject.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage implements Threaded {

    private static final long serialVersionUID = 1L;
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

        //to add a custom server method
        super.addServerMethod("testServerMethod", new CustomServerMethod(httpSession));
        
        // here we should return the object IndexPageLayout

        indexPageLayout = new IndexPageLayout(httpSession, locale);
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

}
