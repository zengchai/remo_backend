package dev.remo.remo.Repository.Shop.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;
import dev.remo.remo.Repository.Shop.ShopRepository;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;
import jakarta.annotation.PostConstruct;

public class ShopRepositoryMongo implements ShopRepository {

    @Autowired
    ShopMongoDb shopMongoDb;

    @Autowired
    MongoDatabase mongoDatabase;

    private GridFSBucket gridFSBucket;

    @PostConstruct
    public void init() {
        this.gridFSBucket = GridFSBuckets.create(mongoDatabase, "shop");
    }

    public Optional<ShopDO> getShopById(ObjectId shopId) {
        return shopMongoDb.findById(shopId);
    }

    public void addShop(ShopDO shop) {
        shopMongoDb.save(shop);
    }

    public String uploadFiles(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            ObjectId fileId = gridFSBucket.uploadFromStream(fileName, inputStream);

            return fileId.toString();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to upload file: " + file.getOriginalFilename());
        }
    }

    public List<ShopDO> getAllShops() {
        return shopMongoDb.findAll();
    }

    public Optional<ShopDO> getShopById(String shopId) {
        return shopMongoDb.findById(new ObjectId(shopId));
    }

    public Optional<Resource> getShopImageById(ObjectId imageId) {
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(imageId);
        return Optional.ofNullable(new GridFsResource(downloadStream.getGridFSFile(), downloadStream));
    }
}
