package com.example.demo;

import java.util.Collections;

import org.apache.http.HttpStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.google.common.net.HttpHeaders;

@SpringBootApplication
@RestController
public class GatewayAppApplication {
	
		
	  
	@Bean
	public CorsWebFilter corsFilter() {
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
	    config.setAllowedMethods(Collections.singletonList("*"));
	    config.setAllowedHeaders(Collections.singletonList("*"));

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);

	    return new CorsWebFilter(source);
	}

	@Bean
	RouteLocator routes(RouteLocatorBuilder builder)
	{
		return builder.routes()
				.route(r->r.path("/salat/**")
						.filters(f->f.addRequestHeader("X-RapidAPI-Key", "6de2dc4252msh294b45a1790ce73p139223jsn9281006406f9")
								.addRequestHeader("X-RapidAPI-Host", "muslimsalat.p.rapidapi.com")
								.rewritePath("/salat/(?<segment>.*)", "/${segment}")
								.addResponseHeader("Access-Control-Allow-Origin", "http://localhost:4200"))
						.uri("https://muslimsalat.p.rapidapi.com")).build();
	}
	@Bean
	DiscoveryClientRouteDefinitionLocator dynamicRoute(DiscoveryLocatorProperties dlp, ReactiveDiscoveryClient rdc)
	{
		return new DiscoveryClientRouteDefinitionLocator(rdc,dlp);
	}
	public static void main(String[] args) {
		SpringApplication.run(GatewayAppApplication.class, args);
	}

}
