package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web;

import com.google.gson.Gson;
import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter.CorsParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer {
    private final Gson gson;
    private final CorsParameter corsParameter;

    @Autowired
    public MvcConfiguration(Gson gson, CorsParameter corsParameter) {
        this.gson = gson;
        this.corsParameter = corsParameter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new GsonHttpMessageConverter(gson));
        converters.add(new ResourceHttpMessageConverter(true));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for(var mapping : this.corsParameter.getMappings()) {
            registry.addMapping(mapping.getPattern())
                    .allowedOrigins(mapping.getOrigins().toArray(String[]::new))
                    .allowedMethods(mapping.getMethods().toArray(String[]::new))
                    .allowedHeaders(mapping.getHeaders().toArray(String[]::new));
        }
    }
}
