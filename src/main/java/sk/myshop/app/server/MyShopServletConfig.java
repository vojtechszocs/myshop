package sk.myshop.app.server;

import sk.myshop.command.client.CommandService;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class MyShopServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new AppModule(), new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/myshop/" + CommandService.PATH).with(CommandServiceImpl.class);
            }
        });
    }

    static class AppModule extends AbstractModule {
        @Override
        protected void configure() {
        }

        @Provides
        @Singleton
        UserService getUserService() {
            return UserServiceFactory.getUserService();
        }
    }

}
