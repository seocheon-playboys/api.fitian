package com.seocheon.fitian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.seocheon.fitian.mapper")
public class FitianApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitianApplication.class, args);
	}
}
