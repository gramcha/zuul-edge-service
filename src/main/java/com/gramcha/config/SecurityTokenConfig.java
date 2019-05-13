package com.gramcha.config;

import com.gramcha.services.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gramachandran on 29/10/18.
 */
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class SecurityTokenConfig  extends WebSecurityConfigurerAdapter {
//    @PostConstruct
//    public void init(){
//        System.out.println("------SecurityTokenConfig-----");
//    }
    @Autowired
    private JwtConfig jwtConfig;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("-----configure()----");
        System.out.println(http.toString());

        http.headers().xssProtection().disable();

        http
                .csrf().disable()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> {
                    System.out.println("Unauth URI = "+req.getRequestURI());
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    })
                .and()
                // Add a filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                // authorization requests config
                .authorizeRequests()
                // allow all who are accessing "auth" service
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                .antMatchers(HttpMethod.GET, "/api/ui/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/ui/**").permitAll()
                // must be an admin if trying to access admin area (authentication is also required here)
                //restricting the antonyms service to admin - to just experimental purpose.
                .antMatchers("/api/wqs/antonyms/**").hasRole("ADMIN")
                // Any other request must be authenticated
                .anyRequest().authenticated();
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }
}
