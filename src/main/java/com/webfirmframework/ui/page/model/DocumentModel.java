package com.webfirmframework.ui.page.model;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import jakarta.servlet.http.HttpSession;

public record DocumentModel(HttpSession httpSession, BrowserPage browserPage) {
}
