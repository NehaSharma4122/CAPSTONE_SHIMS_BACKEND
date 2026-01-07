package com.usermanager.controller;

import com.usermanager.client.AuthFeignClient;
import com.usermanager.request.*;
import com.usermanager.service.EmailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    AuthFeignClient authClient;

    @Mock
    EmailService emailService;

    @InjectMocks
    UserController controller;

    @Test
    void getUsers_shouldReturnList() {

        var users = List.of(new UserResponse());
        users.get(0).setEmail("a@a.com");

        when(authClient.getAllUsers()).thenReturn(users);

        var result = controller.getUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("a@a.com");

        verify(authClient).getAllUsers();
    }

    @Test
    void addAgent_shouldCallFeignAndSendMail() {

        var req = new CreateUserRequest();
        req.setUsername("agent1");
        req.setEmail("a@a.com");
        req.setPassword("pass");

        var created = new UserResponse();
        created.setUserId(1L);
        created.setEmail("a@a.com");
        created.setRole(Role.ROLE_AGENT);

        when(authClient.createUser(any())).thenReturn(created);

        var result = controller.addAgent(req);

        verify(emailService).sendUserCreationEmail(req, created);
        verify(authClient, times(2)).createUser(any());

        assertThat(result).isNotNull();
    }

    @Test
    void addHospital_shouldCallFeignAndSendMail() {

        var req = new CreateUserRequest();
        req.setEmail("h@h.com");

        var created = new UserResponse();
        created.setUserId(5L);

        when(authClient.createUser(any())).thenReturn(created);

        controller.addHospital(req);

        verify(emailService).sendUserCreationEmail(req, created);
        verify(authClient, times(2)).createUser(any());
    }

    @Test
    void deleteUser_shouldCallFeign() {

        var result = controller.deleteUser(10L);

        verify(authClient).deleteUser(10L);

        assertThat(result.getStatus()).isEqualTo("SUCCESS");
    }

    @Test
    void updateRole_shouldCallFeignAndSendMail() {

        var req = new UpdateRoleRequest();
        req.setRole(Role.ROLE_AGENT);

        var updated = new UserResponse();
        updated.setUserId(3L);

        when(authClient.updateRole(3L, Role.ROLE_AGENT)).thenReturn(updated);

        controller.updateRole(3L, req);

        verify(emailService).sendRoleUpdateEmail(updated, Role.ROLE_AGENT);
        verify(authClient, times(2)).updateRole(3L, Role.ROLE_AGENT);
    }
}
