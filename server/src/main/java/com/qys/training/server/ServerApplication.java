package com.qys.training.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.qys.training")
@MapperScan("com.qys.training.biz.*.mapper")
@EnableScheduling
public class ServerApplication {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(ServerApplication.class).web(WebApplicationType.SERVLET).build().run(args);
	}

}
