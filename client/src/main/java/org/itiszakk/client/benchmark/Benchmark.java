package org.itiszakk.client.benchmark;

import org.itiszakk.shared.balance.BalanceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Benchmark {

    private final static int THREAD_COUNT = 4;
    private final static int READ_QUOTA = 1;
    private final static int WRITE_QUOTA = 2;
    private final static List<Long> READ_ID_LIST = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    private final static List<Long> WRITE_ID_LIST = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);

    private final WebClient.Builder webClientBuilder;

    @Value("${application.base-url}")
    private String BASE_URL;

    public Benchmark(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int iThread = 0; iThread < THREAD_COUNT; iThread++) {
            executor.execute(() -> {
                while (true) {
                    double probability = (double) READ_QUOTA / (double) (READ_QUOTA + WRITE_QUOTA);

                    if (ThreadLocalRandom.current().nextDouble() < probability) {
                        getBalance(randomChoice(READ_ID_LIST));
                    } else {
                        changeBalance(randomChoice(WRITE_ID_LIST), 1L);
                    }
                }
            });
        }

        executor.shutdown();
    }

    private void getBalance(Long id) {
        webClientBuilder.build()
                .get()
                .uri(BASE_URL + "/{id}", id)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    private void changeBalance(Long id, Long amount) {
        webClientBuilder.build()
                .patch()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BalanceDTO(id, amount))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private Long randomChoice(List<Long> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
