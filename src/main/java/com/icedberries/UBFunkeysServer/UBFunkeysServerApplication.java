package com.icedberries.UBFunkeysServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.icedberries", "javagrinko.spring"})
public class UBFunkeysServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UBFunkeysServerApplication.class, args);
	}

}
