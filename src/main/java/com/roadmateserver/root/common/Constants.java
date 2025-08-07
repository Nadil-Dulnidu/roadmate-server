package com.roadmateserver.root.common;

public class Constants {

    public static final String APPLICATION_JSON = "application/json";

    public enum UserRole {
        ADMIN,
        RENTER,
        OWNER,
        STAFF
    }

    public enum VehicleType {
        CAR,
        PICKUP_TRUCK,
        MOTORCYCLE,
        VAN,
    }

    public enum VehicleStatus {
        AVAILABLE,
        UNAVAILABLE,
        IN_MAINTENANCE,
        RESERVED
    }

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        COMPLETED,
        CANCELLED
    }
}
