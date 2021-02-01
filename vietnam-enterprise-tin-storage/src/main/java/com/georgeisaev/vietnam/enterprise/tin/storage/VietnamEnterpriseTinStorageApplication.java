package com.georgeisaev.vietnam.enterprise.tin.storage;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@Log
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, ErrorMvcAutoConfiguration.class})
public class VietnamEnterpriseTinStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(VietnamEnterpriseTinStorageApplication.class, args);
	}

}
