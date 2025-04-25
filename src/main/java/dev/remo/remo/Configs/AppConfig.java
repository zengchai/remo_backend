package dev.remo.remo.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.remo.remo.Service.Ai.AiChatbotService;
import dev.remo.remo.Service.Ai.AiChatbotServiceImpl;
import dev.remo.remo.Service.Forum.ForumService;
import dev.remo.remo.Service.Forum.ForumServiceImpl;
import dev.remo.remo.Service.Inspection.InspectionService;
import dev.remo.remo.Service.Inspection.InspectionServiceImpl;
import dev.remo.remo.Service.Listing.MotorcycleListingService;
import dev.remo.remo.Service.Listing.MotorcycleListingServiceImpl;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelServiceImpl;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Service.User.UserServiceImpl;
import dev.remo.remo.Repository.Forum.ForumRepository;
import dev.remo.remo.Repository.Forum.MongoDb.ForumRepositoryMongoDb;
import dev.remo.remo.Repository.Inspection.InspectionRepository;
import dev.remo.remo.Repository.Inspection.MongoDb.InspectionRepositoryMongoDb;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;
import dev.remo.remo.Repository.MotorcycleListing.MongoDb.MotorListingRepositoryMongoDb;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;
import dev.remo.remo.Repository.MotorcycleModel.MongoDb.MotorcycleModelRepositoryMongoDb;
import dev.remo.remo.Repository.Shop.ShopRepository;
import dev.remo.remo.Repository.Shop.MongoDb.ShopRepositoryMongo;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Repository.User.MongoDb.UserRespositoryMongoDb;

@Configuration
public class AppConfig {

    // Service Layer
    @Bean
    UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    ForumService reviewService() {
        return new ForumServiceImpl();
    }

    @Bean
    MotorcycleModelService motorcycleModelService() {
        return new MotorcycleModelServiceImpl();
    }

    @Bean
    MotorcycleListingService motorcycleListingService() {
        return new MotorcycleListingServiceImpl();
    }

    @Bean
    InspectionService inspectionService() {
        return new InspectionServiceImpl();
    }

    @Bean
    AiChatbotService aiChatbotService() {
        return new AiChatbotServiceImpl();
    }

    // Repository Layer
    @Bean
    UserRepository userRepository() {
        return new UserRespositoryMongoDb();
    }

    @Bean
    MotorcycleModelRepository motorcycleModelRepository() {
        return new MotorcycleModelRepositoryMongoDb();
    }

    @Bean
    MotorListingRepository motorListingRepository() {
        return new MotorListingRepositoryMongoDb();
    }

    @Bean
    ForumRepository forumRepository() {
        return new ForumRepositoryMongoDb();
    }

    @Bean
    InspectionRepository inspectionRepository() {
        return new InspectionRepositoryMongoDb();
    }

    @Bean
    ShopRepository shopRepository() {
        return new ShopRepositoryMongo();
    }

    // Utils
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}