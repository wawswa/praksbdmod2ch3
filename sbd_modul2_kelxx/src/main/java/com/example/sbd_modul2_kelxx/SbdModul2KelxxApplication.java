package com.example.sbd_modul2_kelxx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.*")
public class SbdModul2KelxxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbdModul2KelxxApplication.class, args);
	}

}
