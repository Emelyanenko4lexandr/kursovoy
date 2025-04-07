package dev.kursovoy.config;


import dev.kursovoy.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/registration/**").permitAll()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/api/base/catalog/**").permitAll()

                .antMatchers("/api/base/check/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPPORT.name())
                .antMatchers("/api/base/allrents/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPPORT.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response,
                                         AuthenticationException authException) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    }
                })
                .and()
                .csrf().disable()
                .cors().disable();

        return http.build();

    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery(
                        "select c.username, c.password, u.enable "
                        + "from users u "
                        + "join credentials c on u.credentials_id = c.id "
                        + "where c.username = ?")
                .authoritiesByUsernameQuery("select c.username, u.role from users u "
                        + "join credentials c on u.credentials_id = c.id "
                        + "where c.username = ?");
    }

}
