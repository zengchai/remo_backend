package dev.remo.remo.Repository.Shop;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;

public interface ShopRepository {

    /**
     * Retrieves a paginated list of shops by their IDs.
     *
     * @param shopId   the list of shop IDs
     * @param pageable pagination information
     * @return a paginated list of shops
     */
    Optional<ShopDO> getShopById(ObjectId shopId);

    /**
     * Retrieves a list of shops.
     *
     * @return a list of ShopDO objects
     */
    List<ShopDO> getAllShops();

    /**
     * Adds a new shop.
     * 
     * @param shop the ShopDO object to add
     */
    void addShop(ShopDO shop);

    /**
     * Get shop image by its ID.
     * 
     * @param imageId the ID of the shop image
     */
    Optional<Resource> getShopImageById(ObjectId imageId);


    /**
     * Uploads a image associated with a shop.
     * 
     * @param file the image to upload
     * @return the id of the image
     */
    String uploadFiles(MultipartFile file);
}
