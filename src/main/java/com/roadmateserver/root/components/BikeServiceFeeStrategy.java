package com.roadmateserver.root.components;

import org.springframework.stereotype.Component;

@Component
public class BikeServiceFeeStrategy implements ServiceFeeStrategy {
    @Override
    public Double calculateServiceFee(Double basePrice) {
        return basePrice * 0.05; //5%
    }
}
