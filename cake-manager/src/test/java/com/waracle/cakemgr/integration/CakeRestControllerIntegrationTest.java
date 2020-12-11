package com.waracle.cakemgr.integration;

import com.waracle.cakemgr.entities.CakeEntity;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CakeRestControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    /**
     * Tests cakes are migrated into database from configured web link when application starts
     */
    @Test
    @Order(1)
    void testCakesMigrated() {
        webTestClient.get().uri("/cakes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CakeEntity.class)
                .value(cakeEntities -> cakeEntities.size(), equalTo(20));
    }

    /**
     * Test a paginated result can be returned successfully
     */
    @Test
    @Order(2)
    void testCakePagingSuccess() {
        webTestClient.get().uri(uriBuilder ->
                uriBuilder.path("/cakes")
                        .queryParam("page", 2)
                        .queryParam("size", 3)
                        .build()
        ).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CakeEntity.class)
                .value(cakeEntities -> cakeEntities.size(), equalTo(3))
                .value(cakeEntities -> cakeEntities.get(0).getTitle(), equalTo("Banana cake"))
                .value(cakeEntities -> cakeEntities.get(1).getTitle(), equalTo("Birthday cake"))
                .value(cakeEntities -> cakeEntities.get(2).getTitle(), equalTo("Lemon cheesecake"));
    }

    /**
     * Test that when a 'page' parameter is supplied but a 'size' parameter is not, a 400
     * bad request status code is returned
     */
    @Test
    @Order(3)
    void testPagingNoSizeParameter() {
        webTestClient.get().uri(uriBuilder ->
                uriBuilder.path("/cakes")
                        .queryParam("page", 2)
                        .build()
        ).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .value(result -> result.get("message"), equalTo("Invalid page query parameters. Page cannot be less than 1, and limit must not be null if page is defined."));
    }

    /**
     * Test that when a page value of '0' is supplied, a bad request response is returned
     */
    @Test
    @Order(4)
    void testPagingInvalidRange() {
        webTestClient.get().uri(uriBuilder ->
                uriBuilder.path("/cakes")
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .build()
        ).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .value(result -> result.get("message"), equalTo("Invalid page query parameters. Page cannot be less than 1, and limit must not be null if page is defined."));
    }

    /**
     * Test that a new cake can be added to the system
     */
    @Test
    @Order(5)
    void testAddingCake() {

        CakeEntity cakeEntity = new CakeEntity();

        cakeEntity.setTitle("German chocolate cake");
        cakeEntity.setDescription("A very chocolatey cake.");
        cakeEntity.setImage("https://www.getrandomthings.com/image/list-tasty-cakes/ecac9acb.jpg");

        // Test the cake can be added
        webTestClient
                .post()
                .uri("/cakes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(cakeEntity)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CakeEntity.class)
                .value(result -> result.getTitle(), equalTo("German chocolate cake"));

        // Test the cake can be retrieved and that there are now 21 cakes in the system
        webTestClient.get().uri(uriBuilder ->
                uriBuilder.path("/cakes")
                        .build()
        ).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CakeEntity.class)
                .value(cakeEntities -> cakeEntities.size(), equalTo(21))
                .value(cakeEntities ->
                        cakeEntities.get(cakeEntities.size() - 1).getTitle(),
                        equalTo("German chocolate cake"));

    }


}
