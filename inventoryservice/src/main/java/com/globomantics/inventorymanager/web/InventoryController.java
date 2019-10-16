package com.globomantics.inventorymanager.web;

import com.globomantics.inventorymanager.model.PurchaseRecord;
import com.globomantics.inventorymanager.service.InventoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class InventoryController {

    private static final Logger logger = LogManager.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<?> getInventoryRecord(@PathVariable Integer id) {

        return inventoryService.getInventoryRecord(id)
                .map(inventoryRecord -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/inventory/" + inventoryRecord.getProductId()))
                                .body(inventoryRecord);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/inventory/purchase-record")
    public ResponseEntity<?> addPurchaseRecord(@RequestBody PurchaseRecord purchaseRecord) {
        logger.info("Creating new purchase record: {}", purchaseRecord);

        return inventoryService.purchaseProduct(purchaseRecord.getProductId(), purchaseRecord.getQuantityPurchased())
                .map(inventoryRecord -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/inventory/" + inventoryRecord.getProductId()))
                                .body(inventoryRecord);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
