package dev.remo.remo.Service.MotorcycleModel;

import java.util.List;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;

public interface MotorcycleModelService {
    public List<MotorcycleModel> getMotorcycleList();
    public MotorcycleModel getMotorcycleByBrandAndModel(String brand,String model);
}
