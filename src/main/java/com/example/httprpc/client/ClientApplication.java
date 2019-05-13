package com.example.httprpc.client;

import com.example.httprpc.GreetingRequest;
import com.example.httprpc.GreetingResponse;
import com.example.httprpc.GreetingsService;
import com.example.httprpc.service.ServiceApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.stereotype.Component;

@Profile("client")
@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@Bean
	GreetingsService greetingsService() {
		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean() {
			{
				setServiceInterface(GreetingsService.class);
				setServiceUrl("http://localhost:8080/remoting/" + ServiceApplication.GREETINGS_SERVICE_NAME);
			}
		};
		fb.afterPropertiesSet();
		return (GreetingsService) fb.getObject();
	}


}

@Log4j2
@Component
@RequiredArgsConstructor
class Runner {

	private final GreetingsService client;

	@EventListener(ApplicationReadyEvent.class)
	public void go() {
		GreetingResponse tammie = client.greet(new GreetingRequest("Tammie"));
		log.info("geeting a response: " + tammie.getGreeting());
	}
}
