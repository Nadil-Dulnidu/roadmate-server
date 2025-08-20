package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    /**
     * Finds all payments associated with a specific user by their clerk ID.
     *
     * @param userId the clerk ID of the user
     * @return a list of PaymentEntity objects associated with the specified user
     */
    List<PaymentEntity> findAllByUser_ClerkId(String userId);
}
