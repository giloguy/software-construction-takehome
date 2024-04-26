package com.giloguy.examcs;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;


@SpringBootApplication
@EntityScan(basePackageClasses = {
	ExamcsApplication.class,
		Jsr310Converters.class
})
public class ExamcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamcsApplication.class, args);
	}

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
