package com.example.httprpc.service;

import com.example.httprpc.GreetingRequest;
import com.example.httprpc.GreetingResponse;
import com.example.httprpc.GreetingsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

@Profile("service")
@SpringBootApplication
public class ServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	public static final String GREETINGS_EXPORTER_NAME = "greetingsExporter";
	public static final String GREETINGS_SERVICE_NAME = "greetingsService";

	@Bean(GREETINGS_EXPORTER_NAME)
	HttpInvokerServiceExporter greetingsExporter(GreetingsService gs) {
		return new HttpInvokerServiceExporter() {
			{
				setService(gs);
				setServiceInterface(GreetingsService.class);
			}
		};
	}

	@Bean
	ServletRegistrationBean<HttpRequestHandlerServlet> greetingsExporterServlet() {
		return new ServletRegistrationBean<HttpRequestHandlerServlet>(
			new HttpRequestHandlerServlet(), "/remoting/" + ServiceApplication.GREETINGS_SERVICE_NAME) {
			{
				setName(GREETINGS_EXPORTER_NAME);
			}
		};
	}
}

@Service(ServiceApplication.GREETINGS_SERVICE_NAME)
class SimpleGreetingsService implements GreetingsService {

	@Override
	public GreetingResponse greet(GreetingRequest request) {
		return new GreetingResponse(
			"Hello " + request.getName() + "!"
		);
	}
}

