package in._10h.java.swaggerspringboot;

import in._10h.java.swaggerspringboot.client.api.DefaultApi;
import in._10h.java.swaggerspringboot.client.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SwaggerspringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwaggerspringbootApplication.class, args);
	}

	@Value("${backend.baseUrl}")
	private String backendBaseUrl;

	@Bean
	public ApiClient apiClient() {
		return new ApiClient().setBasePath(this.backendBaseUrl);
	}

	@Bean
	public DefaultApi defaultApi(
			@Autowired
			final ApiClient apiClient
	) {
		return new DefaultApi(apiClient);
	}

}
