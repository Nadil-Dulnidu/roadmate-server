package com.roadmateserver.root.repository;

import com.roadmateserver.root.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

}
