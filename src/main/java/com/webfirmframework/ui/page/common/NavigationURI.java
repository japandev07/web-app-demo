package com.webfirmframework.ui.page.common;

import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.common.URIEvent;
import com.webfirmframework.wffweb.server.page.LocalStorage;
import com.webfirmframework.wffweb.util.URIUtil;
import com.webfirmframework.wffwebcommon.TokenUtil;

import java.util.Map;
import java.util.function.Predicate;

public enum NavigationURI {

    LOGIN("/ui/login", false, false, false),

    USER("/ui/user/", true, false, true),

    VIEW_ITEMS("/ui/user/items/view", false, false, true),

    ADD_ITEM("/ui/user/items/add", false, false, true),

    ITEM_PRICE_HISTORY_CHART("/ui/user/items/pricehistory/{itemId}", false, true, true),

    SAMPLE_TEMPLATE1("/ui/user/sampletemplate1", false, false, true),

    SAMPLE_TEMPLATE2("/ui/user/sampletemplate2", false, false, true),

    REALTIME_CLOCK("/ui/user/realtimeclock", false, false, true),

    REALTIME_SERVER_LOG("/ui/realtime-server-log", false, false, true);

    private final String uri;

    private final boolean parentPath;

    private final boolean patternType;

    private final boolean loginRequired;

    NavigationURI(String uri, boolean parentPath, boolean patternType, boolean loginRequired) {
        this.uri = uri;
        this.parentPath = parentPath;
        this.patternType = patternType;
        this.loginRequired = loginRequired;
    }


    public Predicate<URIEvent> getPredicate(DocumentModel documentModel) {

        LocalStorage localStorage = documentModel.session().localStorage();
        String contextPath = documentModel.contextPath();
        if (NavigationURI.LOGIN.equals(this)) {
            return uriEvent -> !TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && contextPath.concat(this.uri).equals(uriEvent.uriAfter());
        }
        if (!loginRequired && !parentPath) {
            if (patternType) {
                return uriEvent -> {
                    try {
                        Map<String, String> pathParamValues = URIUtil.parseValues(this.uri, uriEvent.uriAfter());
                        return pathParamValues.size() > 0;
                    } catch (InvalidValueException e) {
                        //NOP
                    }
                    return false;
                };
            } else {
                return uriEvent -> contextPath.concat(this.uri).equals(uriEvent.uriAfter());
            }
        }
        if (loginRequired && parentPath) {
            if (patternType) {
                return uriEvent -> TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && URIUtil.patternMatchesBase(this.uri, uriEvent.uriAfter());
            }
            return uriEvent -> TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && uriEvent.uriAfter().startsWith(contextPath.concat(this.uri));
        } else if (loginRequired) {
            if (patternType) {
                return uriEvent -> TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && URIUtil.patternMatches(this.uri, uriEvent.uriAfter());
            }
        }
        return uriEvent -> TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && uriEvent.uriAfter().equals(contextPath.concat(this.uri));
    }

    public String getUri(DocumentModel documentModel) {
        return documentModel.contextPath() + uri;
    }
}