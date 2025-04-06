package com.indpro.task_manager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JwtToken {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String jwToken;

    public boolean revoked;

    public boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public JwtToken(String jwToken,
                    boolean revoked, boolean expired,
                    User user) {
        this.jwToken = jwToken;
        this.revoked = revoked;
        this.expired = expired;
        this.user = user;
    }
}
