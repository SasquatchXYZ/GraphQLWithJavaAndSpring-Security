package playground.security_demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class Config {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.oauth2Login(oauth2 ->
                oauth2.authenticationSuccessHandler(
                        new RedirectServerAuthenticationSuccessHandler("/graphiql")
                )
        );

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated()
                )
                .build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    MapReactiveUserDetailsService userDetailsService() {
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        UserDetails luna = userBuilder
                .username("Luna").password("password").roles("USER")
                .build();
        UserDetails dalton = userBuilder
                .username("Dalton").password("password").roles("USER", "ADMIN")
                .build();
        return new MapReactiveUserDetailsService(luna, dalton);
    }
}
