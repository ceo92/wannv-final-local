package please_do_it.yumi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YumiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YumiApplication.class, args);
	}

}
