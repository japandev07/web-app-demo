package com.wffwebdemo.wffwebdemoproject.page.model;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.TitleTag;

public class DocumentModel {

    private Body body;

    private TitleTag pageTitle;

    private HttpSession httpSession;

    public DocumentModel() {
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public TitleTag getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(TitleTag pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * @return the httpSession
     */
    public HttpSession getHttpSession() {
        return httpSession;
    }

    /**
     * @param httpSession
     *            the httpSession to set
     */
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

}
