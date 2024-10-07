package com.NNTDATA.TrasacctionB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.config.EnableWebFlux;

@Lazy
@ComponentScan(lazyInit = true)
@EnableWebFlux
@SpringBootApplication
public class TrasacctionBApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrasacctionBApplication.class, args);
	}

}
