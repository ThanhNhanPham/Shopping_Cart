package com.ecom.Shopping_Cart.config;
import org.springframework.security.authentication.AuthenticationProvider;

import com.ecom.Shopping_Cart.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    @Lazy
    private  AuthFailureHandlerImpl authFailureHandler;

    private JwtAuthFilter jwtAuthFilter;// tạo mới
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
//    @Bean
//    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder pe) {
//        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
//        p.setUserDetailsService(userDetailsService);
//        p.setPasswordEncoder(pe);
//        return p;
//    }

    public SecurityConfig() {
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImlp();
    }
    //Lớp này được Spring Security sử dụng để xác thực thông tin đăng nhập của người dùng.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        //  CSRF : Cross-Site Request Forgery
//        //  CORS (Cross-Origin Resource Sharing)
//        http.csrf(csrf->csrf.disable()).cors(cors->cors.disable())
//                .authorizeHttpRequests(red->red
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/**").permitAll())
//                .formLogin(form->form.loginPage("/signin")
//                        .loginProcessingUrl("/login")
////                        .defaultSuccessUrl("/")
//                        .failureHandler(authFailureHandler)
//                        .successHandler(authenticationSuccessHandler))
//                .logout(logout->logout.permitAll());
//        return http.build();
//
//    }
@Bean
public SecurityFilterChain filterChain(HttpSecurity http,
                                       JwtAuthFilter jwtAuthFilter,
                                       AuthenticationProvider authenticationProvider) throws Exception {
    http
            .csrf(csrf -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/auth/**","/register","/saveRegister","/forgot-password","/reset-password","user/**"))
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/signin", "/error", "/register","/forgot-password","/auth/**",
                            "/css/**","/js/**","/img/**","/saveRegister","/reset-password","/product/**","products").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user/**").hasRole("USER")
                    .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage("/signin")
                    .loginProcessingUrl("/login")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authFailureHandler))    // tránh cần import AbstractHttpConfigurer
            .httpBasic(b -> b.disable())
            .logout(l -> l.disable())
            .authenticationProvider(authenticationProvider)              // <- bật lại
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((req,res,e)-> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .accessDeniedHandler((req,res,e)-> res.sendError(HttpServletResponse.SC_FORBIDDEN)));
    return http.build();
}
}
