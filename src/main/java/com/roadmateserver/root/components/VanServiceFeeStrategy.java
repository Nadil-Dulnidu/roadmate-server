package com.roadmateserver.root.components;

import org.springframework.stereotype.Component;

@Component
public class VanServiceFeeStrategy implements ServiceFeeStrategy {
    @Override
    public Double calculateServiceFee(Double basePrice) {
        return basePrice * 0.12; //12%
    }
}
