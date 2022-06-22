package com.webfirmframework.wffwebcommon;

import jakarta.servlet.http.Part;

public final class FileUtil {

    public static String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static boolean isFile(Part part) {
        return getFileName(part) != null;
    }
}
