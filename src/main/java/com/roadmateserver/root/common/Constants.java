package com.roadmateserver.root.common;

public class Constants {

    public static final String APPLICATION_JSON = "application/json";

    public static final String ADMIN_ROLE_PERMISSION = "hasRole('ADMIN')";
    public static final String STAFF_ROLE_PERMISSION = "hasRole('STAFF')";
    public static final String RENTER_ROLE_PERMISSION = "hasRole('RENTER')";
    public static final String OWNER_ROLE_PERMISSION = "hasRole('OWNER')";
    public static final String ADMIN_OR_RENTER_ROLE_PERMISSION = "hasAnyRole('ADMIN', 'RENTER')";
    public static final String ADMIN_OR_OWNER_ROLE_PERMISSION = "hasAnyRole('ADMIN', 'OWNER')";
    public static final String ADMIN_OR_STAFF_ROLE_PERMISSION = "hasAnyRole('ADMIN', 'STAFF')";
    public static final String RENTER_OR_OWNER_ROLE_PERMISSION = "hasAnyRole('RENTER', 'OWNER')";

    public enum UserRole {
        ADMIN,
        RENTER,
        OWNER,
        STAFF
    }

    public enum VehicleType {
        CAR,
        SUV,
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

    public enum EngineType {
        PETROL,
        DIESEL,
        ELECTRIC,
        HYBRID
    }

    public enum TransmissionType {
        MANUAL,
        AUTOMATIC
    }
}
