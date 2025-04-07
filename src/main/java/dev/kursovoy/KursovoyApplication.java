package dev.kursovoy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KursovoyApplication {

	private static Initializer initiaotor;

	@Autowired
	public void setInitialLoader(Initializer initiaotor) {
		KursovoyApplication.initiaotor = initiaotor;
	}

	public static void main(String[] args) {
		SpringApplication.run(KursovoyApplication.class, args);
		initiaotor.initial();
	}

}
