package com.globomantics.inventorymanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globomantics.inventorymanager.model.InventoryRecord;
import com.globomantics.inventorymanager.model.PurchaseRecord;
import com.globomantics.inventorymanager.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerTest {
    @MockBean
    private InventoryService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /inventory/1 - Success")
    void testGetInventoryByIdSuccess() throws Exception {
        // Setup our mocked service
        InventoryRecord mockRecord = new InventoryRecord(1, 10,
                "Product 1", "Great Products");
        doReturn(Optional.of(mockRecord)).when(service).getInventoryRecord(1);

        // Execute the GET request
        mockMvc.perform(get("/inventory/{id}", 1))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

                // Validate the headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/inventory/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.productName", is("Product 1")))
                .andExpect(jsonPath("$.productCategory", is("Great Products")));
    }

    @Test
    @DisplayName("GET /inventory/2 - Not Found")
    void testGetInventoryByIdNotFound() throws Exception {
        // Setup our mocked service
        doReturn(Optional.empty()).when(service).getInventoryRecord(2);

        // Execute the GET request
        mockMvc.perform(get("/inventory/{id}", 2))

                // Validate the response code is 404 Not Found
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /inventory/purchase-record - Success")
    void testCreatePurchaseRecord() throws Exception {
        // Setup mocked service
        InventoryRecord mockRecord = new InventoryRecord(1, 10,
                "Product 1", "Great Products");
        doReturn(Optional.of(mockRecord)).when(service).purchaseProduct(1, 5);


        mockMvc.perform(post("/inventory/purchase-record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new PurchaseRecord(1, 5))))

                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

                // Validate the headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/inventory/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.productName", is("Product 1")))
                .andExpect(jsonPath("$.productCategory", is("Great Products")));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
