package adservice.postadservice;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.transport.DefaultTransportOptions;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
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
	public DataSource dataSource() {
		var ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:h2:mem:test");
		ds.setDriverClassName(Driver.class.getName());
		ds.setUsername("sa");
		return ds;
	}

	@Bean
	public Consumer<String> readAdData(
			ElasticsearchClient es,
			JdbcTemplate jt
	) {
		jt.execute("CREATE TABLE addata(data VARCHAR)");
		return (data) -> {
            try {
				jt.update("INSERT INTO addata VALUES ( ? )", data);
				LOG.info("Inserted the AD data {} to DB", data);
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
				LOG.info("Index the AD data {} to ES", data);
			} catch (Exception e) {
				LOG.error("ERROR!!!", e);
            }
        };
	}
}
