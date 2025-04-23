package dev.remo.remo.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.remo.remo.Service.Ai.AiChatbotService;
import dev.remo.remo.Service.Ai.AiChatbotServiceImpl;
import dev.remo.remo.Service.Forum.ReviewService;
import dev.remo.remo.Service.Forum.ReviewServiceImpl;
import dev.remo.remo.Service.Listing.MotorcycleListingService;
import dev.remo.remo.Service.Listing.MotorcycleListingServiceImpl;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelServiceImpl;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Service.User.UserServiceImpl;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;
import dev.remo.remo.Repository.MotorcycleListing.MongoDb.MotorListingRepositoryMongoDb;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;
import dev.remo.remo.Repository.MotorcycleModel.MongoDb.MotorcycleModelRepositoryMongoDb;
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
    ReviewService reviewService() {
        return new ReviewServiceImpl();
    }

    @Bean
    MotorcycleModelService motorcycleService() {
        return new MotorcycleModelServiceImpl();
    }

    @Bean
    MotorcycleListingService motorcycleListingService() {
        return new MotorcycleListingServiceImpl();
    }

    // Repository Layer
    @Bean
    UserRepository userRepository() {
        return new UserRespositoryMongoDb();
    }

    @Bean
    MotorcycleModelRepository motorcycleRepository() {
        return new MotorcycleModelRepositoryMongoDb();
    }

    @Bean
    MotorListingRepository motorListingRepository() {
        return new MotorListingRepositoryMongoDb();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Returns a new instance of BCryptPasswordEncoder
    }

    @Bean
    public AiChatbotService aiChatbotService() {
        return new AiChatbotServiceImpl(); // Returns a new instance of AiChatbotServiceImpl
    }
}