package com.roadmateserver.root.dto.cache;

import com.roadmateserver.root.dto.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleListCache implements Serializable {

    private List<VehicleDTO> vehicles;
}
