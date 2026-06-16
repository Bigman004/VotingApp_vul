package com.vulnerable.voting.security;
import com.vulnerable.voting.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@AllArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private JwtUtil jwtUtil;
    private AuthorizationDetails authorizationDetails;
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AuthorizationInterceptor::preHandle");
        log.info("endpoint: {}", request.getRequestURI());

            String token = getCookieValue("token", request.getCookies());
            if(token != null) {
                if(jwtUtil.validateToken(token)){
                    String username = jwtUtil.getUsernameFromToken(token);
                    authorizationDetails.setUser(userRepository.findByEmailAddress(username));
                    log.info("AuthorizationInterceptor::preHandle: username = {} token-validate{}", username, jwtUtil.validateToken(token));
                    return true;
                }
                else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
            }
            else {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED, "cookie does not contain authorization data"

                );
                return false;
            }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {



    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        log.info("AuthorizationInterceptor::afterCompletion");
    }
    private String getCookieValue(String name, Cookie[] cookies) {
        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        catch (Exception e) {
            return null;
        }
        return null;
    }
}
