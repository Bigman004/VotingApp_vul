package com.vulnerable.voting.security;

import com.vulnerable.voting.model.UserEntity;
import lombok.*;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthorizationDetails {
    private UserEntity user;
}
