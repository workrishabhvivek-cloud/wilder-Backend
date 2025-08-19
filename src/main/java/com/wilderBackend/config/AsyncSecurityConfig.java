package com.wilderBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AsyncSecurityConfig implements WebMvcConfigurer {

    @Bean(name = "streamingExecutor")
    public ThreadPoolTaskExecutor streamingExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(5);
        exec.setMaxPoolSize(20);
        exec.setQueueCapacity(100);
        exec.setThreadNamePrefix("stream-exec-");
        exec.initialize();
        return exec;
    }

    @Bean(name = "delegatingSecurityExecutor")
    public DelegatingSecurityContextAsyncTaskExecutor delegatingSecurityExecutor(ThreadPoolTaskExecutor streamingExecutor) {

        System.out.println("AsyncSecurityConfig loaded");

        // Wrap the executor so it copies the SecurityContext to tasks it runs
        return new DelegatingSecurityContextAsyncTaskExecutor(streamingExecutor);
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 15 minutes timeout (milliseconds)
        configurer.setDefaultTimeout(15 * 60 * 1000L);

        // Use the delegating executor for Spring MVC async processing (StreamingResponseBody, DeferredResult, etc.)
        configurer.setTaskExecutor(delegatingSecurityExecutor(streamingExecutor()));
    }

}
