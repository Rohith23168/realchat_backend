package com.substring.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // absolute and safe upload path
        String uploadPath = new File("uploads").getAbsolutePath();

        // IMPORTANT: ensure trailing slash
        if (!uploadPath.endsWith(File.separator)) {
            uploadPath = uploadPath + File.separator;
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}