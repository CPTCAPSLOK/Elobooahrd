package jonas.elobooahrd.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jonas.elobooahrd.dto.GameDTO;
import jonas.elobooahrd.model.Dart;
import jonas.elobooahrd.model.Game;
import jonas.elobooahrd.model.GameContainer;
import jonas.elobooahrd.model.Tablefootball;

/**
 * Service for managing games in the Eloboard system.
 */
@Service
public class GameService {
    
    private GameContainer gameContainer;
    
    @Autowired
    public GameService(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }
    
    /**
     * Get all games
     * 
     * @return List of all games
     */
    public List<GameDTO> getAllGames() {
        return gameContainer.getAllGames().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a game by ID
     * 
     * @param id The game ID
     * @return The game if found
     */
    public Optional<GameDTO> getGame(UUID id) {
        Game game = gameContainer.getGame(id);
        return game != null ? Optional.of(convertToDTO(game)) : Optional.empty();
    }
    
    /**
     * Create a new game
     * 
     * @param gameDTO The game to create
     * @return The created game
     */
    public GameDTO createGame(GameDTO gameDTO) {
        Game game;
        
        // Create the appropriate game type based on the type field
        if ("Tablefootball".equals(gameDTO.getType())) {
            game = new Tablefootball(gameDTO.getName(), gameDTO.getDescription());
        } else if ("Dart".equals(gameDTO.getType())) {
            game = new Dart(gameDTO.getName(), gameDTO.getDescription());
        } else {
            throw new UnsupportedOperationException("Unsupported game type: " + gameDTO.getType());
        }
        
        // Set additional properties
        if (gameDTO.getInitialEloRating() > 0) {
            game.setInitialEloRating(gameDTO.getInitialEloRating());
        }
        
        if (gameDTO.getKFactor() > 0) {
            game.setKFactor(gameDTO.getKFactor());
        }
        
        // Add the game to the container
        gameContainer.addGame(game);
        
        return convertToDTO(game);
    }
    
    /**
     * Update a game
     * 
     * @param gameDTO The updated game data
     * @return The updated game
     */
    public GameDTO updateGame(GameDTO gameDTO) {
        Game game = gameContainer.getGame(gameDTO.getId());
        if (game != null) {
            game.setName(gameDTO.getName());
            game.setDescription(gameDTO.getDescription());
            game.setInitialEloRating(gameDTO.getInitialEloRating());
            game.setKFactor(gameDTO.getKFactor());
            return convertToDTO(game);
        }
        throw new IllegalArgumentException("Game not found: " + gameDTO.getId());
    }
    
    /**
     * Delete a game
     * 
     * @param id The game ID
     */
    public void deleteGame(UUID id) {
        gameContainer.removeGame(id);
    }
    
    /**
     * Get the game container
     * 
     * @return The game container
     */
    public GameContainer getGameContainer() {
        return this.gameContainer;
    }
    
    /**
     * Convert a Game entity to a GameDTO
     * 
     * @param game The game entity
     * @return The game DTO
     */
    private GameDTO convertToDTO(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setDescription(game.getDescription());
        dto.setInitialEloRating(game.getInitialEloRating());
        dto.setKFactor(game.getKFactor());
        dto.setType(game.getClass().getSimpleName());
        return dto;
    }
}
