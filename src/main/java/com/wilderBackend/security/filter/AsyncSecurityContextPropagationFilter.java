package com.wilderBackend.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Restores a SecurityContext stored as a request attribute (ATTR) for both REQUEST and ASYNC dispatches.
 */
@Slf4j
public class AsyncSecurityContextPropagationFilter extends OncePerRequestFilter {

    public static final String ATTR = "ASYNC_SECURITY_CONTEXT";

    // Allow this filter to run for async dispatches too
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        // return false => do NOT skip async dispatch; i.e. allow filtering in async dispatch
        return false;
    }

    // We want the filter to run always (REQUEST and ASYNC)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Save previous context so we can restore after the filter completes on this thread:
        SecurityContext previous = SecurityContextHolder.getContext();

        try {
            // Try to restore SecurityContext that the controller placed on the request
            Object attr = request.getAttribute(ATTR);
            if (attr instanceof SecurityContext sc) {
                SecurityContextHolder.setContext(sc);
//                log.debug("[AsyncSecurityContextPropagationFilter] Restored SecurityContext on thread={} dispatcherType={} auth={}",
//                        Thread.currentThread().getName(), request.getDispatcherType(),
//                        SecurityContextHolder.getContext().getAuthentication());
            }
//            else {
//                log.debug("[AsyncSecurityContextPropagationFilter] No SecurityContext found on request attribute for dispatcherType={}",
//                        request.getDispatcherType());
//            }

            filterChain.doFilter(request, response);
        } finally {
            // Restore whatever was on the thread before we changed it
            SecurityContextHolder.setContext(previous);
        }
    }
}
