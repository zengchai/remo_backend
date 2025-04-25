package dev.remo.remo.Repository.Shop.MongoDb;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;
import dev.remo.remo.Repository.Shop.ShopRepository;

public class ShopRepositoryMongo implements ShopRepository {

    @Autowired
    ShopMongoDb shopMongoDb;

    public Optional<ShopDO> getShopById(ObjectId shopId) {
        return shopMongoDb.findById(shopId);
    }
}
