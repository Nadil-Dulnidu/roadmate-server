package com.roadmateserver.root.mapper;

import com.roadmateserver.root.dto.BookingDTO;
import com.roadmateserver.root.entity.BookingEntity;

import java.util.Objects;

public class BookingDTOEntityMapper {
    public static BookingDTO map(final BookingEntity bookingEntity) {
        if (Objects.isNull(bookingEntity))
            throw new IllegalArgumentException("BookingEntity cannot be null");
        final BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(bookingEntity.getBookingId());
        if(Objects.isNull(bookingEntity.getRenter()))
            throw new IllegalArgumentException("Renter cannot be null in BookingEntity");
        bookingDTO.setRenterId(bookingEntity.getRenter().getClerkId());
        bookingDTO.setStartDate(bookingEntity.getStartDate());
        bookingDTO.setEndDate(bookingEntity.getEndDate());
        bookingDTO.setStatus(bookingEntity.getStatus());
        bookingDTO.setTotalPrice(bookingEntity.getTotalPrice());
        bookingDTO.setCreatedAt(bookingEntity.getCreatedAt());
        return bookingDTO;

    }

    public static BookingEntity map(final BookingDTO bookingDTO) {
        if (Objects.isNull(bookingDTO))
            throw new IllegalArgumentException("BookingDTO cannot be null");
        final BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setStartDate(bookingDTO.getStartDate());
        bookingEntity.setEndDate(bookingDTO.getEndDate());
        bookingEntity.setStatus(bookingDTO.getStatus());
        bookingEntity.setTotalPrice(bookingDTO.getTotalPrice());
        bookingEntity.setCreatedAt(bookingDTO.getCreatedAt());
        return bookingEntity;
    }
}