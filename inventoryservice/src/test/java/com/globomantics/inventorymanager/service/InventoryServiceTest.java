package com.globomantics.inventorymanager.service;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.globomantics.inventorymanager.model.InventoryRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class InventoryServiceTest {

    @Autowired
    private InventoryService service;

    private WireMockServer wireMockServer;

    @BeforeEach
    void beforeEach() {
        // Start the WireMock Server
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();

        // Configure our requests
        wireMockServer.stubFor(get(urlEqualTo("/inventory/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("json/inventory-response.json")));
        wireMockServer.stubFor(get(urlEqualTo("/inventory/2"))
                .willReturn(aResponse().withStatus(404)));
        wireMockServer.stubFor(post("/inventory/1/purchaseRecord")
                // Actual Header sent by the RestTemplate is: application/json;charset=UTF-8
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(containing("\"productId\":1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("json/inventory-response-after-post.json")));
    }

    @AfterEach
    void afterEach() {
        wireMockServer.stop();
    }

    @Test
    void testGetInventoryRecordSuccess() {
        Optional<InventoryRecord> record = service.getInventoryRecord(1);
        Assertions.assertTrue(record.isPresent(), "InventoryRecord should be present");

        // Validate the contents of the response
        Assertions.assertEquals(500, record.get().getQuantity().intValue(),
                "The quantity should be 500");
    }

    @Test
    void testGetInventoryRecordNotFound() {
        Optional<InventoryRecord> record = service.getInventoryRecord(2);
        Assertions.assertFalse(record.isPresent(), "InventoryRecord should not be present");
    }

    @Test
    void testPurchaseProductSuccess() {
        Optional<InventoryRecord> record = service.purchaseProduct(1, 5);
        Assertions.assertTrue(record.isPresent(), "InventoryRecord should be present");

        // Validate the contents of the response
        Assertions.assertEquals(495, record.get().getQuantity().intValue(),
                "The quantity should be 495");
    }
}
