package com.claimmanager.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClaimDocumentEntityTests {

    @Test
    void testClaimDocumentFields() {

        Claim claim = new Claim();
        claim.setId(1L);

        ClaimDocument doc = new ClaimDocument(
                10L,
                "invoice.pdf",
                "http://files/invoice.pdf",
                claim
        );

        assertThat(doc.getId()).isEqualTo(10L);
        assertThat(doc.getFileName()).isEqualTo("invoice.pdf");
        assertThat(doc.getFileUrl()).isEqualTo("http://files/invoice.pdf");
        assertThat(doc.getClaim()).isEqualTo(claim);
    }

    @Test
    void testClaimDocumentSetters() {

        ClaimDocument doc = new ClaimDocument();

        doc.setId(2L);
        doc.setFileName("report.pdf");
        doc.setFileUrl("http://files/report.pdf");

        assertThat(doc.getId()).isEqualTo(2L);
        assertThat(doc.getFileName()).isEqualTo("report.pdf");
        assertThat(doc.getFileUrl()).isEqualTo("http://files/report.pdf");
    }
}
