package com.claimmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor 
@NoArgsConstructor
@Table(name = "claim_documents")
public class ClaimDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "claim_id")
    private Claim claim;
}
