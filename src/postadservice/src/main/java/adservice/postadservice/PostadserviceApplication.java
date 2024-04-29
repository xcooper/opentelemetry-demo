package adservice.postadservice;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.transport.DefaultTransportOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@SpringBootApplication
public class PostadserviceApplication {
	private static final Logger LOG = LoggerFactory.getLogger(PostadserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PostadserviceApplication.class, args);
	}

	@Bean
	public Consumer<String> readAdData(
			ElasticsearchClient es
	) {
		return (data) -> {
			LOG.info("Read ad data: {}", data);
            try {
				UpdateRequest<Map, Map> req = new UpdateRequest.Builder<Map, Map>()
						.index("postadservice")
						.id(UUID.randomUUID().toString())
						.docAsUpsert(true)
						.doc(Map.of(
								"ad", data
						))
						.build();
				es.withTransportOptions(
								new DefaultTransportOptions.Builder()
										.setHeader("Content-Type", "application/json")
										.build()
						)
						.update(req, Map.class);
			} catch (Exception e) {
				LOG.error("ERROR!!!", e);
            }
        };
	}
}
