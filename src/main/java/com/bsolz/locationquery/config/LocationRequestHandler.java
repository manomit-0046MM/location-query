package com.bsolz.locationquery.config;

import com.bsolz.locationquery.exception.InputValidationException;
import com.bsolz.locationquery.model.Location;
import com.bsolz.locationquery.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocationRequestHandler {

    private final LocationService locationService;


    public Mono<ServerResponse> allLocation(ServerRequest serverRequest) {
        return ServerResponse.ok().body(locationService.findAllLocation(), Location.class);
    }

    public Mono<ServerResponse> findByNameAndGeoType(ServerRequest serverRequest) {
        if (serverRequest.queryParam("name").isEmpty() || serverRequest.queryParam("geoType").isEmpty()) {
            // throw new InputValidationException(HttpStatus.FORBIDDEN.value());
            Mono.error(new InputValidationException(HttpStatus.FORBIDDEN.value()));
        }
        return ServerResponse.ok().body(locationService.findByNameAndGeoType(
                serverRequest.queryParam("name").orElseThrow(() -> new InputValidationException(HttpStatus.FORBIDDEN.value())),
                serverRequest.queryParam("geoType").orElseThrow(() -> new InputValidationException(HttpStatus.FORBIDDEN.value()))
        ), Location.class);
    }

    public Mono<ServerResponse> findByName(ServerRequest serverRequest) {
        return ServerResponse.ok().body(
                locationService.findByName(serverRequest.queryParam("name").orElseThrow(() -> new IllegalArgumentException("Name parameter not found"))),
                Location.class
        );
    }

    public Mono<ServerResponse> findByGeoType(ServerRequest serverRequest) {
        return ServerResponse.ok().body(
            locationService.findByGeoType(serverRequest.queryParam("geoType").orElseThrow(() -> new IllegalArgumentException("Name parameter not found"))
        ), Location.class);
    }
}
