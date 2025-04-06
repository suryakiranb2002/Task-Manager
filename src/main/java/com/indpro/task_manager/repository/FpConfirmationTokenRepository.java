package com.indpro.task_manager.repository;

import com.indpro.task_manager.entity.FpConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FpConfirmationTokenRepository extends JpaRepository<FpConfirmationToken,Long> {
    FpConfirmationToken findByFpToken(String fpToken);
}
