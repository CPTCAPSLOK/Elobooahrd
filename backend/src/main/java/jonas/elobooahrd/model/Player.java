package jonas.elobooahrd.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jonas.elobooahrd.model.interfaces.PlayerInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a player in the Eloboard system.
 * A player can participate in multiple games and have different Elo ratings for each game.
 */
@Data
@NoArgsConstructor
@ToString
public class Player implements PlayerInterface {
    private UUID id;
    private String name;
    private String email;
    private Map<UUID, Integer> eloRatings = new HashMap<>(); // Maps game ID to Elo rating

    /**
     * Creates a new player with the given name and email
     */
    public Player(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
    }

    /**
     * Gets the player's Elo rating for a specific game
     * @param gameId The ID of the game
     * @return The player's Elo rating for the game, or a default value if not set
     */
    @Override
    public int getEloRating(UUID gameId) {
        return eloRatings.getOrDefault(gameId, 1000); // Default Elo rating is 1000
    }

    /**
     * Sets the player's Elo rating for a specific game
     * @param gameId The ID of the game
     * @param rating The new Elo rating
     */
    @Override
    public void setEloRating(UUID gameId, int rating) {
        eloRatings.put(gameId, rating);
    }

    /**
     * Updates the player's Elo rating for a specific game
     * @param gameId The ID of the game
     * @param ratingChange The change in Elo rating
     */
    @Override
    public void updateEloRating(UUID gameId, int ratingChange) {
        int currentRating = getEloRating(gameId);
        eloRatings.put(gameId, currentRating + ratingChange);
    }
}
