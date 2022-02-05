package com.webfirmframework.wffwebconfig.server.servlet;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.attribute.core.AttributeRegistry;
import com.webfirmframework.wffweb.tag.html.core.TagRegistry;
import com.webfirmframework.wffwebconfig.page.IndexPage;
import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Servlet implementation class HomePageServlet
 */
@WebServlet(urlPatterns = { "/ui/*" })
public class IndexPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOGGER = Logger
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
        LOGGER.info("Loaded all wffweb classes");
        ServerConstants.CONTEXT_PATH = getServletContext().getContextPath();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
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

        try (OutputStream os = response.getOutputStream();) {

            HttpSession session = request.getSession();

            session.setMaxInactiveInterval(ServerConstants.SESSION_TIMEOUT_SECONDS);

            IndexPage indexPage = new IndexPage(request.getSession(), request.getRequestURI());

            BrowserPageContext.INSTANCE.addBrowserPage(session.getId(),
                    indexPage);

            indexPage.toOutputStream(os, "UTF-8");
            os.flush();
        }

    }

}
