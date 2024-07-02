package cn.sudoer.javaproj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "cn.sudoer.javaproj.repository")
@EntityScan(basePackages = "cn.sudoer.javaproj.entity")
@ServletComponentScan
public class JavaprojApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaprojApplication.class, args);
	}

	
}
