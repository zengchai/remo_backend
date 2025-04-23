package dev.remo.remo.Repository.MotorcycleModel;

import java.util.List;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;

public interface MotorcycleModelRepository {
public List<MotorcycleModelDO> getMotorcycleList();
public MotorcycleModelDO getMotorcycleDO(String brand, String model);
}
