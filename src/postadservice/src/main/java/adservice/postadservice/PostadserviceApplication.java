package adservice.postadservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SpringBootApplication
public class PostadserviceApplication {
	private static final Logger LOG = LoggerFactory.getLogger(PostadserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PostadserviceApplication.class, args);
	}

	@Bean
	public Consumer<String> readAdData() {
		return (data) -> {
			LOG.info("Read ad data: {}", data);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
				LOG.error("ERROR!!!", e);
            }
        };
	}
}
