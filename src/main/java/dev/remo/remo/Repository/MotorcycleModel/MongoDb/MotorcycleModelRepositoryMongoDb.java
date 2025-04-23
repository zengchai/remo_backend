package dev.remo.remo.Repository.MotorcycleModel.MongoDb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;

public class MotorcycleModelRepositoryMongoDb implements MotorcycleModelRepository{

    @Autowired
    MotorcycleModelMongoDb motorcycleModelMongoDb;
    
    public List<MotorcycleModelDO> getMotorcycleList(){
        return motorcycleModelMongoDb.findAll();
    }
        
    public MotorcycleModelDO getMotorcycleDO(String brand, String model){
        return motorcycleModelMongoDb.findByBrandAndModel(brand,model);
    }


}
