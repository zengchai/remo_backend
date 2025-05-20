package dev.remo.remo.Service.MotorcycleModel;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;

public interface MotorcycleModelService {

    public List<MotorcycleModel> getMotorcycleList();

    public MotorcycleModel getMotorcycleByBrandAndModel(String brand, String model);

    public MotorcycleModel getMotorcycleByBrand(String brand);

    public MotorcycleModel getMotorcycleModelById(String id);

    public Resource getMotorcycleModelImageById(String id);

    public void createMotorcycleModel(String brand, String model, MultipartFile image);

    public void updateMotorcycleModelReviewList(MotorcycleModel motorcycleModel);

    public void removeReviewIdListById(String modelId, String reviewId);
}
