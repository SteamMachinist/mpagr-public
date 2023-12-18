package steammachinist.mpagradmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAdminServer
@EnableScheduling
public class MpagrAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(MpagrAdminApplication.class, args);
	}
}
