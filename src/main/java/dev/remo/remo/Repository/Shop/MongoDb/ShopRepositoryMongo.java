package dev.remo.remo.Repository.Shop.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;
import dev.remo.remo.Repository.Shop.ShopRepository;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;

public class ShopRepositoryMongo implements ShopRepository {

    @Autowired
    ShopMongoDb shopMongoDb;

    @Autowired
    MongoDatabase mongoDatabase;

    public Optional<ShopDO> getShopById(ObjectId shopId) {
        return shopMongoDb.findById(shopId);
    }

    public void addShop(ShopDO shop) {
        shopMongoDb.save(shop);
    }

    public String uploadFiles(MultipartFile file) {
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, "shop");
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();

            ObjectId fileId = bucket.uploadFromStream(fileName, inputStream);
            return fileId.toString();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to upload file: " + file.getOriginalFilename());
        }
    }
}
