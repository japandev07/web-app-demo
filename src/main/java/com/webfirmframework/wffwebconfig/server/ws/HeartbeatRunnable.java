package com.webfirmframework.wffwebconfig.server.ws;

import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class HeartbeatRunnable implements Runnable {
    
    private static final Logger LOGGER = Logger
            .getLogger(HeartbeatRunnable.class.getName());
    
    private final String httpSessionId;
    
    public HeartbeatRunnable(String httpSessionId) {
        this.httpSessionId = httpSessionId;
    }

    @Override
    public void run() {
        if (ServerConstants.CONTEXT_PATH == null) {
            return;
        }
        HttpURLConnection con = null;
        BufferedReader in = null;
        try {
            String url = ServerConstants.DOMAIN_URL
                    .concat(ServerConstants.CONTEXT_PATH)
                    .concat("/heart-beat");

            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            String sessionCookieName = ServerConstants.MULTI_NODE_MODE ? ServerConstants.WFFWEB_TOKEN_COOKIE.concat("=") : "JSESSIONID=";

            con.setRequestProperty("Cookie", sessionCookieName + httpSessionId);
            con.connect();

            int responseCode = con.getResponseCode();

            LOGGER.info("responseCode " + responseCode);

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            LOGGER.info("heartbeat response " + response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }

}
