package com.company.bookstore.configs.security;


import com.company.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    //    https://www.codejava.net/frameworks/spring-boot/spring-boot-security-role-based-authorization-tutorial
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/name", "/book", "/books", "/static/**", "/webjars/**", "/files/**","/user-panel/registration").permitAll()
                .antMatchers("/adminpanel/**", "/book-panel/**").hasAnyAuthority("ADMIN", "SUPER_ADMIN")
                .antMatchers("/user-panel/**").hasAnyAuthority("SUPER_ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .logout().permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService)
//                .passwordEncoder(NoOpPasswordEncoder.getInstance());

        auth.userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder(8));
    }

}