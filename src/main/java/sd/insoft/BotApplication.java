package sd.insoft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class BotApplication {
    public static void main(String[] args) {

        Logger log = LoggerFactory.getLogger(BotApplication.class);
        log.debug("BotApplication started with argument={}", args);

        ApiContextInitializer.init();
        SpringApplication.run(BotApplication.class, args);
    }
}
