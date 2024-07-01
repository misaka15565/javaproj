package cn.sudoer.javaproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackages = "cn.sudoer.javaproj.repository")
@EntityScan(basePackages = "cn.sudoer.javaproj.entity")
public class JavaprojApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaprojApplication.class, args);
	}
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
	@GetMapping("/gettime")
	public String getTime() {
		String time=new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		return String.format("Current time:%s", time);
	}
	
}
