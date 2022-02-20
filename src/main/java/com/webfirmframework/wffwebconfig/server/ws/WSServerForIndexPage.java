package com.webfirmframework.wffwebconfig.server.ws;

import com.webfirmframework.wffweb.PushFailedException;
import com.webfirmframework.wffweb.server.page.*;
import com.webfirmframework.wffweb.server.page.action.BrowserPageAction;
import com.webfirmframework.wffweb.util.ByteBufferUtil;
import com.webfirmframework.wffwebconfig.AppSettings;
import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.ServerEndpointConfig.Configurator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @ServerEndpoint gives the relative name for the end point This will be
 * accessed via
 * ws://localhost:8080/com.minimalproductionsample/ws-for-index-page
 */
@ServerEndpoint(value = ServerConstants.INDEX_PAGE_WS_URI, configurator = WSServerForIndexPage.class)
@WebListener
public class WSServerForIndexPage extends Configurator {

    private static final Logger LOGGER = Logger
            .getLogger(WSServerForIndexPage.class.getName());

    private static final long HTTP_SESSION_HEARTBEAT_INTERVAL = ServerConstants.SESSION_TIMEOUT_MILLISECONDS
            - (1000 * 60 * 2);

    private volatile PayloadProcessor payloadProcessor;

    private volatile HeartbeatManager heartbeatManager;

