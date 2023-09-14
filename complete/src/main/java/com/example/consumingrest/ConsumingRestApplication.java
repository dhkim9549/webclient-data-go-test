package com.example.consumingrest;

import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents; 
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.DefaultUriBuilderFactory;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class ConsumingRestApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ConsumingRestApplication.class);

    public static void main(String[] args) {
        log.info("main() start...");
        for(String arg : args) {
            log.info("arg = " + arg);
        }
        SpringApplication.run(ConsumingRestApplication.class, args);
        log.info("main() end...");
    }

    public void run(String... args) throws Exception {
        log.info("run() start...");
        for(String arg : args) {
            log.info(">>> arg = " + arg);
        }
        for(int i = 0; i <= 100000000; i++) {
            Thread.sleep(100);
            int randomNum = ThreadLocalRandom.current().nextInt(0, 10);
	    if(randomNum > 0) {
                continue;
            }
            long nano = System.currentTimeMillis();
	    restT();
            long nano2 = System.currentTimeMillis();
	    log.info("duration = " + (nano2 - nano));
        }
        // webClientT2();
        // webFlux();
    }

    public void restT() throws Exception {

        int randomNum = ThreadLocalRandom.current().nextInt(10, 10000);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(
            new URI("https://apis.data.go.kr/B551408/jnse-rcmd-info/jnse-rcmd-list?serviceKey=PW2VvwTvkcs%2FWMVLduXzeRL0BPjOYH%2B0wMnsQiyy5UgcrukEjAurATJUNkeA7T%2Bj47s3GAmLzHduip%2BfbxESlQ%3D%3D&dataType=JSON&pageNo=1&numOfRows=100&rentGrntAmt=" + randomNum + "&trgtLwdgCd=1111111111&age=111&weddStcd=1&myIncmAmt=0&myTotDebtAmt=0&ownHsCnt=0&grntPrmeActnDvcdCont="),
            String.class);

        log.info("response = " + response);
    }

    ArrayList addrLst = null;




	public void webFlux() {

		Flux.interval(Duration.ofSeconds(600))
                        .subscribe(num -> {
                                log.info("ansin num = " + num);
				checkSise("https://ansim.hf.go.kr");
                        });

		Flux.interval(Duration.ofSeconds(600))
                        .subscribe(num -> {
                                log.info("bada num = " + num);
                                checkSise("http://bada.ai");
                        });

	}

	WebClient client = null;

	public void checkSise(String baseUrl) {

		if(client == null) {
			client = WebClient.builder()
                        	.baseUrl(baseUrl)
                        	.build();
		}

		String urlBuilder = new String("/siseapi/sise/addr3");
                urlBuilder = urlBuilder.concat("?" + "lwdgCd" + "=" + "26290106");
                urlBuilder = urlBuilder.concat("&" + "ltnoBno" + "=" + "1858");
                urlBuilder = urlBuilder.concat("&" + "ltnoBuno" + "=" + "");

                log.info("urlBuilder = " + urlBuilder);

		client.get()
			.uri(urlBuilder)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(Iterable.class)
			.subscribe(addrArr -> {
				for(Object addr : addrArr) {
					log.info("addr = " + addr);
				}
		});
	}
}
