package co.zip.candidate.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class UserApiApplication {

	private static final String TIME_ZONE = "UTC";
	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE));
	}
}
