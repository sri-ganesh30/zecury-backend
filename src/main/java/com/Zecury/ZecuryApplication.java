package com.Zecury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZecuryApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ZecuryApplication.class);
//		application.setRegisterShutdownHook(false);
		application.run(args);
	}
	

}
