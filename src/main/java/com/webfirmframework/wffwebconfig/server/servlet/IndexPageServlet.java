package com.webfirmframework.wffwebconfig.server.servlet;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.server.page.BrowserPageSession;
import com.webfirmframework.wffwebcommon.TokenUtil;
import com.webfirmframework.wffwebconfig.page.IndexPage;
import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Servlet implementation class HomePageServlet
 */
@WebServlet(urlPatterns = {"/ui/*"})
public class IndexPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(IndexPageServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexPageServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        // optional
//        TagRegistry.loadAllTagClasses();
//        AttributeRegistry.loadAllAttributeClasses();
//        LOGGER.info("Loaded all wffweb classes");
        ServerConstants.CONTEXT_PATH = getServletContext().getContextPath();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        if (request.getRequestURI().endsWith("/ui")) {
            response.sendRedirect(request.getRequestURI() + "/");
            return;
        } else if (request.getRequestURI().equals(request.getContextPath() + "/")) {
            response.sendRedirect(request.getContextPath() + "/ui/");
            return;
        }

        response.setContentType("text/html;charset=utf-8");
        //NB: it is required to work "Reopen Closed Tab"
        response.setHeader("Cache-Control", "no-store");

        final String contextPath = request.getServletContext().getContextPath();

        String httpSessionId = null;
        HttpSession session = null;
        if (ServerConstants.MULTI_NODE_MODE) {
            final Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (ServerConstants.WFFWEB_TOKEN_COOKIE.equals(cookie.getName())) {
                        if (cookie.getValue() != null) {
                            JSONObject json = TokenUtil.getPayloadFromJWT(cookie.getValue());
                            final Object id = json != null ? json.get("id") : null;
                            httpSessionId = id != null ? String.valueOf(id) : null;
                            break;
                        }
                    }
                }
            }
            if (httpSessionId == null) {
                httpSessionId = UUID.randomUUID().toString();
                Cookie cookie = new Cookie(ServerConstants.WFFWEB_TOKEN_COOKIE, TokenUtil.createJWT(Map.of("id", httpSessionId)));
                cookie.setPath(contextPath + "/ui");
                cookie.setMaxAge(-1);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }
        } else {
            session = request.getSession();
            httpSessionId = session.getId();
            session.setMaxInactiveInterval(ServerConstants.SESSION_TIMEOUT_SECONDS);
        }

        BrowserPageSession bpSession = BrowserPageContext.INSTANCE.getSession(httpSessionId, true);
        if (session != null) {
            bpSession.setWeakProperty("httpSession", session);
        }

        IndexPage indexPage = new IndexPage(contextPath, bpSession, contextPath + request.getRequestURI());

        BrowserPageContext.INSTANCE.addBrowserPage(httpSessionId,
                indexPage);

        try (OutputStream os = response.getOutputStream()) {
            indexPage.toOutputStream(os, "UTF-8");
            os.flush();
        }

    }

}
