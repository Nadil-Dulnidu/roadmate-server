package com.roadmateserver.root.controller;

import com.roadmateserver.root.dto.ListingCountProjection;
import com.roadmateserver.root.repository.BookingRepository;
import com.roadmateserver.root.repository.VehicleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;

    public AuditController(BookingRepository bookingRepository,
                           VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/total-revenue")
    public Map<String, Double> getCompletedRevenue() {
        Double totalRevenue = bookingRepository.getTotalRevenue();
        return Map.of("totalRevenue", totalRevenue);
    }

    @GetMapping("/total-revenue-by-vehicle/{vehicleId}")
    public Map<String, Double> getTotalRevenueByVehicle(@PathVariable Integer vehicleId) {
        Double totalRevenue = bookingRepository.getTotalRevenueByVehicleId(vehicleId);
        return Map.of("totalRevenue", totalRevenue);
    }

    @GetMapping("/listing-count")
    public List<ListingCountProjection> getListingCountByDate() {
        return vehicleRepository.getListingCountByDate();
    }
}
