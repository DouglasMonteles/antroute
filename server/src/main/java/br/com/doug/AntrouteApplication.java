package br.com.doug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AntrouteApplication {

//	public static void main(String[] args) {
//		initServer(new String[]{});
//	}

	public static void initServer(String[] args) {
		SpringApplication.run(AntrouteApplication.class, args);
	}

}
