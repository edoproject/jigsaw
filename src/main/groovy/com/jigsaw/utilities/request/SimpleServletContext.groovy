package com.jigsaw.utilities.request

import javax.servlet.RequestDispatcher
import javax.servlet.Servlet
import javax.servlet.ServletContext
import javax.servlet.ServletException

class SimpleServletContext implements ServletContext {

    @Override
    ServletContext getContext(String uripath) {
        return null
    }

    @Override
    int getMajorVersion() {
        return 0
    }

    @Override
    int getMinorVersion() {
        return 0
    }

    @Override
    String getMimeType(String file) {
        return null
    }

    @Override
    Set getResourcePaths(String path) {
        return null
    }

    @Override
    URL getResource(String path) throws MalformedURLException {
        return null
    }

    @Override
    InputStream getResourceAsStream(String path) {
        return null
    }

    @Override
    RequestDispatcher getRequestDispatcher(String path) {
        return null
    }

    @Override
    RequestDispatcher getNamedDispatcher(String name) {
        return null
    }

    @Override
    Servlet getServlet(String name) throws ServletException {
        return null
    }

    @Override
    Enumeration getServlets() {
        return null
    }

    @Override
    Enumeration getServletNames() {
        return null
    }

    @Override
    void log(String msg) {

    }

    @Override
    void log(Exception exception, String msg) {

    }

    @Override
    void log(String message, Throwable throwable) {

    }

    @Override
    String getRealPath(String path) {
        return null
    }

    @Override
    String getServerInfo() {
        return null
    }

    @Override
    String getInitParameter(String name) {
        return null
    }

    @Override
    Enumeration getInitParameterNames() {
        return null
    }

    @Override
    Object getAttribute(String name) {
        return null
    }

    @Override
    Enumeration getAttributeNames() {
        return null
    }

    @Override
    void setAttribute(String name, Object object) {

    }

    @Override
    void removeAttribute(String name) {

    }

    @Override
    String getServletContextName() {
        return null
    }
}
