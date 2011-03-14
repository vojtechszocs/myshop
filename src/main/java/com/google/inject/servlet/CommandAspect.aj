package com.google.inject.servlet;

import java.lang.reflect.Constructor;

import javax.servlet.ServletContext;

import sk.myshop.command.aspect.Replaceable;

import com.google.inject.Injector;

public aspect CommandAspect {

    private pointcut googleGwtOnly(): 
        within(com.google.gwt.user.server.rpc..*);

    @SuppressWarnings("unchecked")
    private pointcut reflectiveConstructorInstantiations(Constructor c):
        target(c) && call(* Constructor.newInstance(..)) && if(c.getDeclaringClass().getAnnotation(Replaceable.class) != null);

    @SuppressWarnings("unchecked")
    Object around(Constructor c): reflectiveConstructorInstantiations(c) && googleGwtOnly() {
        return doReplace(c.getDeclaringClass());
    }

    protected Object doReplace(Class<?> replacer) {
        Class<?> replacee = replacer.getAnnotation(Replaceable.class).value();
        ServletContext servletContext = GuiceFilter.servletContext.get();
        Injector injector = (Injector) servletContext.getAttribute(GuiceServletContextListener.INJECTOR_NAME);

        // Replacee classes use Guice just-in-time binding, so they
        // are free to define scopes if necessary (e.g. @Singleton)
        return injector.getInstance(replacee);
    }

}
