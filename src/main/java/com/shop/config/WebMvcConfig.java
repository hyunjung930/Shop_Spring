package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer { //업로드한 파일을 읽어올 경로 설정

    @Value("${uploadPath}")     //application.propertise에 설정한 "uploadPath" 값을 읽어옴.
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")    //웹 브라우저에 입력하는 url/images로 시작하는 경우 UploadPath에 설정한 폴더를 기준으로 파일을 읽어옴
                .addResourceLocations(uploadPath);  //로컬 컴퓨터에 저장된 파일을 읽어올 root 경로를 설정
    }
}
