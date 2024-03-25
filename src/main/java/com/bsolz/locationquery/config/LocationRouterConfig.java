package com.bsolz.locationquery.config;

import com.bsolz.locationquery.exception.InputValidationException;
import com.bsolz.locationquery.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;

@Configuration
@RequiredArgsConstructor
public class LocationRouterConfig {

    private final LocationRequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> mainRouterFunction() {
        return RouterFunctions.route()
                .path("location", this::responseRouterFunction)
                .build();
    }

    private RouterFunction<ServerResponse> responseRouterFunction() {
        return RouterFunctions.route()
                .GET("search",
                        RequestPredicates
                                .all()
                                .and(queryParam("name", t -> !t.isEmpty())
                                .and(queryParam("geoType", t -> !t.isEmpty()))),
                requestHandler::findByNameAndGeoType)
                .GET("search", req -> ServerResponse.badRequest().bodyValue(
                    new ApiResponse(HttpStatus.BAD_REQUEST.value(), "No Parameter Founds", new SimpleDateFormat("MMM dd,yyyy HH:mm").format(new Date(System.currentTimeMillis()))
                )))
                //.onError(InputValidationException.class, exceptionHandler())
                .GET("all", requestHandler::allLocation)
                .filter(dataNotFoundToBadRequest())
                .build();
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> dataNotFoundToBadRequest() {
        System.out.println("Filter Here");
        return (request, next) -> next.handle(request)
                .onErrorResume(InputValidationException.class, e -> ServerResponse.badRequest().build());
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        System.out.println("Exception Here");
        return (err, req) -> {
            var ex = (InputValidationException) err;
            return ServerResponse.badRequest()
                    .bodyValue(
                            new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new SimpleDateFormat("MMM dd,yyyy HH:mm").format(new Date(System.currentTimeMillis())))
                    );
        };
    }
}
