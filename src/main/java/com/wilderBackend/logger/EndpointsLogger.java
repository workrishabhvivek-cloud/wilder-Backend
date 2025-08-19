//package com.wilderBackend.logger;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//@Component
//public class EndpointsLogger implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Autowired
//    private RequestMappingHandlerMapping mapping;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        mapping.getHandlerMethods().forEach((info, method) -> {
//            System.out.println(info + " -> " + method);
//        });
//    }
//}
