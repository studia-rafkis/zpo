package com.shop.backend.config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthEntryPoint userAuthenticationEntryPoint;
    private final UserAuthProvider userAuthenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                        .requestMatchers("/products/image/**").permitAll()
                        .requestMatchers("/products/all").permitAll()
                        .requestMatchers("/products/id/**").permitAll()
                        .requestMatchers("/products/**").hasRole("ADMIN")
                        .requestMatchers("/orders/all").permitAll()
                        .requestMatchers("/user/{email}").hasRole("USER")
                        .requestMatchers("/orders/email/{email}").hasRole("USER")
                        .requestMatchers("/createContact").permitAll()
                        .requestMatchers("/createNewsletter").permitAll()
                        .requestMatchers("/settings/all").permitAll()
                        .requestMatchers("/createNewSetting").hasRole("ADMIN")
                        .requestMatchers("/orders/new").permitAll()
                        .requestMatchers("/promocode/all").permitAll()
                        .requestMatchers("/likes/all").permitAll()
                        .requestMatchers("settings/updateColor/{id}").hasRole("ADMIN")
                        .requestMatchers("settings/updateSize/{id}").hasRole("ADMIN")
                        .requestMatchers("/newLike").permitAll()
                        .requestMatchers("/promocode/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/verify").permitAll()
                        .requestMatchers("likes/update/{id}").hasRole("USER")
                        .requestMatchers("comments/status/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/comments/all").permitAll()
                        .requestMatchers("/createComment").permitAll()
                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers("/createOrder").permitAll()
                        .requestMatchers("/payu-callback/**").permitAll()
                        .requestMatchers("/processing").permitAll()
                        .requestMatchers("/orders/findByOrderId").permitAll()
                        .requestMatchers("/orders/getIdp/{id}").permitAll()
                        .requestMatchers("/update/{orderId}").hasRole("ADMIN")
                        .requestMatchers("/status/{id}").hasRole("ADMIN")
                        .requestMatchers("orders/updateStatus/{orderId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}

