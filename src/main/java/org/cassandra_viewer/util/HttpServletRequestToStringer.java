package org.cassandra_viewer.util;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
* User: captain-protect
* Date: 4/5/12
* Time: 11:57 AM
*/
public class HttpServletRequestToStringer {
    private final HttpServletRequest request;

    public HttpServletRequestToStringer(HttpServletRequest request) {
        this.request = request;
    }

    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    public String getAuthType() {
        return request.getAuthType();
    }

    public int getContentLength() {
        return request.getContentLength();
    }

    public String getContentType() {
        return request.getContentType();
    }

    public Enumeration getHeaderNames() {
        return request.getHeaderNames();
    }

    public Enumeration getParameterNames() {
        return request.getParameterNames();
    }

    public String getMethod() {
        return request.getMethod();
    }

    public Map getParameterMap() {
        return request.getParameterMap();
    }

    public String getPathInfo() {
        return request.getPathInfo();
    }

    public String getProtocol() {
        return request.getProtocol();
    }

    public String getPathTranslated() {
        return request.getPathTranslated();
    }

    public String getScheme() {
        return request.getScheme();
    }

    public String getContextPath() {
        return request.getContextPath();
    }

    public String getServerName() {
        return request.getServerName();
    }

    public String getQueryString() {
        return request.getQueryString();
    }

    public int getServerPort() {
        return request.getServerPort();
    }

    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    public String getRemoteHost() {
        return request.getRemoteHost();
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public String getRequestedSessionId() {
        return request.getRequestedSessionId();
    }

    public StringBuffer getRequestURL() {
        return request.getRequestURL();
    }

    public Locale getLocale() {
        return request.getLocale();
    }

    public String getServletPath() {
        return request.getServletPath();
    }

    public Enumeration getLocales() {
        return request.getLocales();
    }

    public int getRemotePort() {
        return request.getRemotePort();
    }

    public boolean isRequestedSessionIdValid() {
        return request.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return request.isRequestedSessionIdFromCookie();
    }

    public boolean isSecure() {
        return request.isSecure();
    }

    public String getLocalName() {
        return request.getLocalName();
    }

    public String getLocalAddr() {
        return request.getLocalAddr();
    }

    public int getLocalPort() {
        return request.getLocalPort();
    }

    public boolean isRequestedSessionIdFromURL() {
        return request.isRequestedSessionIdFromURL();
    }

    @Override
    public String toString() {
        return "HttpServletRequest{" +
                "attributeNames=" + getAttributeNames() +
                ", characterEncoding='" + getCharacterEncoding() + '\'' +
                ", authType='" + getAuthType() + '\'' +
                ", contentLength=" + getContentLength() +
                ", contentType='" + getContentType() + '\'' +
                ", headerNames=" + getHeaderNames() +
                ", parameterNames=" + getParameterNames() +
                ", method='" + getMethod() + '\'' +
                ", parameterMap=" + getParameterMap() +
                ", pathInfo='" + getPathInfo() + '\'' +
                ", protocol='" + getProtocol() + '\'' +
                ", pathTranslated='" + getPathTranslated() + '\'' +
                ", scheme='" + getScheme() + '\'' +
                ", contextPath='" + getContextPath() + '\'' +
                ", serverName='" + getServerName() + '\'' +
                ", queryString='" + getQueryString() + '\'' +
                ", serverPort=" + getServerPort() +
                ", remoteUser='" + getRemoteUser() + '\'' +
                ", remoteAddr='" + getRemoteAddr() + '\'' +
                ", remoteHost='" + getRemoteHost() + '\'' +
                ", userPrincipal=" + getUserPrincipal() +
                ", requestURI='" + getRequestURI() + '\'' +
                ", requestedSessionId='" + getRequestedSessionId() + '\'' +
                ", requestURL=" + getRequestURL() +
                ", locale=" + getLocale() +
                ", servletPath='" + getServletPath() + '\'' +
                ", locales=" + getLocales() +
                ", remotePort=" + getRemotePort() +
                ", requestedSessionIdValid=" + isRequestedSessionIdValid() +
                ", requestedSessionIdFromCookie=" + isRequestedSessionIdFromCookie() +
                ", secure=" + isSecure() +
                ", localName='" + getLocalName() + '\'' +
                ", localAddr='" + getLocalAddr() + '\'' +
                ", localPort=" + getLocalPort() +
                ", requestedSessionIdFromURL=" + isRequestedSessionIdFromURL() +
                '}';
    }
}
