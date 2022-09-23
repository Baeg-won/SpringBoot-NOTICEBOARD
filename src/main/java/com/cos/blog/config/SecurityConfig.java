package com.cos.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cos.blog.config.auth.PrincipalDetailService;
import com.cos.blog.interceptor.NotificationInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration //빈등록 (IoC관리)
@EnableWebSecurity //security 필터 등록
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근하면 권한 및 인증을 미리 체크하겠다는 뜻
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
	
	private final PrincipalDetailService principalDetailService;
	private final AuthenticationFailureHandler userLoginFailHandler;
    private final NotificationInterceptor notificationInterceptor;
	
	@Bean
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(notificationInterceptor)
                .excludePathPatterns("/js/**", "/css/**", "/image/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		  .csrf().disable()
		  .rememberMe()
		  	.rememberMeParameter("remember")
		  	.tokenValiditySeconds(3600)
		  	.alwaysRemember(false)
		  	.userDetailsService(principalDetailService)
		  .and()
		  .authorizeRequests()  
		    .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/sort/**")
		    .permitAll()
		    .anyRequest()
		    .authenticated()
		  .and()
		  	.formLogin()
		  	.loginPage("/auth/loginForm")
		  	.loginProcessingUrl("/auth/loginProc")
		  	.failureHandler(userLoginFailHandler)
		  	.defaultSuccessUrl("/");
        return http.build();
    }
}