    private volatile BrowserPageSession bpSession;

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request, HandshakeResponse response) {

        final Map<String, List<String>> parameterMap = request
                .getParameterMap();

        HttpSession httpSession = null;

        List<String> wffInstanceIds = parameterMap
                .get(BrowserPage.WFF_INSTANCE_ID);
        String instanceId = wffInstanceIds.get(0);

        //httpSession is not required here but if required for some other purpose, it can be obtained as follows
        final BrowserPageSession bpSession = BrowserPageContext.INSTANCE.getSessionByInstanceId(instanceId);
        if (bpSession != null) {
            httpSession = (HttpSession) bpSession.getWeakProperty("httpSession");
        }

        super.modifyHandshake(config, request, response);

        if (httpSession == null) {
            LOGGER.info("session == null");
            return;
        }

        LOGGER.info("modifyHandshake " + httpSession.getId());
    }

    /**
     * @OnOpen allows us to intercept the creation of a new session. The session
     * class allows us to send data to the user. In the method onOpen,
     * we'll let the user know that the handshake was successful.
     */
    @OnOpen
    public void onOpen(final Session session, EndpointConfig config) {

        LOGGER.info("onOpen");

        session.setMaxIdleTimeout(ServerConstants.SESSION_TIMEOUT_MILLISECONDS);

        List<String> wffInstanceIds = session.getRequestParameterMap()
                .get(BrowserPage.WFF_INSTANCE_ID);

        String instanceId = wffInstanceIds.get(0);

        final WebSocketOpenedRecord webSocketOpenedRecord = BrowserPageContext.INSTANCE.webSocketOpened(instanceId,
                k -> new HeartbeatManager(AppSettings.CACHED_THREAD_POOL,
                        HTTP_SESSION_HEARTBEAT_INTERVAL, new HeartbeatRunnable(k)));
        HeartbeatManager hbm = null;
        BrowserPage browserPage = null;
        HttpSession httpSession = null;
        if (webSocketOpenedRecord != null) {
            browserPage = webSocketOpenedRecord.browserPage();
            hbm = webSocketOpenedRecord.heartbeatManager();
            bpSession = webSocketOpenedRecord.session();
            bpSession.userProperties().compute("totalConnections", (k, v) -> {
                int totalConnections = 0;
                if (v != null) {
                    totalConnections = (int) v;
                }
                totalConnections++;
                return totalConnections;
            });

            httpSession = (HttpSession) bpSession.getWeakProperty("httpSession");
        }
        heartbeatManager = hbm;
        //NB: if the server restarted the hbm could be null as the modifyHandshake may not be invoked.
        if (hbm != null) {
            hbm.runAsync();
        }

        if (httpSession != null) {
            LOGGER.info("websocket session id " + session.getId()
                    + " has opened a connection for httpsession id "
                    + httpSession.getId());

            // never to close the session on inactivity
            httpSession.setMaxInactiveInterval(-1);
            LOGGER.info("session.setMaxInactiveInterval(-1)");
        }

        if (browserPage == null) {
            try {
                // or refresh the browser
                session.getBasicRemote().sendBinary(
                        BrowserPageAction.RELOAD.getActionByteBuffer());
                session.close();
                return;
            } catch (IOException e) {
                // NOP
            }
        }

        // Internally it may contain a volatile variable
        // so it's better to declare a dedicated variable before
        // addWebSocketPushListener.
        // If the maxBinaryMessageBufferSize is changed dynamically
        // then call getMaxBinaryMessageBufferSize method directly in
        // sliceIfRequired method as second argument.
        final int maxBinaryMessageBufferSize = session
                .getMaxBinaryMessageBufferSize();

        // NB: do not use browserPage.getPayloadProcessor it has bug
        // payloadProcessor = browserPage.getPayloadProcessor();
        payloadProcessor = new PayloadProcessor(browserPage);

        browserPage.addWebSocketPushListener(session.getId(), data -> {

            ByteBufferUtil.sliceIfRequired(data, maxBinaryMessageBufferSize,
                    (part, last) -> {

                        try {
                            session.getBasicRemote().sendBinary(part, last);
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE,
                                    "IOException while session.getBasicRemote().sendBinary(part, last)",
                                    e);
                            try {
                                session.close();
                            } catch (IOException e1) {
                                LOGGER.log(Level.SEVERE,
                                        "IOException while session.close()",
                                        e1);
                            }
                            throw new PushFailedException(e.getMessage(), e);
                        }

                        return !last;
                    });
        });

    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     */
    @OnMessage
    public void onMessage(ByteBuffer message, boolean last, Session session) {

        payloadProcessor.webSocketMessaged(message, last);

        if (last && message.capacity() == 0) {
            LOGGER.info("client ping message.length == 0");
            HeartbeatManager hbm = heartbeatManager;
            if (hbm != null) {
                LOGGER.info("going to start httpsession hearbeat");
                hbm.runAsync();
            }
        }
    }

    /**
     * The user closes the connection.
     * <p>
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        LOGGER.info("onClose");

        // how much time you want client for inactivity
        // may be it could be the same value given for
        // session timeout in web.xml file.
        // it's valid only when the browser is closed
        // because client will be trying to reconnect.
        // The value is in seconds.
        if (bpSession != null) {

            final int totalConnections = (int) bpSession.userProperties().compute("totalConnections", (k, v) -> {
                int totalConnectionsTmp = 0;
                if (v != null) {
                    totalConnectionsTmp = (int) v;
                }
                if (totalConnectionsTmp > 0) {
                    totalConnectionsTmp--;
                }
                return totalConnectionsTmp;
            });

            final HttpSession httpSession = (HttpSession) bpSession.getWeakProperty("httpSession");
            if (httpSession != null && totalConnections == 0) {
                httpSession.setMaxInactiveInterval(
                        ServerConstants.SESSION_TIMEOUT_SECONDS);
                LOGGER.info("session.setMaxInactiveInterval(" + ServerConstants.SESSION_TIMEOUT_SECONDS + ")");
                HeartbeatManager hbm = heartbeatManager;
                if (hbm != null) {
                    hbm.runAsync();
                }
            }
        }

        LOGGER.info("Session " + session.getId() + " closed");
        List<String> wffInstanceIds = session.getRequestParameterMap()
                .get(BrowserPage.WFF_INSTANCE_ID);

        String instanceId = wffInstanceIds.get(0);
        BrowserPageContext.INSTANCE.webSocketClosed(instanceId,
                session.getId());
        payloadProcessor = null;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // NOP
        // log only if required
        // if (LOGGER.isLoggable(Level.WARNING)) {
        // LOGGER.log(Level.WARNING, throwable.getMessage());
        // }
    }

}
