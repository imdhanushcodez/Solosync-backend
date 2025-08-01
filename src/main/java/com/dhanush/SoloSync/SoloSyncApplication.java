package com.dhanush.SoloSync;

import com.dhanush.SoloSync.Model.ProfileEntity;
import com.dhanush.SoloSync.Repository.ProfileEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@SpringBootApplication
public class SoloSyncApplication {
	public static void main(String[] args) {
		SpringApplication.run(SoloSyncApplication.class, args);
		//checking with aws
		//spring.datasource.url=jdbc:mysql://localhost:3306/solosync
		//spring.datasource.username=root
		//spring.datasource.password=dhanush28
	}

}
