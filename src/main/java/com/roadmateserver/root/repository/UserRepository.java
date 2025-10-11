package com.roadmateserver.root.repository;

import com.roadmateserver.root.common.Constants;
import com.roadmateserver.root.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    /**
     * Finds a user entity by their unique clerk id.
     * @param clerkId the clerk id of the user to retrieve; must not be {@code null}.
     * @return an {@link Optional} containing the {@link UserEntity} with the specified clerk id, or empty if not found.
     */
    Optional<UserEntity> findByClerkId(String clerkId);


    List<UserEntity> findAllByRoleIn(List<Constants.UserRole> roles);
}
