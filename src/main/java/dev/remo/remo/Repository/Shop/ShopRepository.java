package dev.remo.remo.Repository.Shop;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;

public interface ShopRepository {
    Optional<ShopDO> getShopById(ObjectId shopId);
    void addShop(ShopDO shop);
    public String uploadFiles(MultipartFile file);
}
