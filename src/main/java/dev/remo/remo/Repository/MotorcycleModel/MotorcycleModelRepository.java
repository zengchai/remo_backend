package dev.remo.remo.Repository.MotorcycleModel;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;

public interface MotorcycleModelRepository {
public List<MotorcycleModelDO> getMotorcycleList();
public Optional<MotorcycleModelDO> getMotorcycleModelById(ObjectId id);
public Optional<MotorcycleModelDO> findByBrandAndModel(String brand, String model);
public void addReviewIdToMotorcycleModel(MotorcycleModelDO motorcycleModelDO);
}
