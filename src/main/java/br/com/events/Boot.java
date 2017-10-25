package br.com.events;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Boot extends WebMvcConfigurerAdapter
{

   public static void main(String[] args)
   {
      SpringApplication.run(Boot.class, args);
   }

   @Bean
   public BCryptPasswordEncoder bCryptPasswordEncoder() {
       return new BCryptPasswordEncoder();
   }
   
   @Override
   public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("/**")
               .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
   }
   
   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
       final CorsConfiguration configuration = new CorsConfiguration();
       configuration.setAllowedOrigins(Arrays.asList("*"));
       configuration.setAllowedMethods(Arrays.asList("HEAD",
               "GET", "POST", "PUT", "DELETE", "PATCH"));
       configuration.setAllowCredentials(true);
       configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
       final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", configuration);
       return source;
   }
   
}
