package dev.remo.remo.Repository.Shop;

import java.util.Optional;

import org.bson.types.ObjectId;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;

public interface ShopRepository {
    Optional<ShopDO> getShopById(ObjectId shopId);
}
