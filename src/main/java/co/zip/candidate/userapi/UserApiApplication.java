package co.zip.candidate.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableAspectJAutoProxy
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
