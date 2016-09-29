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
package com.wffwebdemo.wffwebdemoproject.server.ws.servlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.wffwebdemo.wffwebdemoproject.server.ws.JettyWSServerForIndexPage;

public class JettyWSServerForIndexPageServlet extends WebSocketServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void configure(WebSocketServletFactory servletFactory) {

        System.out.println("received request");
        
        //in milliseconds
        servletFactory.getPolicy().setIdleTimeout(1000 * 60 * 30);
        servletFactory.register(JettyWSServerForIndexPage.class);
        
//        servletFactory.setCreator(new JettyWSServerForIndexPageCreator());

    }

}
