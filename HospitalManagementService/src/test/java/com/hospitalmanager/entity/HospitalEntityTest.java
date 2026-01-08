package com.hospitalmanager.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HospitalEntityTest {

    @Test
    void hospital_entity_sets_fields_correctly() {

        Hospital h = new Hospital();
        h.setId(5L);
        h.setName("Apollo");

        assertEquals(5L, h.getId());
        assertEquals("Apollo", h.getName());
        assertNotNull(h.getLinkedPlans());
    }
}
