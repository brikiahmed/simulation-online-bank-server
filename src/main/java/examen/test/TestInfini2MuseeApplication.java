package examen.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class TestInfini2MuseeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestInfini2MuseeApplication.class, args);
	}

}
