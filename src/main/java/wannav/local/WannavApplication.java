package wannav.local;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WannavApplication {

	public static void main(String[] args) {
		SpringApplication.run(WannavApplication.class, args);
	}

}
