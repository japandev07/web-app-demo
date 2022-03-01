<%@ page language="java" session="false" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%
    request.getServletContext().getRequestDispatcher("/ui")
            .forward(request, response);
%>