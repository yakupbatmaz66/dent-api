package yqkup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import yqkup.others.Variables;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println(Application.class.getSimpleName() + " başlatıldı.");
		Variables.GetVariables();
	}
}