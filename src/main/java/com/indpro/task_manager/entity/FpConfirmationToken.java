package com.indpro.task_manager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "FpConfirmationToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FpConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private Long fpTokenId;

    @Column(nullable = false)
    private String fpToken;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    public FpConfirmationToken(String fpToken,
                               LocalDateTime createdAt,
                               LocalDateTime expiresAt,
                               User user) {
        this.fpToken = fpToken;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
