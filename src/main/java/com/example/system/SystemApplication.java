package com.example.system;


import com.example.system.action.util.Result;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.web.DefaultSecurityFilterChain;

import java.util.Map;

@SpringBootApplication
@MapperScan
public class SystemApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext  context=SpringApplication.run(SystemApplication.class, args);
		context.getBean(DefaultSecurityFilterChain.class);

	}

}
