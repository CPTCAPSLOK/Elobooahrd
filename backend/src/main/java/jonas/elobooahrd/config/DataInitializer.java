package jonas.elobooahrd.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jonas.elobooahrd.dto.GameDTO;
import jonas.elobooahrd.dto.PlayerDTO;
import jonas.elobooahrd.service.GameService;
import jonas.elobooahrd.service.PlayerService;

/**
 * Data initializer to populate the application with sample data on startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final GameService gameService;
    private final PlayerService playerService;

    @Autowired
    public DataInitializer(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only initialize data if no games exist
        if (gameService.getAllGames().isEmpty()) {
            initializeData();
        }
    }
    
    private void initializeData() {
        // Create sample games
        GameDTO chess = new GameDTO();
        chess.setName("Chess");
        chess.setDescription("Classic strategy board game");
        chess.setType("Tablefootball"); // Using supported game type
        chess.setInitialEloRating(1200);
        chess.setKFactor(32);
        chess = gameService.createGame(chess);
        
        GameDTO pingPong = new GameDTO();
        pingPong.setName("Ping Pong");
        pingPong.setDescription("Table tennis game");
        pingPong.setType("Tablefootball"); // Using supported game type
        pingPong.setInitialEloRating(1500);
        pingPong.setKFactor(24);
        pingPong = gameService.createGame(pingPong);
        
        GameDTO darts = new GameDTO();
        darts.setName("Darts");
        darts.setDescription("Dart throwing game");
        darts.setType("Dart"); // Using supported game type
        darts.setInitialEloRating(1400);
        darts.setKFactor(28);
        darts = gameService.createGame(darts);
        
        // Create sample players
        PlayerDTO player1 = new PlayerDTO();
        player1.setName("Alice");
        Map<UUID, Integer> aliceElo = new HashMap<>();
        aliceElo.put(chess.getId(), 1250);
        aliceElo.put(pingPong.getId(), 1550);
        player1.setEloRatings(aliceElo);
        playerService.createPlayer(player1);
        
        PlayerDTO player2 = new PlayerDTO();
        player2.setName("Bob");
        Map<UUID, Integer> bobElo = new HashMap<>();
        bobElo.put(chess.getId(), 1300);
        bobElo.put(darts.getId(), 1450);
        player2.setEloRatings(bobElo);
        playerService.createPlayer(player2);
        
        PlayerDTO player3 = new PlayerDTO();
        player3.setName("Charlie");
        Map<UUID, Integer> charlieElo = new HashMap<>();
        charlieElo.put(pingPong.getId(), 1600);
        charlieElo.put(darts.getId(), 1500);
        player3.setEloRatings(charlieElo);
        playerService.createPlayer(player3);
        
        PlayerDTO player4 = new PlayerDTO();
        player4.setName("Diana");
        Map<UUID, Integer> dianaElo = new HashMap<>();
        dianaElo.put(chess.getId(), 1400);
        dianaElo.put(pingPong.getId(), 1520);
        dianaElo.put(darts.getId(), 1480);
        player4.setEloRatings(dianaElo);
        playerService.createPlayer(player4);
    }
}
