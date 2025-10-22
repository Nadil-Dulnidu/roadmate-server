package com.roadmateserver.root.dto;

import java.time.LocalDate;

public interface ListingCountProjection {
    LocalDate getDate();
    Integer getListingCount();
}
