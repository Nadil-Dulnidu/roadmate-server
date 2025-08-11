package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

}
