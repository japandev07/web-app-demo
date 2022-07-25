package com.webfirmframework.wffwebconfig.page;

import java.util.logging.Logger;

import com.webfirmframework.ui.page.layout.IndexPageLayout;
import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageSession;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerMethod;
import com.webfirmframework.wffwebconfig.AppSettings;
import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;

public class IndexPage extends BrowserPage {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(IndexPage.class.getName());

    // this is a standard interval
    public static final int HEARTBEAT_TIME_MILLISECONDS = 25000;

    private static final int WS_RECONNECT_TIME = 1000;

    private final BrowserPageSession session;
    private final String contextPath;

    public IndexPage(String contextPath, BrowserPageSession session, String uri) {
        //should not set
//        super.setURI(uri);
        this.session = session;
        this.contextPath = contextPath;
    }

    @Override
    public String webSocketUrl() {
        return ServerConstants.DOMAIN_WS_URL
                .concat(ServerConstants.CONTEXT_PATH.concat(ServerConstants.INDEX_PAGE_WS_URI));
    }

    // this is new since 3.0.1
    @Override
    protected void beforeRender() {
        // Write BrowserPage configurations code here.
        // The following code can also be written inside render() method but
        // writing here will be a nice separation of concern.
        super.setWebSocketHeartbeatInterval(HEARTBEAT_TIME_MILLISECONDS);
        super.setWebSocketReconnectInterval(WS_RECONNECT_TIME);
        super.setExecutor(AppSettings.CACHED_THREAD_POOL);
    }

    @Override
    public AbstractHtml render() {
        // Here you can return layout template based on condition,
        // if you are returning conditional layouts then
        // you also have to type check in onInitialClientPing methods
        return new IndexPageLayout(this, session, contextPath);
    }

    @Override
    protected void afterRender(AbstractHtml rootTag) {
        // Here you can add custom server methods (super.addServerMethod) etc...
        addServerMethod("customServerMethodSample1", (ServerMethod) event -> {
            LOGGER.info("customServerMethodSample1 invoked with data = " + event.data());
            return null;
        });
        // ServerMethod can be added from anywhere using browserPage.addServerMethod
        // better place is to write it inside IndexPageLayout.buildMainDivTags method as it will invoke only after browse page is loaded.
        // sample for customServerMethod1 is included in IndexPageLayout.buildMainDivTags method.
    }

    // this is new since 3.0.18
    @Override
    protected void onInitialClientPing(AbstractHtml rootTag) {
        IndexPageLayout layout = (IndexPageLayout) rootTag;
        // to build main div tags only if there is a client communication
        layout.buildMainDivTags();
    }

    public BrowserPageSession getSession() {
        return session;
    }

}
