package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    /**
     * Finds a user entity by their unique clerk id.
     * @param clerkId the clerk id of the user to retrieve; must not be {@code null}.
     * @return an {@link Optional} containing the {@link UserEntity} with the specified clerk id, or empty if not found.
     */
    Optional<UserEntity> findByClerkId(String clerkId);
}
