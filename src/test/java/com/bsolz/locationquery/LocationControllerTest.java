package com.bsolz.locationquery;


import com.bsolz.locationquery.controller.LocationController;
import com.bsolz.locationquery.model.Location;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.http.MediaType;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@WebFluxTest(LocationController.class)
public class LocationControllerTest extends LocationBaseTest {




    @Test
    public void claimsTest() {
        this.client
                .get()
                .uri("/location/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.sub").isEqualTo("user")
                .jsonPath("$.scope").isEqualTo("read");

    }

    @Test
    public void findByGeoTypeTest() {
        when(locationService.findByGeoType(anyString())).thenReturn(locationList);
        this.client
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/location")
                                .queryParam("geoType", "City")
                                .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Location.class)
                .hasSize(2);
    }
    @Test
    public void findByGeoNameTest() {
        when(locationService.findByName(anyString())).thenReturn(locationList);
        this.client
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/location/search-by-name")
                                .queryParam("name", "P")
                                .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Location.class)
                .hasSize(2);
    }

    @Test
    public void pathNotFound() {
        when(locationService.findByGeoType(anyString())).thenReturn(locationList);
        this.client
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/location")
                                .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }
}
