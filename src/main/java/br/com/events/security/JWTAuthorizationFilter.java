package br.com.events.security;

import io.jsonwebtoken.Jwts;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.events.service.UserService;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	@Autowired
	private UserService userService;
	
    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        try{
	        if (token != null) {
	            String user = Jwts.parser()
	                    .setSigningKey(SecurityConstants.SECRET.getBytes())
	                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
	                    .getBody()
	                    .getSubject();	
	            if (user != null) {
	                if(userService==null){
	                    ServletContext servletContext = request.getServletContext();
	                    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	                    userService = webApplicationContext.getBean(UserService.class);
	                }
	            	UserDetails userDetails = userService.loadUserByUsername(user);
	                return new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
	            }
	            return null;
	        }
        }catch(Exception e){
        	//log error ?lançar exceção ?
        	return null;
        }
        return null;
    }
}
