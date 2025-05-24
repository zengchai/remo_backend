package dev.remo.remo.Service.MotorcycleModel;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.FilterForumRequest;

public interface MotorcycleModelService {

     List<MotorcycleModel> getMotorcycleList();

     Page<MotorcycleModel> getMotorcycleModelByFilter(FilterForumRequest filterForumRequest, int page, int size);

     MotorcycleModel getMotorcycleByBrandAndModel(String brand, String model);

     MotorcycleModel getMotorcycleByBrand(String brand);

     MotorcycleModel getMotorcycleModelById(String id);

     Resource getMotorcycleModelImageById(String id);

     void createMotorcycleModel(String brand, String model, MultipartFile image);

     void updateMotorcycleModelReviewList(MotorcycleModel motorcycleModel);

     void removeReviewIdListById(String modelId, String reviewId);
}
