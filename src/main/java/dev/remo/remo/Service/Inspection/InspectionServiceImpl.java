package dev.remo.remo.Service.Inspection;

import java.lang.Thread.State;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Mappers.InspectionMapper;
import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Inspection.Shop.Shop;
import dev.remo.remo.Models.Inspection.Shop.ShopDO;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.Inspection.InspectionRepository;
import dev.remo.remo.Repository.Shop.ShopRepository;
import dev.remo.remo.Service.Listing.MotorcycleListingService;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Exception.NotFoundException;

public class InspectionServiceImpl implements InspectionService {

    @Autowired
    InspectionRepository inspectionRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    UserService userService;

    @Autowired
    MotorcycleListingService motorcycleListingService;

    @Autowired
    InspectionMapper inspectionMapper;

    public ShopDO getShopById(String shopId) {
        return shopRepository.getShopById(new ObjectId(shopId)).orElseThrow(() -> {
            return new NotFoundException("Shop not found for ID: " + shopId);
        });
    }

    public Inspection getInspectionById(String inspectionId) {
        return inspectionMapper
                .convertInspectionDOToInspection(inspectionRepository.getInspectionById(new ObjectId(inspectionId))
                        .orElseThrow(() -> new NotFoundException("Inspection not found for ID: " + inspectionId)));
    }

    public void createInspection(CreateInspectionRequest request, String accessToken) {
        User currentUser = userService.getUserByAccessToken(accessToken);
        Inspection inspection = inspectionMapper.toDomain(request);

        ShopDO shop = getShopById(request.getShopId());
        MotorcycleListing motorcycleListing = motorcycleListingService
                .getMotorcycleListingById(request.getMotorcycleListingId());
        inspection.setShop(inspectionMapper.convertShopDOToShop(shop));
        inspection.setUser(currentUser);
        inspection.setMotorcycleListing(motorcycleListing);

        inspectionRepository.createInspection(inspectionMapper.convertInspectionToInspectionDO(inspection));
    }

    public void updateInspectionStatus(String inspectionId, String status, String remark, String accessToken) {
        Inspection inspection = getInspectionById(inspectionId);
        User currentUser = userService.getUserByAccessToken(accessToken);

        StatusEnum statusEnum = StatusEnum.fromCode(status);

        Map<String, String> extInfo = inspection.getExtInfo();
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }

        extInfo.put("updatedBy", currentUser.getEmail());
        extInfo.put("updatedAt", LocalDateTime.now().toString());
        extInfo.put("remark", remark);

        inspectionRepository.updateInspectionStatus(new ObjectId(inspection.getId()),
                statusEnum.getCode(), extInfo.get("updatedBy"),
                extInfo.get("updatedAt"), extInfo.get("remark"));
    }

}
