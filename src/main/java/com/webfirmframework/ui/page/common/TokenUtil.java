package com.webfirmframework.ui.page.common;

import com.webfirmframework.wffweb.server.page.LocalStorage;

import java.util.UUID;

public final class TokenUtil {

    public static boolean isValidJWT(LocalStorage.Item token) {
        //TODO write real JWT code to validate
        if (token != null) {
            try {
                UUID.fromString(token.value());
                return true;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
