package com.wffwebdemo.wffwebdemoproject.server.ws;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.webfirmframework.wffweb.PushFailedException;
import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.server.page.WebSocketPushListener;
import com.webfirmframework.wffweb.server.page.action.BrowserPageAction;
import com.wffwebdemo.wffwebdemoproject.page.Threaded;

public class JettyWSServerForIndexPage extends WebSocketAdapter {

    private static final Logger LOGGER = Logger
            .getLogger(JettyWSServerForIndexPage.class.getName());

    private BrowserPage browserPage;
    
    private HttpSession httpSession;
    
    private String wffInstanceId;
    
    private String wsSessionId;

    public JettyWSServerForIndexPage() {
    }

    public JettyWSServerForIndexPage(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public void onWebSocketConnect(final Session session) {
        // TODO Auto-generated method stub
        super.onWebSocketConnect(session);

        LOGGER.info("onWebSocketConnect " + session.hashCode());

        this.wffInstanceId = session.getUpgradeRequest().getParameterMap()
                .get("wffInstanceId").get(0);

        if (httpSession != null) {
            
            Object totalCons = httpSession.getAttribute("totalConnections");
            
            int totalConnections = 0;
            
            if (totalCons != null) {
                totalConnections = (int) totalCons;
            }
            
            totalConnections++;
            httpSession.setAttribute("totalConnections", totalConnections);
            
            // never to close the session on inactivity
            httpSession.setMaxInactiveInterval(-1);
            LOGGER.info("httpSession.setMaxInactiveInterval(-1)");
        }
//        browserPage = BrowserPageContext.INSTANCE.getBrowserPage(wffInstanceId);
        browserPage = BrowserPageContext.INSTANCE.webSocketOpened(wffInstanceId);

        if (browserPage == null) {
            System.out.println("browserPage == null, read comments");

            // get browserPageBytes by wffInstanceId, which is stored in
            // an external persistent storage like db.
            // try {
            // byte[] browserPageBytes = null;
            // ByteArrayInputStream bais = new
            // ByteArrayInputStream(browserPageBytes);
            // ObjectInputStream ois = new ObjectInputStream(bais);
            // browserPage = (BrowserPage) ois.readObject();
            // } catch (Exception e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

         // or refresh the browser in later version
            try {
                session.getRemote().sendBytes(
                        BrowserPageAction.RELOAD.getActionByteBuffer());
            } catch (Exception e) {
                LOGGER.severe(e.toString());
            }
            session.close();
            return;

            // get browserPage instance associated this this instance id
            // and add again, otherwise required to manually refresh the page
            // BrowserPageContext.INSTANCE.addBrowserPage(httpSession.getId(),
            // browserPage);

        } else {

            // try {
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // ObjectOutputStream oos = new ObjectOutputStream(baos);
            // oos.writeObject(browserPage);
            // byte[] browserPageBytes = baos.toByteArray();
            // save browserPageBytes to persistent storage like db so that
            // we can get it by wffInstanceId

            // } catch (Exception e) {
            // e.printStackTrace();
            // }

//            BrowserPageContext.INSTANCE.webSocketOpened(wffInstanceId);
            
            //to stop all running threads
            
            
            if (browserPage instanceof Threaded) {
                Threaded threaded = (Threaded) browserPage;
                threaded.startAllThreads();
            }
        }
        
        wsSessionId = String.valueOf(session.hashCode());

        browserPage.addWebSocketPushListener(wsSessionId,
                new WebSocketPushListener() {

                    @Override
                    public void push(ByteBuffer data) {
                        try {

                            session.getRemote().sendBytes(data);
                            // asyncRemove will make exception if the click is
                            // made many
                            // times
                            // https://bz.apache.org/bugzilla/show_bug.cgi?id=56026
                            // session.getAsyncRemote().sendBinary(ByteBuffer.wrap(message));
                        } catch (Exception e) {
                            throw new PushFailedException(e.getMessage(), e);
                        }
                    }
                });

    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);

        browserPage.websocketMessaged(payload);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        // TODO Auto-generated method stub
        
        LOGGER.info("onWebSocketClose");
        
        super.onWebSocketClose(statusCode, reason);

        LOGGER.info("onWebSocketClose 2");

        if (httpSession != null) {
            

            Object totalCons = httpSession.getAttribute("totalConnections");
            
            int totalConnections = 0;
            
            if (totalCons != null) {
                totalConnections = (int) totalCons;
                totalConnections--;
            }
            
            httpSession.setAttribute("totalConnections", totalConnections);
            
            
            if (totalConnections == 0) {
                httpSession.setMaxInactiveInterval(60 * 30);
            }
            
            LOGGER.info("httpSession.setMaxInactiveInterval(60 * 30)");
        }
        
        if (browserPage instanceof Threaded) {
            Threaded threaded = (Threaded) browserPage;
            threaded.stopAllThreads();
        }
        
        
        try {
            LOGGER.info("getSession().hashCode() " + getSession().hashCode());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BrowserPageContext.INSTANCE.webSocketClosed(wffInstanceId, wsSessionId);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);

        LOGGER.severe(cause.getMessage());
        // cause.printStackTrace(System.err);
    }
}
