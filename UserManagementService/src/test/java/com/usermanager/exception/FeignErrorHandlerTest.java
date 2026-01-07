package com.usermanager.exception;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FeignErrorHandlerTest {

    private final FeignErrorHandler handler = new FeignErrorHandler();

    private Request dummyRequest() {
        return Request.create(
                Request.HttpMethod.GET,
                "/test",
                Map.of(),
                null,
                StandardCharsets.UTF_8,
                null
        );
    }

    @Test
    void returns404ForNotFound() {

        FeignException ex = new FeignException.NotFound(
                "Not Found",
                dummyRequest(),
                null,
                null
        );

        var res = handler.handleFeignException(ex);

        assertThat(res.getStatusCode().value()).isEqualTo(404);
        assertThat(res.getBody()).isEqualTo("User not found in Authentication Service");
    }

    @Test
    void returns400ForBadRequest() {

        FeignException ex = new FeignException.BadRequest(
                "Bad Request",
                dummyRequest(),
                null,
                null
        );

        var res = handler.handleFeignException(ex);

        assertThat(res.getStatusCode().value()).isEqualTo(400);
        assertThat(res.getBody()).isEqualTo("Invalid request");
    }

    @Test
    void returns409ForConflict() {

        FeignException ex = new FeignException.Conflict(
                "Conflict",
                dummyRequest(),
                null,
                null
        );

        var res = handler.handleFeignException(ex);

        assertThat(res.getStatusCode().value()).isEqualTo(409);
        assertThat(res.getBody()).isEqualTo("Conflict — duplicate or invalid operation");
    }

    @Test
    void returns409ForDuplicateEmail500Case() {

        FeignException ex = new FeignException.InternalServerError(
                "Email already exists",
                dummyRequest(),
                null,
                null
        );

        var res = handler.handleFeignException(ex);

        assertThat(res.getStatusCode().value()).isEqualTo(409);
        assertThat(res.getBody()).isEqualTo("User already exists");
    }

    @Test
    void returns500DefaultError() {

        FeignException ex = new FeignException.InternalServerError(
                "Random server error",
                dummyRequest(),
                null,
                null
        );

        var res = handler.handleFeignException(ex);

        assertThat(res.getStatusCode().value()).isEqualTo(500);
        assertThat(res.getBody()).isEqualTo("Authentication service error");
    }
}
