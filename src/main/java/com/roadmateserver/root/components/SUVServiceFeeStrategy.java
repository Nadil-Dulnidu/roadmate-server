package com.roadmateserver.root.components;

import org.springframework.stereotype.Component;

@Component
public class SUVServiceFeeStrategy implements ServiceFeeStrategy {
    @Override
    public Double calculateServiceFee(Double basePrice) {
        return basePrice * 0.15; //15%
    }
}
