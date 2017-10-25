package br.com.events.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import br.com.events.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	
	private UserService userService;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public SecurityConfig(UserService userService,BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/cardImage/*")
        .and().ignoring().antMatchers("/model/*")
        .and().ignoring().antMatchers("/card/*")
        .and().ignoring().antMatchers("/card")
        .and().ignoring().antMatchers("/sign-in");
    }
	
    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {	
    	http.authorizeRequests().anyRequest().authenticated();
    	http.httpBasic().disable();
    	http.csrf().disable();
    	http.formLogin().usernameParameter("username");
    	http.formLogin().passwordParameter("password");
    	http.formLogin().loginProcessingUrl("/login");
    	http.addFilter(new JWTAuthenticationFilter(authenticationManager())).addFilter(new JWTAuthorizationFilter(authenticationManager()));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }
}
