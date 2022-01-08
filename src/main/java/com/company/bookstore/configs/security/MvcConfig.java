package com.company.bookstore.configs.security;


import com.company.bookstore.storage.StorageProperties;
import com.company.bookstore.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/upload","classpath:/static/js","classpath:/static/socket-client","/webjars/socket-client",
            "classpath:/static/img","classpath:/static/css","/webjars/bootstrap","/webjars/jquery","/webjars/sockjs-client" };

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/book").setViewName("book");
    }
    // path for show the files without the login, you must add it from security config too
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/static/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        registry.addResourceHandler("/webjars/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
//        didn't work until some times - I didn't understand why, and I changed it with below and added @Value("${upload.path}") from the top
//        registry.addResourceHandler("/**")
//                .addResourceLocations("file://" + new StorageProperties().getLocation() + "/");
        registry.addResourceHandler("/**")
                .addResourceLocations("file://"  + uploadPath + "/");
    }

//    internationalization https://www.javadevjournal.com/spring-boot/spring-boot-internationalization/
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(localeChangeInterceptor());
    }
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("eng"));
        return  localeResolver;
    }
    //upload config
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            //storageService.deleteAll();
            storageService.init();
        };
    }
}