package com.roadmateserver.root.components;

import com.roadmateserver.root.common.Constants;
import org.springframework.stereotype.Component;

@Component
public class ServiceFeeStrategyFactory {
    private final CarServiceFeeStrategy carServiceFeeStrategy;
    private final BikeServiceFeeStrategy bikeServiceFeeStrategy;
    private final PickupTruckServiceFeeStrategy pickupTruckServiceFeeStrategy;
    private final SUVServiceFeeStrategy suvServiceFeeStrategy;
    private final VanServiceFeeStrategy vanServiceFeeStrategy;

    public ServiceFeeStrategyFactory(
            CarServiceFeeStrategy carServiceFeeStrategy,
            BikeServiceFeeStrategy bikeServiceFeeStrategy,
            PickupTruckServiceFeeStrategy pickupTruckServiceFeeStrategy,
            SUVServiceFeeStrategy suvServiceFeeStrategy,
            VanServiceFeeStrategy vanServiceFeeStrategy
    ){
        this.carServiceFeeStrategy = carServiceFeeStrategy;
        this.bikeServiceFeeStrategy = bikeServiceFeeStrategy;
        this.pickupTruckServiceFeeStrategy = pickupTruckServiceFeeStrategy;
        this.suvServiceFeeStrategy = suvServiceFeeStrategy;
        this.vanServiceFeeStrategy = vanServiceFeeStrategy;
    }

    public ServiceFeeStrategy getStrategy(Constants.VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> carServiceFeeStrategy;
            case MOTORCYCLE -> bikeServiceFeeStrategy;
            case VAN -> vanServiceFeeStrategy;
            case PICKUP_TRUCK -> pickupTruckServiceFeeStrategy;
            case SUV -> suvServiceFeeStrategy;
        };
    }
}
