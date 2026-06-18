package com.vulnerable.voting.security;

import com.vulnerable.voting.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtUtil jwtUtil;
    private final AuthorizationDetails authorizationDetails;
    private final UserRepository userRepository;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new AuthorizationInterceptor(jwtUtil, authorizationDetails,  userRepository))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/save_user",
                        "/auth/ping"
                );
    }
}
