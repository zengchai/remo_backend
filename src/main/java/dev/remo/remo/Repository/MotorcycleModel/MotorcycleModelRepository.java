package dev.remo.remo.Repository.MotorcycleModel;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;

public interface MotorcycleModelRepository {
    List<MotorcycleModelDO> getMotorcycleList();

    Optional<MotorcycleModelDO> getMotorcycleModelById(ObjectId id);

    Optional<MotorcycleModelDO> findByBrandAndModel(String brand, String model);

    void addOrUpdateMotorcycleModel(MotorcycleModelDO motorcycleModelDO);

    Page<MotorcycleModelDO> getAllMotorcycleModelByPage(Pageable pageable);

    String uploadFiles(MultipartFile file);

    Optional<Resource> getMotorcycleModelImageById(ObjectId id);

}
