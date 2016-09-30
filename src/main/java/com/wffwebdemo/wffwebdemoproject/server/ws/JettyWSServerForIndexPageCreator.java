/*
 * Copyright 2014-2016 Web Firm Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wffwebdemo.wffwebdemoproject.server.ws;

import java.util.logging.Logger;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class JettyWSServerForIndexPageCreator implements WebSocketCreator {

    private static final Logger LOGGER = Logger
            .getLogger(JettyWSServerForIndexPageCreator.class.getName());

    @Override
    public Object createWebSocket(ServletUpgradeRequest request,
            ServletUpgradeResponse response) {

        LOGGER.info("JettyWSServerForIndexPageCreator.createWebSocket()");

        for (String subprotocol : request.getSubProtocols()) {
            LOGGER.info("subprotocol " + subprotocol);
            if ("binary".equals(subprotocol)) {
                LOGGER.info("binary equals subprotocol");
                response.setAcceptedSubProtocol(subprotocol);
            }
        }

        return new JettyWSServerForIndexPage(
                request.getHttpServletRequest().getSession());
    }

}
