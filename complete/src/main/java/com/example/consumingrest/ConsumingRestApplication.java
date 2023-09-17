package com.example.consumingrest;

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
import java.net.URI;
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
	/*
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
	*/
        webFlux();
    }

    String urlStr = "https://apis.data.go.kr/B551408/jnse-rcmd-info/jnse-rcmd-list?serviceKey=PW2VvwTvkcs%2FWMVLduXzeRL0BPjOYH%2B0wMnsQiyy5UgcrukEjAurATJUNkeA7T%2Bj47s3GAmLzHduip%2BfbxESlQ%3D%3D&dataType=JSON&pageNo=1&numOfRows=100&rentGrntAmt=AAA&trgtLwdgCd=1111111111&age=111&weddStcd=1&myIncmAmt=0&myTotDebtAmt=0&ownHsCnt=0&grntPrmeActnDvcdCont=";

    public void restT() throws Exception {

        int randomNum = ThreadLocalRandom.current().nextInt(10, 10000);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(
            new URI(urlStr.replaceAll("AAA", "" + randomNum)),
            String.class
            );

        log.info("response = " + response);
    }

    public void webFlux() {

        Flux.interval(Duration.ofMillis(4000))
            .subscribe(num -> {
                log.info("ansin num = " + num);
                checkSise();
            });

    }

    WebClient client = null;

    public void checkSise() {

        if(client == null) {
            client = WebClient.builder()
               .build();
        }

        int randomNum = ThreadLocalRandom.current().nextInt(10, 10000);

	URI uri = null;

	try {
            uri = new URI(urlStr.replaceAll("AAA", "" + randomNum));
        } catch(Exception e) {
        }

        long nano = System.currentTimeMillis();

        client.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(rspsStr -> {
                log.info("rspsStr= " + rspsStr);
                long nano2 = System.currentTimeMillis();
                log.info("duration = " + (nano2 - nano));
            });
    }
}
