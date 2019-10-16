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
class InventoryServiceMappingTest {

    @Autowired
    private InventoryService service;

    private WireMockServer wireMockServer;

    @BeforeEach
    void beforeEach() {
        // Start the WireMock Server
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();
    }

    @AfterEach
    void afterEach() {
        wireMockServer.stop();
    }

    @Test
    void testGetInventorRecordSuccess() {
        Optional<InventoryRecord> record = service.getInventoryRecord(1);
        Assertions.assertTrue(record.isPresent(), "InventoryRecord should be present");

        // Validate the contents of the response
        Assertions.assertEquals(500, record.get().getQuantity().intValue(), "The quantity should be 500");
    }

    @Test
    void testGetInventorRecordNotFound() {
        Optional<InventoryRecord> record = service.getInventoryRecord(2);
        Assertions.assertFalse(record.isPresent(), "InventoryRecord should not be present");
        System.out.println(record);
    }

    @Test
    void testPurchaseProductSuccess() {
        Optional<InventoryRecord> record = service.purchaseProduct(1, 5);
        Assertions.assertTrue(record.isPresent(), "InventoryRecord should be present");

        // Validate the contents of the response
        Assertions.assertEquals(495, record.get().getQuantity().intValue(), "The quantity should be 495");
    }
}
