package com.usermanager.client;

import com.usermanager.request.*;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthFeignClientTest {

    @Test
    void feignClientAnnotationIsPresent() {

        FeignClient annotation = AuthFeignClient.class.getAnnotation(FeignClient.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.name()).isEqualTo("Authentication-Service");
    }

    @Test
    void createUserMethodIsMappedCorrectly() throws Exception {

        Method method = AuthFeignClient.class.getMethod(
                "createUser", CreateUserRequest.class);

        PostMapping mapping = method.getAnnotation(PostMapping.class);

        assertThat(mapping).isNotNull();
        assertThat(mapping.value()).containsExactly("/api/internal/auth/create-user");

        assertThat(method.getReturnType()).isEqualTo(UserResponse.class);

        var params = method.getParameters();
        assertThat(params[0].getAnnotation(RequestBody.class)).isNotNull();
    }

    @Test
    void updateRoleMethodIsMappedCorrectly() throws Exception {

        Method method = AuthFeignClient.class.getMethod(
                "updateRole", Long.class, Role.class);

        PutMapping mapping = method.getAnnotation(PutMapping.class);

        assertThat(mapping).isNotNull();
        assertThat(mapping.value()).containsExactly("/api/internal/auth/users/{id}/role");

        assertThat(method.getReturnType()).isEqualTo(UserResponse.class);

        var params = method.getParameters();

        assertThat(params[0].getAnnotation(PathVariable.class)).isNotNull();
        assertThat(params[1].getAnnotation(RequestParam.class)).isNotNull();
    }

    @Test
    void deleteUserMethodIsMappedCorrectly() throws Exception {

        Method method = AuthFeignClient.class.getMethod(
                "deleteUser", Long.class);

        DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);

        assertThat(mapping).isNotNull();
        assertThat(mapping.value()).containsExactly("/api/internal/auth/users/{id}");

        assertThat(method.getReturnType()).isEqualTo(ActionResponse.class);

        assertThat(method.getParameters()[0].getAnnotation(PathVariable.class)).isNotNull();
    }

    @Test
    void getAllUsersMethodIsMappedCorrectly() throws Exception {

        Method method = AuthFeignClient.class.getMethod("getAllUsers");

        GetMapping mapping = method.getAnnotation(GetMapping.class);

        assertThat(mapping).isNotNull();
        assertThat(mapping.value()).containsExactly("/api/internal/auth/users");

        assertThat(method.getReturnType()).isEqualTo(List.class);
    }
}
