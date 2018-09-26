package org.dbadmin.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * Created by henrynguyen on 4/29/16.
 * This class to obtain the Meta Data Source which map from url to its access.
 */
public class FilterSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    // Path Matcher using Ant Format.
    private PathMatcher urlMatcher= new AntPathMatcher();
    private TemplateService templateService;

    @Autowired
    public FilterSecurityMetadataSourceImpl(TemplateService templateService) {
        this.templateService = templateService;
    }

    // According to a URL, Find out permission configuration of this URL.
    public Collection <ConfigAttribute> getAttributes(Object object)
        throws IllegalArgumentException {
        String url= ((FilterInvocation)object).getRequestUrl();
        Map<String, Collection <ConfigAttributeImpl>> routesMap = templateService.getRoutesMap();
        List<String> urls = routesMap.keySet().stream().collect(Collectors.toList());

        Collections.sort(urls, (url1, url2) -> {
            Integer priority1 = routesMap.get(url1).stream().map(ca -> ca.getPriority()).min(Integer::min).get();
            Integer priority2 = routesMap.get(url2).stream().map(ca -> ca.getPriority()).min(Integer::min).get();
            return priority1.compareTo(priority2);
        });

        for (int i = 0; i < urls.size(); i++) {
            String resURL = urls.get(i);
            if (urlMatcher.match(resURL, url)) {
                return routesMap.get(resURL).stream().map(ca -> (ConfigAttribute)ca).collect(Collectors.toList());
            }
        }

        return null ;
    }

    /**
     * @return all Config Attribute Setted in the database.
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        List<ConfigAttribute> attributes = new ArrayList<>();
        templateService.getRoutesMap().values().forEach(c -> attributes.addAll(c));
        return attributes;
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}