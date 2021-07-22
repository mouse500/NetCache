package com.sypark.simple;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

//import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

//@Slf4j
@SpringBootApplication
public class NetCacheApplication {
	public static void main(String[] args) {
		SpringApplication.run(NetCacheApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route()
				.POST("/**", this::handlerPost)
				.GET("/**", this::handlerGet)
				.build();
	}
	ConcurrentHashMap<String,String> cache = new ConcurrentHashMap<>();
	private Mono<ServerResponse> handlerPost(ServerRequest request) {
		return request.bodyToMono(String.class).flatMap(val -> {
			String key = request.uri().toASCIIString();
			cache.put(key,val);
			return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("OK");
		});
	}
	private Mono<ServerResponse> handlerGet(ServerRequest request) {
		String key = request.uri().toASCIIString();
		if(cache.containsKey(key)) {
			String val = cache.get(key);
			return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue(val);
		}
		else {
			return ServerResponse.notFound().build();
		}
	}


}
