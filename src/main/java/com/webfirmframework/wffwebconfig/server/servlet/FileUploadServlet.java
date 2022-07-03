package com.webfirmframework.wffwebconfig.server.servlet;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.server.page.BrowserPageSession;
import com.webfirmframework.wffwebcommon.FileUtil;
import com.webfirmframework.wffwebcommon.UploadedFilesData;
import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "FileUploadServlet", urlPatterns = {ServerConstants.FILE_UPLOAD_URI})
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private final static Logger LOGGER =
            Logger.getLogger(FileUploadServlet.class.getCanonicalName());


    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        final String secretKeyName = "fileSecretKey";
        final String fileSecretKey = request.getParameter(secretKeyName);

        //httpSessionId should not be taken by cookie
        final String httpSessionId = request.getParameter("sessionId");
        final String instanceId = request.getParameter("instanceId");
        final String serverMethod = request.getParameter("serverMethod");

        boolean validRequest = false;
        BrowserPageSession session = null;
        if (httpSessionId != null && fileSecretKey != null) {
            session = BrowserPageContext.INSTANCE.getSession(httpSessionId);
            if (session != null) {
                validRequest = fileSecretKey.equals(session.userProperties().get(secretKeyName));
            }
        }
        BrowserPage browserPage = BrowserPageContext.INSTANCE.getBrowserPageIfValid(instanceId);
        if (browserPage == null) {
            validRequest = false;
        }

        if (!validRequest) {
            response.setContentType("text/html;charset=UTF-8");
            try (final PrintWriter writer = response.getWriter();) {
                writer.println("ERROR: Unauthorized request");
            }
            LOGGER.log(Level.SEVERE, "Unauthorized request");
            throw new ServletException("Unauthorized request");
        }

        try {

            List<Part> filesOnly = new ArrayList<>();
            for (Part part : request.getParts()) {
                if (FileUtil.isFile(part)) {
                    filesOnly.add(part);
                }
            }

            Map<String, String[]> requestParamMap = request.getParameterMap();
            String uri = request.getContextPath() + request.getRequestURI();
            UploadedFilesData uploadedFilesData = new UploadedFilesData(requestParamMap, filesOnly);

            browserPage.invokeServerMethod(serverMethod, uploadedFilesData, uri);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);
            try (final PrintWriter writer = response.getWriter();) {
                writer.println("{\"status\":\"success\"}");
            }
        } catch (Exception fne) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);
            try (final PrintWriter writer = response.getWriter();) {
                writer.println("{\"status\":\"failed\"}");
            }
            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                    new Object[]{fne.getMessage()});
        }
    }


}
