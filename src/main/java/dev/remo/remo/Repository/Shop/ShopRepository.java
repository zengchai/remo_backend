package dev.remo.remo.Repository.Shop;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;

public interface ShopRepository {
    Optional<ShopDO> getShopById(ObjectId shopId);
    List<ShopDO> getAllShops();
    void addShop(ShopDO shop);
    Optional<Resource> getShopImageById(ObjectId imageId);
    String uploadFiles(MultipartFile file);
}
