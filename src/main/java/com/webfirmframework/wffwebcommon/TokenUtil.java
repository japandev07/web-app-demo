package com.webfirmframework.wffwebcommon;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.webfirmframework.wffweb.server.page.LocalStorage;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public final class TokenUtil {

    private static final byte[] SECRET = "dfskfjsdoidksfksdfkjjerdfi#%^@&*)@$*+-h'sdwew]s".getBytes(StandardCharsets.UTF_8);

    public static boolean isValidJWT(LocalStorage.Item token) {
        if (token != null) {
            try {
                Algorithm algorithmHS = Algorithm.HMAC256(SECRET);
                JWTVerifier verifier = JWT.require(algorithmHS)
                        .withIssuer("wffweb")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token.value());
                return true;
            } catch (JWTVerificationException e) {
                //Invalid signature/claims
            }
        }

        return false;
    }

    public static JSONObject getPayloadFromJWT(LocalStorage.Item token) {
        return getPayloadFromJWT(token.value());
    }

    public static JSONObject getPayloadFromJWT(String token) {
        if (token != null) {
            try {
                Algorithm algorithmHS = Algorithm.HMAC256(SECRET);
                JWTVerifier verifier = JWT.require(algorithmHS)
                        .withIssuer("wffweb")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);
                String decodedPayload = new String(Base64.getUrlDecoder().decode(jwt.getPayload()), StandardCharsets.UTF_8);
                return new JSONObject(decodedPayload);
            } catch (JWTVerificationException e) {
                //Invalid signature/claims
            }
        }
        return null;
    }

    public static String createJWT(Map<String, Object> payload) {
        Algorithm algorithmHS = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withPayload(payload)
                .withIssuer("wffweb")
                .sign(algorithmHS);
    }

}
