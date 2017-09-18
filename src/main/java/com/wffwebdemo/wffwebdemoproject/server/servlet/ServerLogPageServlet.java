package com.wffwebdemo.wffwebdemoproject.server.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.wffwebdemo.wffwebdemoproject.page.ServerLogPage;

/**
 * Servlet implementation class HomePageServlet
 */
@WebServlet({"/server-log/*"})
public class ServerLogPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServerLogPageServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        
        // all request other than server-log/realtime should redirect to server-log/realtime
        // but server-log/realtime should not be used as a direct link in any wffweb single page.
        // in a wffweb single page use server-log links to avoid unwanted removal of browserPage from its context
        if (request.getRequestURI() == null 
                || !request.getRequestURI().endsWith("server-log/realtime")) {
            
            response.sendRedirect("server-log/realtime");
            return;
        }
        

        try (OutputStream os = response.getOutputStream();) {

            HttpSession session = request.getSession();

            String instanceId = (String) session
                    .getAttribute("serverLogPageInstanceId");

            BrowserPage browserPage = null;
            if (instanceId != null) {

                browserPage = BrowserPageContext.INSTANCE
                        .getBrowserPage(instanceId);

                // if the server is restarted browserPage could be null here
                // so you could save this instance to db after addBrowserPage
                // method
                // and retried from db using browserPage.getInstanceId()

            }

            if (browserPage == null) {
                browserPage = new ServerLogPage();
                BrowserPageContext.INSTANCE.addBrowserPage(session.getId(),
                        browserPage);
                session.setAttribute("serverLogPageInstanceId",
                        browserPage.getInstanceId());
            }

            browserPage.toOutputStream(os, "UTF-8");
        }

    }

}
