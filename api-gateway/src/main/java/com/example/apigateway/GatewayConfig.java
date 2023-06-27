package com.example.apigateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Configuration
public class GatewayConfig {

    @Bean
    public GlobalFilter customGlobalFilter() {

        List<String> strings = Arrays.asList(
                "/matching-page",
                "/my-profile",
                "/single-announcement",
                "/announcements",
                "/create-announcement"
        );

        return (exchange, chain) -> {

            if (!exchange.getRequest().getPath().toString().contains("/api") && !strings.contains(exchange.getRequest().getPath().toString())) {

                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                }));
            }

            return exchange.getPrincipal()
                    .publishOn(Schedulers.boundedElastic())
                    .map(principal -> {

                                String emailId = "";
                                String session = "";
                                String userName = "";

                                if (principal instanceof OAuth2AuthenticationToken) {

                                    userName = principal.getName();

                                    SecurityContextImpl context = requireNonNull(exchange.getSession().block()).getAttribute("SPRING_SECURITY_CONTEXT");
                                    assert context != null;
                                    DefaultOidcUser authPrincipal = (DefaultOidcUser) context.getAuthentication().getPrincipal();
                                    emailId = authPrincipal.getEmail();

                                    session = new HttpCookie("SESSION", requireNonNull(exchange.getSession().block()).getId()).toString();
                                }

                                exchange.getRequest().mutate().header("X-Auth-Username", userName)
                                        .header("X-Auth-Email", emailId).header("cookie", session)
                                        .build();
                                return exchange;
                            }
                    )
                    .flatMap(chain::filter)
                    .then(Mono.fromRunnable(() -> {
                    }));
        };
    }
}