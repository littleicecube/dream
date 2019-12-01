package com.palace.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DreamMainApp {
	public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(DreamMainApp.class)
        	.run(args);
    }
}
