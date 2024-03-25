package com.bsolz.locationquery.controller;

import com.bsolz.locationquery.exception.InputValidationException;
import com.bsolz.locationquery.model.Location;
import com.bsolz.locationquery.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("test")
    public Mono<Map<String, Object>> claims(@AuthenticationPrincipal JwtAuthenticationToken auth) {
        return Mono.just(auth.getTokenAttributes());
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Location>>> findByGeoType(@RequestParam String geoType) {
        return Mono.just(geoType)
                .filter(s -> !s.isEmpty())
                .map(locationService::findByGeoType)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("search-by-name")
    public Flux<Location> searchByName(@RequestParam String name) {
        return Flux.just(name)
                .handle((value, sink) -> {
                    if (value.isEmpty())
                        sink.error(new InputValidationException(HttpStatus.BAD_REQUEST.value()));
                    else
                        sink.next(name);
                }).cast(String.class)
                .flatMap(this.locationService::findByName);
    }
}
