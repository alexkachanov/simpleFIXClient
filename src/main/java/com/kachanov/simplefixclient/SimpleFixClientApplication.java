package com.kachanov.simplefixclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleFixClientApplication implements CommandLineRunner {

	public static void main( String[] args ) {
		SpringApplication.run( SimpleFixClientApplication.class, args );
	}

	@Override
	public void run( String... args ) throws Exception {
		SimpleFixClient.main( args );
	}

}
