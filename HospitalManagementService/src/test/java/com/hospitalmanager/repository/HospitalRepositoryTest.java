package com.hospitalmanager.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HospitalRepositoryTest {

    @Test
    void repositoryInterfaceLoads() {
        assertNotNull(HospitalRepository.class);
    }
}
