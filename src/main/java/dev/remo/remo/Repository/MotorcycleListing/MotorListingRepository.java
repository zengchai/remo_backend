package dev.remo.remo.Repository.MotorcycleListing;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;

public interface MotorListingRepository {
    public MotorcycleListingDO createOrUpdateListing(MotorcycleListingDO listing);

}
