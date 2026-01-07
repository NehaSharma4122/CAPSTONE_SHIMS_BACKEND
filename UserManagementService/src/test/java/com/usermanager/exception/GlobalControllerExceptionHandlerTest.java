package com.usermanager.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalControllerExceptionHandlerTest {

    private final GlobalControllerExceptionHandler handler =
            new GlobalControllerExceptionHandler();

    @Test
    void shouldReturn400ForInvalidJson() {

        var ex = new HttpMessageNotReadableException("bad json");

        var response = handler.handleInvalidJson(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody())
                .isEqualTo("Invalid request body — invalid or unknown role value");
    }
}
