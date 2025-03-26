package jonas.elobooahrd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jonas.elobooahrd.model.GameContainer;

/**
 * Application configuration for the Eloboard system.
 */
@Configuration
public class AppConfig {
    
    /**
     * Creates a singleton GameContainer bean to be shared across services
     * 
     * @return The GameContainer instance
     */
    @Bean
    public GameContainer gameContainer() {
        return new GameContainer("Main Game Container");
    }
}
