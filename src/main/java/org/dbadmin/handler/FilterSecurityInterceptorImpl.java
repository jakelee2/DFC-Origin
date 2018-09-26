package org.dbadmin.handler;

/**
 * Created by henrynguyen on 5/5/16.
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * This class is used to perform a Custom Filter Security Interceptor
 */
public class FilterSecurityInterceptorImpl extends AbstractSecurityInterceptor
    implements Filter {

    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    /**
     * Getter of Security Meta Data Source
     * @return
     */
    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    /**
     * Setter Custom Filter Invocation of Security Meta Data
     * @param newSource
     */
    public void setSecurityMetadataSource(
        FilterInvocationSecurityMetadataSource newSource) {
        this.securityMetadataSource= newSource;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    /**
     * Perform filtering
     * @param request is the request which constructed in ServletRequest class
     * @param response is the general response.
     * @param chain is the Chain of Filters through the Interceptor
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi= new FilterInvocation(request, response, chain);
        invoke(fi);
    }


    @Override
    public Class <? extends Object> getSecureObjectClass() {
        return FilterInvocation.class ;
    }

    /**
     * Invoke the call to do fitlering
     * @param fi is the Filter Inovation
     * @throws IOException
     * @throws ServletException
     */
    public void invoke(FilterInvocation fi) throws IOException,
        ServletException {
        InterceptorStatusToken token= super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null );
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}