package com.bsolz.locationquery;

import com.bsolz.locationquery.config.LocationRequestHandler;
import com.bsolz.locationquery.config.LocationRouterConfig;
import com.bsolz.locationquery.model.Location;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {LocationRouterConfig.class, LocationRequestHandler.class})
public class LocationRouterConfigTest extends LocationBaseTest {

    @Test
    public void findByNameAndGeoTypeTest() {
        when(locationService.findByNameAndGeoType(anyString(), anyString())).thenReturn(locationListByNameAndGeoType);
        this.client
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/location/search")
                                .queryParam("name", "p")
                                .queryParam("geoType", "Country")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Location.class)
                .value(locationResponse -> {
                    Assertions.assertThat(locationResponse.get(0).geoId()) .isEqualTo("10Z2W9ZTH2RUM");
                    Assertions.assertThat(locationResponse.get(0).geoType()) .isEqualTo("Country");
                    Assertions.assertThat(locationResponse.get(0).name()) .isEqualTo("India");
                });
    }

    @Test
    public void pathNotFound() {
        when(locationService.findByGeoType(anyString())).thenReturn(locationList);
        this.client
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/location/search")
                                .build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }
}
