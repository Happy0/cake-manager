package com.waracle.cakemgr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.entities.CakeEntity;
import com.waracle.cakemgr.repositories.CakeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Migrates the cakes from the configured migration URL to the database on
 * system startup
 */
@Component
public class CakeMigrator implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${waracle.cake.manager.migrate.url}")
    private String cakeJsonUrl;

    @Autowired
    CakeRepository cakeRepository;

    static final Logger logger = LoggerFactory.getLogger(CakeMigrator.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Migrating cakes");
        try {
            migrateCakes();
        } catch (InterruptedException e) {
            logger.error("Error while migrating cakes: " + e.getMessage());
        }
    }

    private void migrateCakes() throws InterruptedException {
        Mono<CakeEntity> empty = Mono.empty();

        // If the database is already populated, don't run the migration.
        Flux<CakeEntity> cakeSource =  cakeRepository.count().flux().flatMap(
                count -> {
                    return (count > 0) ? empty.flux() : cakesFromUrl();
                }
        );

        Mono<Long> cakesLoaded = cakeRepository.saveAll(cakeSource).count();

        // Block until all the cakes have been loaded
        cakesLoaded
                .map(count -> {
                    logger.info(count + " cakes migrated from ." + cakeJsonUrl);
                    return count;
                })
                .doOnError(error -> logger.error("Error while migrating cakes " + error.getMessage()))
                .block();

    }

    public Flux<CakeEntity> cakesFromUrl() {

        return WebClient
                .builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> {
                    // The given URL returns a media type of text/plain which makes "bodyToFlux"
                    // throw an error without the following override as it expects application/json
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    configurer.customCodecs().decoder(new Jackson2JsonDecoder(mapper, MimeTypeUtils.parseMimeType(MediaType.TEXT_PLAIN_VALUE)));
                }).build())
                .build()
                .method(HttpMethod.GET)
                .uri(cakeJsonUrl)
                .retrieve()
                .bodyToFlux(CakeEntity.class);
    }

}
