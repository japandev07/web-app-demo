package com.webfirmframework.ui.page.common;

import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.common.URIEvent;
import com.webfirmframework.wffweb.server.page.LocalStorage;
import com.webfirmframework.wffweb.tag.html.URIStateSwitch;
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

    SAMPLE_FILES_UPLOAD("/ui/user/samplefilesupload", false, false, true),

    REALTIME_SERVER_LOG("/ui/realtime-server-log", false, false, true);

    private final String uri;

    private final boolean parentPath;

    private final boolean patternType;

    private final boolean loginRequired;

    /**
     * @param uri
     * @param parentPath
     * @param patternType   true if the uri contains path params
     * @param loginRequired
     */
    NavigationURI(String uri, boolean parentPath, boolean patternType, boolean loginRequired) {
        this.uri = uri;
        this.parentPath = parentPath;
        this.patternType = patternType;
        this.loginRequired = loginRequired;
    }

    public Predicate<URIEvent> getPredicate(DocumentModel documentModel) {
        return getPredicate(documentModel, null);
    }

    public Predicate<URIEvent> getPredicate(DocumentModel documentModel, URIStateSwitch forTag) {

        LocalStorage localStorage = documentModel.session().localStorage();
        String contextPath = documentModel.contextPath();
        if (NavigationURI.LOGIN.equals(this)) {
            return uriEvent -> {
                if (forTag != null) {
                    forTag.getCurrentWhenURIProperties().setPreventDuplicateSuccess(true);
                    forTag.getCurrentWhenURIProperties().setPreventDuplicateFail(true);
                }
                return !TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && contextPath.concat(this.uri).equals(uriEvent.uriAfter());
            };
        }
        if (!loginRequired && !parentPath) {
            if (patternType) {
                //no need to set setPreventDuplicateSuccess(true) and setPreventDuplicateFail(true) for
                // patternType uri i.e. which contains path params
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
                return uriEvent -> {
                    if (forTag != null) {
                        forTag.getCurrentWhenURIProperties().setPreventDuplicateSuccess(true);
                        forTag.getCurrentWhenURIProperties().setPreventDuplicateFail(true);
                    }
                    return contextPath.concat(this.uri).equals(uriEvent.uriAfter());
                };
            }
        }
        if (loginRequired && parentPath) {
            if (patternType) {
                //no need to set setPreventDuplicateSuccess(true) and setPreventDuplicateFail(true) for
                // patternType uri i.e. which contains path params
                return uriEvent -> TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && URIUtil.patternMatchesBase(this.uri, uriEvent.uriAfter());
            }
            return uriEvent -> {
                if (forTag != null) {
                    forTag.getCurrentWhenURIProperties().setPreventDuplicateSuccess(true);
                    forTag.getCurrentWhenURIProperties().setPreventDuplicateFail(true);
                }
                return TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && uriEvent.uriAfter().startsWith(contextPath.concat(this.uri));
            };
        } else if (loginRequired) {
            if (patternType) {
                //no need to set setPreventDuplicateSuccess(true) and setPreventDuplicateFail(true) for
                // patternType uri i.e. which contains path params
                return uriEvent -> TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && URIUtil.patternMatches(this.uri, uriEvent.uriAfter());
            }
        }
        return uriEvent -> {
            if (forTag != null) {
                forTag.getCurrentWhenURIProperties().setPreventDuplicateSuccess(true);
                forTag.getCurrentWhenURIProperties().setPreventDuplicateFail(true);
            }
            return TokenUtil.isValidJWT(localStorage.getToken("jwtToken")) && uriEvent.uriAfter().equals(contextPath.concat(this.uri));
        };
    }

    public String getUri(DocumentModel documentModel) {
        return documentModel.contextPath() + uri;
    }
}