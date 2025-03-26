package jonas.elobooahrd.model.interfaces;

import java.util.List;
import java.util.UUID;

import jonas.elobooahrd.model.Player;

/**
 * Interface for teams in the Eloboard system.
 */
public interface TeamInterface {
    /**
     * Gets the unique identifier of the team
     * @return The team's UUID
     */
    UUID getId();
    
    /**
     * Gets the name of the team
     * @return The team's name
     */
    String getName();
    
    /**
     * Gets the list of players in this team
     * @return List of players
     */
    List<Player> getPlayers();
    
    /**
     * Adds a player to the team
     * @param player The player to add
     * @return true if the player was added, false otherwise
     */
    boolean addPlayer(Player player);
    
    /**
     * Removes a player from the team
     * @param playerId The ID of the player to remove
     * @return true if the player was removed, false otherwise
     */
    boolean removePlayer(UUID playerId);
    
    /**
     * Gets the team's Elo rating for a specific game
     * @param gameId The ID of the game
     * @return The team's Elo rating
     */
    int getTeamEloRating(UUID gameId);
}
