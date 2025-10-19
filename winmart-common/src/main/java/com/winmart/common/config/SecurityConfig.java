package com.winmart.common.config;

import com.winmart.common.config.bean.Public;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  public SecurityConfig(CustomAccessDeniedHandler customAccessDeniedHandler) {
    this.customAccessDeniedHandler = customAccessDeniedHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping requestMappingHandlerMapping)
      throws Exception {
    String[] publicEndpoints = getPublicEndpoints(requestMappingHandlerMapping);

    http.csrf(AbstractHttpConfigurer::disable) // 🔑 Tắt CSRF cho REST API
        .cors(cors -> cors.and()) // 🔑 Enable CORS
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/public/**")
                    .permitAll()
                    .requestMatchers(publicEndpoints)
                    .permitAll()
                    .anyRequest()
                    .permitAll()) // Tạm thời mở hết để test
        .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler));

    return http.build();
  }

  private String[] getPublicEndpoints(RequestMappingHandlerMapping requestMappingHandlerMapping) {
    Set<String> publicUrls = new HashSet<>();

    // get all public endpoints
    Map<RequestMappingInfo, HandlerMethod> handlerMethods =
        requestMappingHandlerMapping.getHandlerMethods();

    handlerMethods.forEach(
        (info, handlerMethod) -> {
          // check if method or class has @Public annotation
          boolean isPublic =
              handlerMethod.hasMethodAnnotation(Public.class)
                  || handlerMethod.getBeanType().isAnnotationPresent(Public.class);

          if (isPublic) {
            // if public, add to set
            if (info.getPathPatternsCondition() != null) {
              publicUrls.addAll(info.getPathPatternsCondition().getPatternValues());
            }
          }
        });

    return publicUrls.toArray(new String[0]);
  }
}
