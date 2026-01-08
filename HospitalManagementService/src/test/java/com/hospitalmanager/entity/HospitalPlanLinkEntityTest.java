package com.hospitalmanager.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HospitalPlanLinkEntityTest {

    @Test
    void link_entity_sets_fields() {

        HospitalPlanLink link = new HospitalPlanLink();
        link.setId(1L);
        link.setPlanId(3L);

        assertEquals(1L, link.getId());
        assertEquals(3L, link.getPlanId());
    }
}
