package lk.ijse.agrosmart_systembackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgroSmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgroSmartApplication.class, args);
    }

}
