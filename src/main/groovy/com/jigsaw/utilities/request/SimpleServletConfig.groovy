package com.jigsaw.utilities.request

import javax.servlet.ServletConfig
import javax.servlet.ServletContext

class SimpleServletConfig implements ServletConfig{
    @Override
    String getServletName() {
        return null
    }

    @Override
    ServletContext getServletContext() {
        return new SimpleServletContext()
    }

    @Override
    String getInitParameter(String name) {
        return null
    }

    @Override
    Enumeration getInitParameterNames() {
        return null
    }
}
