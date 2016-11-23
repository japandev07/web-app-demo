package com.wffwebdemo.wffwebdemoproject.server.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class HeartBeatUtil {

    private static final Logger LOGGER = Logger
            .getLogger(HeartBeatUtil.class.getName());

    public static void ping(String sessionId) {
        try {
            String url = "http://wffweb.herokuapp.com/heart-beat";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // add request header
            con.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
            int responseCode = con.getResponseCode();

            LOGGER.info("responseCode " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            LOGGER.info("heartbeat response " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
