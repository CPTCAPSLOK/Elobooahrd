package jonas.elobooahrd.model.interfaces;

import java.util.Map;
import java.util.UUID;

/**
 * Interface for players in the Eloboard system.
 */
public interface PlayerInterface {
    /**
     * Gets the unique identifier of the player
     * @return The player's UUID
     */
    UUID getId();
    
    /**
     * Gets the name of the player
     * @return The player's name
     */
    String getName();
    
    /**
     * Gets the email of the player
     * @return The player's email
     */
    String getEmail();
    
    /**
     * Gets all Elo ratings for this player across different games
     * @return Map of game IDs to Elo ratings
     */
    Map<UUID, Integer> getEloRatings();
    
    /**
     * Gets the player's Elo rating for a specific game
     * @param gameId The ID of the game
     * @return The player's Elo rating for the game, or a default value if not set
     */
    int getEloRating(UUID gameId);
    
    /**
     * Sets the player's Elo rating for a specific game
     * @param gameId The ID of the game
     * @param rating The new Elo rating
     */
    void setEloRating(UUID gameId, int rating);
    
    /**
     * Updates the player's Elo rating for a specific game
     * @param gameId The ID of the game
     * @param ratingChange The change in Elo rating
     */
    void updateEloRating(UUID gameId, int ratingChange);
}
