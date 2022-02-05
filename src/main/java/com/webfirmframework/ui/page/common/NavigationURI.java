package com.webfirmframework.ui.page.common;

import com.webfirmframework.ui.page.model.DocumentModel;
import jakarta.servlet.http.HttpSession;

import java.util.function.Predicate;

public enum NavigationURI {

    LOGIN("/ui/login", false, false),

    USER("/ui/user/", true, true),

    VIEW_ITEMS("/ui/user/items/view", false, true),

    ADD_ITEM("/ui/user/items/add", false, true),

    ITEM_PRICE_HISTORY_CHART("/ui/user/items/pricehistory", false, true),

    SAMPLE_TEMPLATE1("/ui/user/sampletemplate1", false, true),

    SAMPLE_TEMPLATE2("/ui/user/sampletemplate2", false, true);

    private final String uri;

    private final boolean parentPath;

    private final boolean loginRequired;

    NavigationURI(String uri, boolean parentPath, boolean loginRequired) {
        this.uri = uri;
        this.parentPath = parentPath;
        this.loginRequired = loginRequired;
    }

    public Predicate<String> getPredicate(DocumentModel documentModel) {
        HttpSession httpSession = documentModel.httpSession();
        String contextPath = httpSession.getServletContext().getContextPath();
        if (NavigationURI.LOGIN.equals(this)) {
            return uri -> !"true".equals(httpSession.getAttribute("loginStatus")) && contextPath.concat(this.uri).equals(uri);
        }
        if (!loginRequired) {
            return uri -> contextPath.concat(this.uri).equals(uri);
        }
        if (parentPath) {
            return uri -> "true".equals(httpSession.getAttribute("loginStatus")) && uri.startsWith(contextPath.concat(this.uri));
        }
        return uri -> "true".equals(httpSession.getAttribute("loginStatus")) && uri.equals(contextPath.concat(this.uri));
    }

    public String getUri(DocumentModel documentModel) {
        return documentModel.httpSession().getServletContext().getContextPath() + uri;
    }
}