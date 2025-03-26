package jonas.elobooahrd.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jonas.elobooahrd.dto.PlayerDTO;
import jonas.elobooahrd.model.GameContainer;
import jonas.elobooahrd.model.Player;

/**
 * Service for managing players in the Eloboard system.
 */
@Service
public class PlayerService {
    
    private GameContainer gameContainer;
    
    @Autowired
    public PlayerService(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }
    
    /**
     * Get all players
     * 
     * @return List of all players
     */
    public List<PlayerDTO> getAllPlayers() {
        return gameContainer.getAllPlayers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get players by game
     * 
     * @param gameId The game ID
     * @return List of players for the specified game
     */
    public List<PlayerDTO> getPlayersByGame(UUID gameId) {
        return gameContainer.getAllPlayers().stream()
                .filter(player -> player.getEloRatings().containsKey(gameId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a player by ID
     * 
     * @param id The player ID
     * @return The player if found
     */
    public Optional<PlayerDTO> getPlayer(UUID id) {
        Player player = gameContainer.getPlayer(id);
        return player != null ? Optional.of(convertToDTO(player)) : Optional.empty();
    }
    
    /**
     * Create a new player
     * 
     * @param playerDTO The player to create
     * @return The created player
     */
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player player = new Player(playerDTO.getName(), ""); // Providing empty email as it's required
        player.setId(UUID.randomUUID());
        gameContainer.addPlayer(player);
        return convertToDTO(player);
    }
    
    /**
     * Update a player
     * 
     * @param playerDTO The updated player data
     * @return The updated player
     */
    public PlayerDTO updatePlayer(PlayerDTO playerDTO) {
        Player player = gameContainer.getPlayer(playerDTO.getId());
        if (player != null) {
            player.setName(playerDTO.getName());
            return convertToDTO(player);
        }
        throw new IllegalArgumentException("Player not found: " + playerDTO.getId());
    }
    
    /**
     * Delete a player
     * 
     * @param id The player ID
     */
    public void deletePlayer(UUID id) {
        gameContainer.removePlayer(id);
    }
    
    /**
     * Get player leaderboard for a game
     * 
     * @param gameId The game ID
     * @return List of players sorted by Elo rating
     */
    public List<PlayerDTO> getLeaderboard(UUID gameId) {
        return gameContainer.getLeaderboard(gameId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert a Player entity to a PlayerDTO
     * 
     * @param player The player entity
     * @return The player DTO
     */
    private PlayerDTO convertToDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setEloRatings(player.getEloRatings());
        return dto;
    }
}
