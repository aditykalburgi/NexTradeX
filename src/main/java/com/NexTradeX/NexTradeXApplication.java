package com.NexTradeX;

import com.NexTradeX.market.MarketService;
import com.NexTradeX.user.User;
import com.NexTradeX.user.UserService;
import com.NexTradeX.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class NexTradeXApplication {
    
    private final UserService userService;
    private final WalletService walletService;
    private final MarketService marketService;

	public static void main(String[] args) {
		SpringApplication.run(NexTradeXApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
	public CommandLineRunner init() {
	    return args -> {
	        // Initialize default prices
	        marketService.initializeDefaultPrices();
	        
	        // Create test user if not exists
	        try {
	            User testUser = userService.createUser(
	                    "testuser",
	                    "test@nextradev.com",
	                    "TestPassword123",
	                    "Test",
	                    "User"
	            );
	            
	            // Initialize wallets for test user
	            walletService.initializeUserWallets(testUser);
	        } catch (Exception e) {
	            // User might already exist
	        }
	    };
	}
}

