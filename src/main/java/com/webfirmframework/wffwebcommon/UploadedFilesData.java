package com.webfirmframework.wffwebcommon;

import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Map;

public record UploadedFilesData(Map<String, String[]> requestParamMap, List<Part> parts) {
}
